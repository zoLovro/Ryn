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
    public void draw(SpriteBatch batch, float currentSongTime) {
        if (currentSongTime > getTime2()) return; // slider is done

        float headY = (getTime1() - currentSongTime) * speed + hitLine;
        float tailY = (getTime2() - currentSongTime) * speed + hitLine;

        if (isHolding()) {
            headY = hitLine; // lock head at the hit line
        }

        float height = tailY - headY;
        if (height <= 0) return; // no body left → disappear immediately

        // Draw the body
        batch.draw(verticalTexture, transformLane(getLane()), headY, 50, height);

        // Draw the head only if not holding
        if (!isHolding()) {
            batch.draw(verticalTexture, transformLane(getLane()), headY);
        }

        // Always draw the tail until slider is complete
        batch.draw(verticalTexture, transformLane(getLane()), tailY);
    }



    private int transformLane(int lane) {
        int result = switch (lane) {
            case 0 -> 10;
            case 1 -> 90;
            case 2 -> 170;
            case 3 -> 250;
            case 4 -> 330;
            case 5 -> 410;
            default -> 0;
        };
        return result;
    }

    public int getHitLine() {
        return hitLine;
    }
}
