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
    private List<Slider> sliders;
    List<Slider> toRemove = new ArrayList<>();
    int[] accuracy  = new int[]{0, 0, 0};
    int combo;
    private ScoreManager scoreManager;

    // sound stuff
    Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/hitSound.wav"));
    Sound missSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/missSound.mp3"));

    public SliderManager(List<Slider> sliders, ScoreManager scoreManager) {
        this.sliders = sliders;
        this.scoreManager = scoreManager;
    }

    public void update(float delta, float songTime) {
        for (Slider slider : sliders) {
            slider.update(delta, songTime);

            // ✅ Case 1: Finished holding correctly → slider done
            if (slider.isHolding() && songTime >= slider.getTime2()) {
                slider.stopHolding();   // you need this in Slider
                scoreManager.addCombo();
                accuracy[2]++;          // treat as "200" / perfect finish
                scoreManager.update(accuracy);
                hitSound.play(0.4f);
                toRemove.add(slider);
                continue; // done with this slider
            }

            // ✅ Case 2: Slider passed without being held → miss
            if (!slider.isHolding() && !slider.isHit() && songTime > slider.getTime2()) {
                slider.hit();
                scoreManager.resetCombo();
                accuracy[0]++;          // miss
                scoreManager.update(accuracy);
                missSound.play(0.4f);
                toRemove.add(slider);
                continue;
            }

            // ✅ Optional Case 3: If you let go early (released before tail)
            if (slider.isHolding() && songTime < slider.getTime2()) {
                // Here you could detect if the player released the key too soon.
                // For now, just leave it; you’ll need input handling for "release".
            }
        }

        if (!toRemove.isEmpty()) {
            sliders.removeAll(toRemove);
            toRemove.clear();
        }
    }



    public void draw(SpriteBatch batch, float currentSongTime) {
        for (Slider slider : sliders) {
            slider.draw(batch, currentSongTime);
        }
    }

    public void checkHit(float songTime, int inputLane) {
        Slider closest = null;
        float hit200 = 0.05f;   // perfect
        float hit50  = 0.1f;    // okay
        float missWindow = 0.5f; // miss
        float minDiff = Float.MAX_VALUE;

        for(Slider slider : sliders) {
            if (slider.getLane() != inputLane) continue;
            float diff = Math.abs(songTime - slider.time1);
            if(diff < minDiff) { minDiff = diff; closest = slider; }
        }

        if (closest != null) {
            if (minDiff <= hit200) {
                // good head hit
                closest.startHolding();          // begin holding
                scoreManager.addCombo();
                accuracy[2]++;                   // count 200
                scoreManager.update(accuracy);
                hitSound.play(0.4f);

            } else if (minDiff <= hit50) {
                closest.startHolding();          // begin holding
                scoreManager.addCombo();
                accuracy[1]++;                   // count 50
                scoreManager.update(accuracy);
                hitSound.play(0.4f);

            } else if (minDiff <= missWindow) {
                closest.hit();                   // fail instantly
                scoreManager.resetCombo();
                accuracy[0]++;                   // miss
                scoreManager.update(accuracy);
                missSound.play(0.4f);
            }
        }

    }

    public List<Slider> fillSliders(FileHandle fileHandle) {
        List<Slider> sliders1 = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(fileHandle.reader())) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] temp = line.split(",");
                if (temp[0].equalsIgnoreCase("slider")) {
                    float time1 = Float.parseFloat(temp[1].replace("f", "").trim());
                    float time2 = Float.parseFloat(temp[2].replace("f", "").trim());
                    int lane = Integer.parseInt(temp[3].replace(";", "").trim());

                    sliders1.add(new SliderVertical(time1, time2, lane));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sliders1;
    }


    public void setSliders(List<Slider> a) {
        sliders = a;
    }

    public int[] getAccuracy() {
        return accuracy.clone(); // prevents unwanted modification
    }
    public void addCombo(int c) {
        combo += c;
    }

}
