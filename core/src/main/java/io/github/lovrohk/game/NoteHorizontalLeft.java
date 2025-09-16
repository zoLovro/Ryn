package io.github.lovrohk.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NoteHorizontalLeft extends Note{

    public NoteHorizontalLeft (float time) {
        super(time);
    }

    @Override
    public void update(float delta, float songTime) {
        x -= 250 * delta;
    }

    @Override
    public void draw(SpriteBatch batch) {

    }
}
