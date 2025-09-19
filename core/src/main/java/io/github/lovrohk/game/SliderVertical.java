package io.github.lovrohk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SliderVertical extends Slider{
    Texture verticalTexture = new Texture(Gdx.files.internal("notes/verticalNote.png"));
    private float distanceFromHitLine;
    private int speed = 150;
    private int hitLine = 50;

    public SliderVertical(float time1, float time2, int lane) {
        super(time1, time2, lane);
    }

    @Override
    public void update(float delta, float songTime) {
        distanceFromHitLine = (time1 - songTime) * speed;
        y = hitLine + distanceFromHitLine;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(verticalTexture, transformLane(lane), y);
    }

    private int transformLane(int lane) {
        int result = switch (lane) {
            case 0 -> 10;
            case 1 -> 85;
            case 2 -> 160;
            case 3 -> 235;
            case 4 -> 310;
            case 5 -> 385;
            default -> 0;
        };
        return result;
    }

    public int getHitLine() {
        return hitLine;
    }
}
