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
