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
import com.badlogic.gdx.math.Rectangle;
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
    Texture fullNoteDown;
    Texture emptyNoteTexture1;
    Texture emptyNoteTexture2;
    Texture emptyNoteTexture3;
    Texture emptyNoteTexture4;
    float mouseX;
    float mouseY;

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

    private boolean isPaused;
    Texture pauseScreenTexture;
    ButtonManager buttonManager;
    Rectangle continueButtonRect;
    Rectangle restartButtonRect;
    Rectangle exitButtonRect;

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
        fullNoteDown = new Texture(Gdx.files.internal("hitline/fullHit.png"));

        // screen management
        isPaused = false;

        // pause screen
        pauseScreenTexture = new Texture(Gdx.files.internal("screenTextures/pauseScreen.png"));
        buttonManager = new ButtonManager();
        continueButtonRect = new Rectangle(17, 220, buttonManager.getContinueTextureWidth(), buttonManager.getContinueTextureHeight());
        restartButtonRect = new Rectangle(17, 120, buttonManager.getRestartTextureWidth(), buttonManager.getRestartTextureHeight());
        exitButtonRect = new Rectangle(17, 20, buttonManager.getExitTextureWidth(), buttonManager.getExitTextureHeight());
    }

    @Override
    public void render(float delta) {
        if(!isPaused) {
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
                emptyNoteTexture1 = Gdx.input.isKeyPressed(Input.Keys.D)
                    ? fullNoteDown
                    : emptyNoteDown;
                emptyNoteTexture2 = Gdx.input.isKeyPressed(Input.Keys.F)
                    ? fullNoteDown
                    : emptyNoteDown;
                emptyNoteTexture3 = Gdx.input.isKeyPressed(Input.Keys.J)
                    ? fullNoteDown
                    : emptyNoteDown;
                emptyNoteTexture4 = Gdx.input.isKeyPressed(Input.Keys.K)
                    ? fullNoteDown
                    : emptyNoteDown;
                batch.draw(emptyNoteTexture1, 802, hitLineHeight, 64, 64);
                batch.draw(emptyNoteTexture2, 886, hitLineHeight, 64, 64);
                batch.draw(emptyNoteTexture3, 970, hitLineHeight, 64, 64);
                batch.draw(emptyNoteTexture4, 1054, hitLineHeight, 64, 64);


            batch.end();

            // hit detection
            if(Gdx.input.isKeyJustPressed(Input.Keys.D)) noteManager.checkHit(songTime, 1);
            if(Gdx.input.isKeyJustPressed(Input.Keys.F)) noteManager.checkHit(songTime, 2);
            if(Gdx.input.isKeyJustPressed(Input.Keys.J)) noteManager.checkHit(songTime, 3);
            if(Gdx.input.isKeyJustPressed(Input.Keys.K)) noteManager.checkHit(songTime, 4);

            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) pause();

            scoreManager.update(noteManager.getAccuracy());

            acc = scoreManager.getAccuracy();
            if (Float.isNaN(acc)) {
                acc = 100f;
            }

            formatted = String.format("%.2f", acc);
            displayText = "Score: " + scoreManager.getScore() + " Accuracy: " + formatted + "%" + " Combo: " + scoreManager.getCombo();
        } else {
            // pause screen
            mouseX = Gdx.input.getX();
            mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            renderPauseScreen();
            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {resume();}
            if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {restart(); resume();}

            if(continueButtonRect.contains(mouseX, mouseY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {resume();}
            if(restartButtonRect.contains(mouseX, mouseY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {restart(); resume();}
            if(exitButtonRect.contains(mouseX, mouseY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {exit();}
        }
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
        isPaused = true;
        song.pause();


    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
        isPaused = false;
        song.play();

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

    private void renderPauseScreen() {
        batch.begin();

            batch.draw(pauseScreenTexture, 0, 0, 1920, 1080);
            buttonManager.drawContinueButton(17, 220, batch);
            buttonManager.drawRestartButton(17, 120, batch);
            buttonManager.drawExitButton(17, 20, batch);

        batch.end();
    }

    private void restart() {
        song.stop();
        songTime = 0f;

        // Playing music :D
        song = Gdx.audio.newMusic(Gdx.files.internal("audio/songs/testSong.mp3"));
        song.setVolume(0.2f);
        song.setLooping(false);
        song.play();

        // score n stuff
        scoreManager = new ScoreManager();

        // note manager filling
        noteManager = new NoteManager(tempNote, scoreManager);
        FileHandle file = Gdx.files.internal("maps/testMap.txt");
        notes = noteManager.fillNotes(file);
        noteManager.setNotes(notes);

        // pause screen
        buttonManager = new ButtonManager();
        continueButtonRect = new Rectangle(17, 220, buttonManager.getContinueTextureWidth(), buttonManager.getContinueTextureHeight());
        restartButtonRect = new Rectangle(17, 120, buttonManager.getRestartTextureWidth(), buttonManager.getRestartTextureHeight());
        exitButtonRect = new Rectangle(17, 20, buttonManager.getExitTextureWidth(), buttonManager.getExitTextureHeight());
    }

    private void exit() {

    }
}
