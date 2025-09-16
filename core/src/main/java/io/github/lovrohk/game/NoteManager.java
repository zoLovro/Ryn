package io.github.lovrohk.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class NoteManager {
    private List<Note> notes;
    List<Note> toRemove = new ArrayList<>();

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
        float hitWindow = 0.2f;
        float minDiff = Float.MAX_VALUE;

        for(Note note : notes) {
            float diff = Math.abs(songTime - note.time);
            if(diff < minDiff) { minDiff = diff; closest = note; }
        }

        if(closest != null && minDiff <= hitWindow) {
            closest.hit();
        }
    }
}
