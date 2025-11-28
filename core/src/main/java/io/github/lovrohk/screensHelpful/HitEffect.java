package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HitEffect {

    private float x, y;
    private float scale;

    private float lifetime = 0.3f;   // how long to show
    private float age = 0f;
    private boolean active = false;

    private Texture hitTexture;

    public HitEffect(int textureNum, float x, float y, float scale) {
        this.x = x;
        this.y = y;
        this.hitTexture = selectTexture(textureNum);
        this.scale = scale;
    }

    // called when a note is hit
    public void trigger() {
        age = 0f;
        active = true;
    }

    public void update(float delta) {
        if (!active) return;
        age += delta;
        if (age >= lifetime) {
            active = false;
        }
    }

    public void draw(SpriteBatch batch) {
        if (!active || hitTexture == null) return;

        float alpha = 1f - (age / lifetime);  // fade out
        Color old = batch.getColor();
        batch.setColor(1f, 1f, 1f, alpha);

        float w = hitTexture.getWidth() * scale;
        float h = hitTexture.getHeight() * scale;

        batch.draw(hitTexture,
            x - w / 2f,
            y - h / 2f,
            w,
            h
        );

        batch.setColor(old);
    }

    private Texture selectTexture(int textureNum) {
        return switch (textureNum) {
            case 0 -> new Texture(Gdx.files.internal("skins/testSkin/hitTexture-300.png"));
            case 1 -> new Texture(Gdx.files.internal("skins/testSkin/hitTexture-300-Combo.png"));
            case 2 -> new Texture(Gdx.files.internal("skins/testSkin/hitTexture-200.png"));
            case 3 -> new Texture(Gdx.files.internal("skins/testSkin/hitTexture-50.png"));
            case 4 -> new Texture(Gdx.files.internal("skins/testSkin/hitTexture-miss.png"));
            default -> null;
        };
    }

    public void changeScale(float newScale) {
        this.scale = newScale;
    }
}
