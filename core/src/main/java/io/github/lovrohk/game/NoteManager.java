package io.github.lovrohk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NoteManager {
    protected List<Note> notes;
    protected List<Note> toRemove = new ArrayList<>();
    protected int[] accuracy  = new int[]{0, 0, 0};
    protected ScoreManager scoreManager;

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

            if (note.isHit() || songTime - note.getTime() > 0.25) {
                toRemove.add(note);
                if (!note.isHit() && songTime - note.getTime() > 0.25) {
                    accuracy[0] += 1;
                    scoreManager.resetCombo();
                    scoreManager.update(accuracy);
                    healthbarManager.missHealth();
                    missSound.play(0.1f);
                }
            }
        }

        if(!toRemove.isEmpty()) {
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

        for(Note note : notes) {
            if (note.getLane() != inputLane) continue;
            float diff = Math.abs(songTime - note.time);
            if(diff < minDiff) { minDiff = diff; closest = note; }
        }

        if (closest != null) {
            if (minDiff <= hit200) {
                closest.hit();
                accuracy[2] += 1;
                scoreManager.addCombo();
                scoreManager.update(accuracy);
                healthbarManager.hit200Health();
                hitSound.play(0.1f);
            } else if (minDiff <= hit50) {
                closest.hit();
                accuracy[1] += 1;
                scoreManager.addCombo();
                scoreManager.update(accuracy);
                healthbarManager.hit50Health();
                hitSound.play(0.1f);
            } else if (minDiff <= missWindow) {
                closest.hit();
                accuracy[0] += 1;
                scoreManager.resetCombo();
                scoreManager.update(accuracy);
                healthbarManager.missHealth();
                missSound.play(0.1f);
            }
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
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return notes1;
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
