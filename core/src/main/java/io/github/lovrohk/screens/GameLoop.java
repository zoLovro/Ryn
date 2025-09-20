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
import io.github.lovrohk.game.*;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/** First screen of the application. Displayed after the application is created. */
public class GameLoop implements Screen {
    private NoteManager noteManager;
    private SliderManager sliderManager;
    private float songTime;
    private SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    List<Note> notes;
    List<Note> tempNote = new ArrayList<>();
    List<Slider> tempSlider = new ArrayList<>();
    List<Slider> sliders;

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
        viewport = new FitViewport(800, 600, camera); // 800x600 is your "virtual" resolution
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

        // slider manager filling
        sliderManager = new SliderManager(tempSlider, scoreManager);
        sliders = sliderManager.fillSliders(file);
        sliderManager.setSliders(sliders);
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render the hitline
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE); // any color
        // horizontal lines
        shapeRenderer.rect(0, hitLineHeight, screenWidth, 1);
        // vertical lines
        shapeRenderer.rect(lane1, 0, 1, screenHeight);
        shapeRenderer.rect(lane2, 0, 1, screenHeight);
        shapeRenderer.rect(lane3, 0, 1, screenHeight);
        shapeRenderer.rect(lane4, 0, 1, screenHeight);
        shapeRenderer.rect(portionOfScreen*5, 0, 1, screenHeight);
        shapeRenderer.end();

        songTime += delta;  // delta = time since last frame
        noteManager.update(delta, songTime);

        // drawing everything
        batch.begin();


        // notes
        noteManager.draw(batch);
        sliderManager.draw(batch, songTime);
        // text
        font.draw(batch, displayText, 0, screenHeight);


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
