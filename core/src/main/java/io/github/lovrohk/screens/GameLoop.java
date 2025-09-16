package io.github.lovrohk.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.lovrohk.game.Note;
import io.github.lovrohk.game.NoteManager;
import io.github.lovrohk.game.NoteVertical;

import java.util.ArrayList;
import java.util.List;

/** First screen of the application. Displayed after the application is created. */
public class GameLoop implements Screen {
    private Note a = new NoteVertical(7);
    private Note b = new NoteVertical(5);
    private NoteManager noteManager;
    private float songTime;
    private SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    int screenWidth;
    int screenHeight;

    @Override
    public void show() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        List<Note> notes = new ArrayList<>();
        notes.add(a);
        notes.add(b);

        noteManager = new NoteManager(notes);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the hitline
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE); // any color
        shapeRenderer.rect(0, 50, screenWidth, 3);
        shapeRenderer.end();

        songTime += delta;  // delta = time since last frame
        noteManager.update(delta, songTime);

        // drawing notes
        batch.begin();
        noteManager.draw(batch);
        batch.end();

        // hit detection
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) noteManager.checkHit(songTime);
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        batch.dispose();
    }
}
