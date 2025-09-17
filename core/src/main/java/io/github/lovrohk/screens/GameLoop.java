package io.github.lovrohk.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.lovrohk.game.Note;
import io.github.lovrohk.game.NoteManager;
import io.github.lovrohk.game.NoteVertical;
import io.github.lovrohk.game.ScoreManager;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/** First screen of the application. Displayed after the application is created. */
public class GameLoop implements Screen {
    private NoteManager noteManager;
    private float songTime;
    private SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    List<Note> notes;
    List<Note> temp = new ArrayList<>();

    ScoreManager scoreManager;
    BitmapFont font;
    String displayText = "Score: 0";
    float acc;
    String formatted;

    private Music song;

    int screenWidth;
    int screenHeight;
    private OrthographicCamera camera;
    private Viewport viewport;

    @Override
    public void show() {
        // note manager filling
        noteManager = new NoteManager(temp);
        FileHandle file = Gdx.files.internal("maps/testMap.txt");
        notes = noteManager.fillNotes(file);
        noteManager.setNotes(notes);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Playing music :D
        song = Gdx.audio.newMusic(Gdx.files.internal("audio/songs/testSong.mp3"));
        song.setVolume(0.2f);
        song.setLooping(false);
        song.play();

        // score n stuff
        scoreManager = new ScoreManager();
        font = new BitmapFont();
        font.getData().setScale(2);

        // viewport and camera for window resize
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera); // 800x600 is your "virtual" resolution
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        // useful
        screenWidth = (int) viewport.getWorldWidth();
        screenHeight = (int) viewport.getWorldHeight();
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the hitline
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE); // any color
        shapeRenderer.rect(0, 50, screenWidth, 3);
        shapeRenderer.end();

        songTime += delta;  // delta = time since last frame
        noteManager.update(delta, songTime);

        // drawing everything
        batch.begin();


        // notes
        noteManager.draw(batch);

        // text
        font.draw(batch, displayText, 0, screenHeight);


        batch.end();

        // hit detection
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) noteManager.checkHit(songTime);

        scoreManager.update(noteManager.getAccuracy(), noteManager.getCombo());
        acc = scoreManager.getAccuracy();
        formatted = String.format("%.2f", acc);
        displayText = "Score: " + scoreManager.getScore() + " Accuracy: " + formatted + " Combo: " + noteManager.getCombo();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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
        if (song != null) song.stop();
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        if (song != null) song.dispose();
        batch.dispose();
    }
}
