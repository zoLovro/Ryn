package io.github.lovrohk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.lovrohk.screensHelpful.SettingsManager;

public class HoldNote extends Note{
    private static final Texture holdBody = new Texture(Gdx.files.internal("skins/testSkin/holdBody.png"));
    private static final Texture verticalTextureLeft = new Texture(Gdx.files.internal("skins/testSkin/verticalNoteLeft.png"));
    private static final Texture verticalTextureDown = new Texture(Gdx.files.internal("skins/testSkin/verticalNoteDown.png"));
    private static final Texture verticalTextureUp = new Texture(Gdx.files.internal("skins/testSkin/verticalNoteUp.png"));
    private static final Texture verticalTextureRight = new Texture(Gdx.files.internal("skins/testSkin/verticalNoteRight.png"));
    private Texture texture;

    private float endTime;
    private float speed = SettingsManager.settings.noteSpeed * 1000;
    private int hitLine = 50;
    private boolean holding = false;
    private boolean releasedEarly = false;

    public HoldNote(float startTime, float endTime, int lane) {
        super(startTime, lane);
        this.endTime = endTime;

        switch (lane) {
            case 1 -> texture = verticalTextureLeft;
            case 2 -> texture = verticalTextureDown;
            case 3 -> texture = verticalTextureUp;
            case 4 -> texture = verticalTextureRight;
            default -> texture = verticalTextureDown;
        }
    }

    @Override
    public void update(float delta, float songTime) {
        float startDist = (time - songTime) * speed;
        float endDist = (endTime - songTime) * speed;
        y = hitLine + startDist;

        if (holding && songTime > endTime) {
            hit = true;
            holding = false;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        float bodyOffset = 32f;
        float bodyY = y + bodyOffset;
        float height = (endTime - time) * speed - bodyOffset; // reduce body height
        if (height < 0) height = 0;
        // draw body with offset
        batch.setColor(1, 1, 1, 0.5f); // translucent
        batch.draw(holdBody, transformLane(lane), bodyY, holdBody.getWidth(), height);
        // draw head
        batch.setColor(1, 1, 1, 1f);
        batch.draw(texture, transformLane(lane), y, texture.getWidth(), texture.getHeight());
    }


    private int transformLane(int lane) {
        return switch (lane) {
            case 1 -> 802;
            case 2 -> 886;
            case 3 -> 970;
            case 4 -> 1054;
            default -> 0;
        };
    }



    public void startHold() { holding = true; }
    public void releaseHold(float songTime) {
        if (songTime < endTime - 0.1f) releasedEarly = true;
        hit = !releasedEarly;
        holding = false;
    }

    public boolean isHolding() { return holding; }
    public float getEndTime() {return endTime;}
}
