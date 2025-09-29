package io.github.lovrohk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NoteVertical extends Note {
    Texture verticalTexture = new Texture(Gdx.files.internal("notes/verticalNoteUpdated.png"));
    private float distanceFromHitLine;
    private int speed = 350;
    private int hitLine = 50;

    public NoteVertical(float time, int lane) {
        super(time, lane);
    }

    @Override
    public void update(float delta, float songTime) {
        distanceFromHitLine = (time - songTime) * speed;
        y = hitLine + distanceFromHitLine;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(verticalTexture, transformLane(lane), y);
    }

    private int transformLane(int lane) {
        int result = switch (lane) {
            case 1 -> 802;
            case 2 -> 886;
            case 3 -> 970;
            case 4 -> 1054;
            default -> 0;
        };
        return result;
    }

    public int getHitLine() {
        return hitLine;
    }
}
