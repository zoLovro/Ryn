package io.github.lovrohk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteManager {
    private List<Note> notes;
    List<Note> toRemove = new ArrayList<>();
    int[] accuracy  = new int[]{0, 0, 0};
    int combo = 0;
    private ScoreManager scoreManager;

    // sound stuff
    Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/hitSound.wav"));
    Sound missSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/missSound.mp3"));

    public NoteManager(List<Note> notes, ScoreManager scoreManager) {
        this.notes = notes;
        this.scoreManager = scoreManager;
    }


    public void update(float delta, float songTime) {
        for (Note note : notes) {
            note.update(delta, songTime);

            if(note.isHit()) {
                toRemove.add(note);
            }
        }

        if(!toRemove.isEmpty()) {
            notes.removeAll(toRemove);
            toRemove.clear();
        }
    }

    public void draw(SpriteBatch batch) {
        for (Note note : notes) {
            note.draw(batch);
        }
    }

    public void checkHit(float songTime, int inputLane) {
        Note closest = null;
        float hit200 = 0.05f;   // perfect
        float hit50  = 0.1f;    // okay
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
                scoreManager.addCombo();
                scoreManager.update(accuracy);
                hitSound.play(0.4f);
            } else if (minDiff <= hit50) {
                closest.hit();
                scoreManager.addCombo();
                scoreManager.update(accuracy);
                hitSound.play(0.4f);
            } else if (minDiff <= missWindow) {
                closest.hit();
                scoreManager.resetCombo();
                scoreManager.update(accuracy);
                missSound.play(0.4f);
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

    public int[] getAccuracy() {
        return accuracy.clone(); // prevents unwanted modification
    }
    public int getCombo() {
        return combo;
    }
    public void addCombo(int c) {
        combo += c;
    }
}
