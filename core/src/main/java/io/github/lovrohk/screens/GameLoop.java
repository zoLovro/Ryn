package io.github.lovrohk.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.lovrohk.game.*;

import java.util.ArrayList;
import java.util.List;

/** First screen of the application. Displayed after the application is created. */
public class GameLoop implements Screen {
    private NoteManager noteManager;
    private float songTime;
    private SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    List<Note> notes;
    List<Note> tempNote = new ArrayList<>();

    Texture emptyNoteDown;

    ScoreManager scoreManager;
    BitmapFont font;
    String displayText = "Score: 0";
    float acc;
    String formatted;

    private Music song;

    private int screenWidth;
    private int screenHeight;
    private OrthographicCamera camera;
    private Viewport viewport;

    private int portionOfScreen;
    private int lane1;
    private int lane2;
    private int lane3;
    private int lane4;
    private int hitLineHeight;

    @Override
    public void show() {
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
        viewport = new FitViewport(1920, 1080, camera); //
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        // useful
        screenWidth = (int) viewport.getWorldWidth();
        screenHeight = (int) viewport.getWorldHeight();
        portionOfScreen = (int) screenWidth/6;
        lane1 = 10;
        lane2 = 85;
        lane3 = 160;
        lane4 = 235;
        hitLineHeight = 48;

        // note manager filling
        noteManager = new NoteManager(tempNote, scoreManager);
        FileHandle file = Gdx.files.internal("maps/testMap.txt");
        notes = noteManager.fillNotes(file);
        noteManager.setNotes(notes);

        // other textures
        emptyNoteDown = new Texture(Gdx.files.internal("hitline/emptyHit.png"));
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        songTime += delta;  // delta = time since last frame
        noteManager.update(delta, songTime);

        // drawing everything
        batch.begin();


        // notes
        noteManager.draw(batch);
        // text
        font.draw(batch, displayText, 0, screenHeight);

        // hitline stuff
        batch.draw(emptyNoteDown, 802, hitLineHeight, 64, 64);
        batch.draw(emptyNoteDown, 886, hitLineHeight, 64, 64);
        batch.draw(emptyNoteDown, 970, hitLineHeight, 64, 64);
        batch.draw(emptyNoteDown, 1054, hitLineHeight, 64, 64);
        batch.end();

        // hit detection
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) noteManager.checkHit(songTime, 0);
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) noteManager.checkHit(songTime, 1);
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) noteManager.checkHit(songTime, 2);
        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) noteManager.checkHit(songTime, 3);
        if(Gdx.input.isKeyJustPressed(Input.Keys.K)) noteManager.checkHit(songTime, 4);
        if(Gdx.input.isKeyJustPressed(Input.Keys.L)) noteManager.checkHit(songTime, 5);

        scoreManager.update(noteManager.getAccuracy());
        acc = scoreManager.getAccuracy();
        formatted = String.format("%.2f", acc);
        displayText = "Score: " + scoreManager.getScore() + " Accuracy: " + formatted + " Combo: " + scoreManager.getCombo();
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
