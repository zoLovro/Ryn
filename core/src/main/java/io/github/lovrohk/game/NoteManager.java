package io.github.lovrohk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NoteManager {
    private List<Note> notes;
    List<Note> toRemove = new ArrayList<>();
    int[] accuracy  = new int[]{0, 0, 0};
    int combo = 0;

    // sound stuff
    Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/hitSound.wav"));
    Sound missSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/missSound.mp3"));

    public NoteManager(List<Note> notes) {
        this.notes = notes;
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

    public void checkHit(float songTime) {
        Note closest = null;
        float hit200 = 0.05f;   // perfect
        float hit50  = 0.1f;    // okay
        float missWindow = 0.5f; // miss
        float minDiff = Float.MAX_VALUE;

        for(Note note : notes) {
            float diff = Math.abs(songTime - note.time);
            if(diff < minDiff) { minDiff = diff; closest = note; }
        }

        if (closest != null) {
            if (minDiff <= hit200) {
                closest.hit();
                accuracy[2]++;  // perfect
                hitSound.play(0.4f);
                combo++;
            } else if (minDiff <= hit50) {
                closest.hit();
                accuracy[1]++;  // off a bit
                hitSound.play(0.4f);
                combo++;
            } else if (minDiff <= missWindow) {
                closest.hit();
                accuracy[0]++;  // miss
                missSound.play(0.4f);
                combo = 0;
            }
        } // need to add songTime + missWindow for automatic misses if player didn't hit
    }

    public List<Note> fillNotes(FileHandle fileHandle) {
        List<Note> notes1 = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(fileHandle.reader())) {
            String line = br.readLine();
            String[] temp = line.split(",");
            for (String a : temp) {
                Note note = new NoteVertical(Float.parseFloat(a));
                notes1.add(note);
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
}
