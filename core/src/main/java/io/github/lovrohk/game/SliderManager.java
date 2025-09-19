package io.github.lovrohk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SliderManager {
    private List<Note> sliders;
    List<Note> toRemove = new ArrayList<>();
    int[] accuracy  = new int[]{0, 0, 0};

     // sound stuff
    Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/hitSound.wav"));
    Sound missSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/missSound.mp3"));


    public SliderManager(List<Note> sliders) {
        this.sliders = sliders;
    }

    public void update(float delta, float songTime) {
        for (Slider slider : sliders) {
            slider.update(delta, songTime);

            if(slider.isHit()) {
                // implement logic for what happens if slider is hit
                continue;
            }
        }

        if(!toRemove.isEmpty()) {
            sliders.removeAll(toRemove);
            toRemove.clear();
        }
    }

    public void draw(SpriteBatch batch) {
        for (Slider slider : sliders) {
            slider.draw(batch);
        }
    }

    public void checkHit(float songTime, int inputLane, float time1, float time2) {
        
    }

    public List<Note> fillNotes(FileHandle fileHandle) {
        List<Note> sliders1 = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(fileHandle.reader())) {
            String line;
            while ((line = br.readLine()) != null) {  // loop through all lines
                line = line.trim();
                if (line.isEmpty()) continue; // skip empty lines

                String[] temp = line.split(",");
                if(temp[0].toLowerCase().equals("slider")) { 
                    float time = Float.parseFloat(temp[1].replace("f", "").trim()); // remove "f" if present
                    int lane = Integer.parseInt(temp[2].replace(";", "").trim());   // remove ";" if present
                    notes1.add(new NoteVertical(time, lane));
                }
                else continue;
            }
                
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sliders1;
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