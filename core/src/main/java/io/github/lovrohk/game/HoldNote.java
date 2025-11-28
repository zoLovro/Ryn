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
    private int lane;

    private float endY;

    public HoldNote(float startTime, float endTime, int lane) {
        super(startTime, lane);
        this.endTime = endTime;
        this.lane = lane;

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
        endY = hitLine + endDist;

        if (holding && songTime > endTime) {
            hit = true;
            holding = false;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        float laneX = transformLane(lane);

        // head center and tail center
        float headCenterY = y;
        float tailCenterY = endY;

        // 1) draw body between head and tail centers
        float bodyTop = Math.max(headCenterY, tailCenterY);
        float bodyBottom = Math.min(headCenterY, tailCenterY);
        float bodyHeight = bodyTop - bodyBottom;

        if (bodyHeight < 0) bodyHeight = 0;

        // body is drawn from its bottom-left
        batch.setColor(1, 1, 1, 0.5f);
        batch.draw(holdBody,
            laneX,
            bodyBottom,             // bottom at the lower center
            holdBody.getWidth(),
            bodyHeight
        );

        // 2) draw head, with y as CENTER
        batch.setColor(1, 1, 1, 1f);
        float headBottomY = headCenterY - texture.getHeight() / 2f;

        batch.draw(texture,
            laneX,                 // left as before
            headBottomY,           // adjusted so y is center
            texture.getWidth(),
            texture.getHeight()
        );
    }

    private int transformLane(int lane) {
        return switch (lane) {
            case 1 -> 704;
            case 2 -> 832;
            case 3 -> 960;
            case 4 -> 1088;
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
