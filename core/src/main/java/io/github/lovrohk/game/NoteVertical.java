package io.github.lovrohk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.lovrohk.screensHelpful.SettingsManager;

public class NoteVertical extends Note {
    private static final Texture verticalTextureLeft = new Texture(Gdx.files.internal("skins/testSkin/verticalNoteLeft.png"));
    private static final Texture verticalTextureDown = new Texture(Gdx.files.internal("skins/testSkin/verticalNoteDown.png"));
    private static final Texture verticalTextureUp = new Texture(Gdx.files.internal("skins/testSkin/verticalNoteUp.png"));
    private static final Texture verticalTextureRight = new Texture(Gdx.files.internal("skins/testSkin/verticalNoteRight.png"));

    private final Texture texture;

    private float distanceFromHitLine;
    private float speed = SettingsManager.settings.noteSpeed * 1000;
    private int hitLine = 50;

    float drawX;
    float drawY;

    public NoteVertical(float time, int lane) {
        super(time, lane);

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
        distanceFromHitLine = (time - songTime) * speed;
        y = hitLine + distanceFromHitLine;
    }

    @Override
    public void draw(SpriteBatch batch) {
        float textureHeight = texture.getHeight();

        drawX = transformLane(lane);
        drawY = y - textureHeight / 2;

        batch.draw(texture, drawX, drawY);
    }


    private int transformLane(int lane) {
        int result = switch (lane) {
            case 1 -> 704;
            case 2 -> 832;
            case 3 -> 960;
            case 4 -> 1088;
            default -> 0;
        };
        return result;
    }

    public int getHitLine() {
        return hitLine;
    }
}
