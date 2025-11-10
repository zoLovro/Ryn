package io.github.lovrohk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.lovrohk.screensHelpful.SettingsManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NoteManager {
    protected List<Note> notes;
    protected List<Note> toRemove = new ArrayList<>();
    protected int[] accuracy  = new int[]{0, 0, 0};
    protected ScoreManager scoreManager;

    protected float sfxVolume = SettingsManager.settings.sfxVolume * SettingsManager.settings.masterVolume;

    // health (in the future every map will hold the HPstat in the map file)
    HealthbarManager healthbarManager = new HealthbarManager(5);

    // sound stuff
    Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("skins/testSkin/hitSound.wav"));
    Sound missSound = Gdx.audio.newSound(Gdx.files.internal("skins/testSkin/missSound.mp3"));

    public NoteManager(List<Note> notes, ScoreManager scoreManager) {
        this.notes = notes;
        this.scoreManager = scoreManager;
    }


    public void update(float delta, float songTime) {
        for (Note note : notes) {
            note.update(delta, songTime);

            boolean isHold = note instanceof HoldNote;
            boolean shouldRemove = false;

            if (note.isHit()) {
                shouldRemove = true;
            } else if (isHold) {
                HoldNote hold = (HoldNote) note;

                // miss only if the end time has passed and player never held it
                if (!hold.isHolding() && songTime - hold.getEndTime() > 0.25f) {
                    accuracy[0]++;
                    scoreManager.resetCombo();
                    scoreManager.update(accuracy);
                    healthbarManager.missHealth();
                    //missSound.play(sfxVolume);
                    shouldRemove = true;
                }
            } else {
                // normal tap note miss
                if (songTime - note.getTime() > 0.25f) {
                    accuracy[0]++;
                    scoreManager.resetCombo();
                    scoreManager.update(accuracy);
                    healthbarManager.missHealth();
                    //missSound.play(sfxVolume);
                    shouldRemove = true;
                }
            }

            if (shouldRemove) {
                toRemove.add(note);
            }
        }

        if (!toRemove.isEmpty()) {
            notes.removeAll(toRemove);
            toRemove.clear();
        }

        healthbarManager.healthDraw();
    }


    public void draw(SpriteBatch batch) {
        for (Note note : notes) {
            note.draw(batch);
        }
    }

    public void checkHit(float songTime, int inputLane) {
        Note closest = null;
        float hit200 = 0.1f;   // perfect
        float hit50  = 0.15f;    // okay
        float missWindow = 0.5f; // miss
        float minDiff = Float.MAX_VALUE;

        for (Note note : notes) {
            if (note.isHit()) continue;
            if (note.getLane() != inputLane) continue;
            float diff = Math.abs(songTime - note.time);
            if (diff < minDiff) {
                minDiff = diff;
                closest = note;
            }
        }

        if (closest == null) return;

        // --- HOLD NOTE HANDLING ---
        if (closest instanceof HoldNote holdNote) {
            if (!holdNote.isHolding()) {
                // Start holding if pressed close enough to start time
                if (minDiff <= hit200) {
                    holdNote.startHold();
                    accuracy[2]++;
                    scoreManager.addCombo();
                    scoreManager.update(accuracy);
                    healthbarManager.hit200Health();
                    // hitSound.play(sfxVolume);
                } else if (minDiff <= hit50) {
                    holdNote.startHold();
                    accuracy[1]++;
                    scoreManager.addCombo();
                    scoreManager.update(accuracy);
                    healthbarManager.hit50Health();
                    // hitSound.play(sfxVolume);
                } else if (minDiff <= missWindow) {
                    // pressed too early/late
                    accuracy[0]++;
                    scoreManager.resetCombo();
                    scoreManager.update(accuracy);
                    healthbarManager.missHealth();
                    //missSound.play(sfxVolume);
                }
            }
            return;
        }

        // --- NORMAL TAP NOTE HANDLING ---
        if (minDiff <= hit200) {
            closest.hit();
            accuracy[2]++;
            scoreManager.addCombo();
            scoreManager.update(accuracy);
            healthbarManager.hit200Health();
            // hitSound.play(sfxVolume);
        } else if (minDiff <= hit50) {
            closest.hit();
            accuracy[1]++;
            scoreManager.addCombo();
            scoreManager.update(accuracy);
            healthbarManager.hit50Health();
            // hitSound.play(sfxVolume);
        } else if (minDiff <= missWindow) {
            closest.hit();
            accuracy[0]++;
            scoreManager.resetCombo();
            scoreManager.update(accuracy);
            healthbarManager.missHealth();
            //missSound.play(sfxVolume);
        }
    }

    public List<Note> fillNotes(FileHandle fileHandle) {
        List<Note> notes1 = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(fileHandle.reader())) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] temp = line.split(",");
                if (temp[0].equalsIgnoreCase("note")) {
                    float time = Float.parseFloat(temp[1].replace("f", "").trim());
                    int lane = Integer.parseInt(temp[2].replace(";", "").trim());
                    notes1.add(new NoteVertical(time, lane));
                } else if (temp[0].equalsIgnoreCase("hold")) {
                    float start = Float.parseFloat(temp[1].replace("f", "").trim());
                    float end = Float.parseFloat(temp[2].replace("f", "").trim());
                    int lane = Integer.parseInt(temp[3].replace(";", "").trim());
                    notes1.add(new HoldNote(start, end, lane));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return notes1;
    }

    public void releaseHold(float songTime, int inputLane) {
        for (Note note : notes) {
            if (note instanceof HoldNote hold && hold.getLane() == inputLane && hold.isHolding()) {
                hold.releaseHold(songTime);

                // Check timing
                float diffEnd = Math.abs(songTime - hold.getEndTime());
                if (diffEnd <= 0.1f) {
                    // released on time
                    accuracy[2]++; // perfect
                    scoreManager.addCombo();
                    scoreManager.update(accuracy);
                    healthbarManager.hit200Health();
                    // hitSound.play(sfxVolume);
                } else if (songTime < hold.getEndTime() - 0.15f) {
                    // released too early -> miss
                    accuracy[0]++;
                    scoreManager.resetCombo();
                    scoreManager.update(accuracy);
                    healthbarManager.missHealth();
                    //missSound.play(sfxVolume);
                } else {
                    // released close but not perfect
                    accuracy[1]++;
                    scoreManager.addCombo();
                    scoreManager.update(accuracy);
                    healthbarManager.hit50Health();
                    // hitSound.play(sfxVolume);
                }

                // always remove hold after release
                hold.hit();
                break;
            }
        }
    }




    public void setNotes(List<Note> a) {
        notes = a;
    }
    public List<Note> getNotes() { return notes; }

    public int[] getAccuracy() {
        return accuracy.clone(); // prevents unwanted modification
    }
    public HealthbarManager getHealthbarManager() {
        return healthbarManager;
    }
}
