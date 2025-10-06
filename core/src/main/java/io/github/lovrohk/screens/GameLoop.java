package io.github.lovrohk.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.lovrohk.Main;
import io.github.lovrohk.game.*;
import io.github.lovrohk.screensHelpful.RankAchievedManager;
import io.github.lovrohk.screensHelpful.Song;

import java.util.ArrayList;
import java.util.List;

/** First screen of the application. Displayed after the application is created. */
public class GameLoop implements Screen {
    private final Main game;
    private Song selectedSong;

    protected NoteManager noteManager;
    protected float songTime;
    protected SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    List<Note> notes;
    List<Note> tempNote = new ArrayList<>();
    FileHandle file;

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

    protected Music song;

    protected int screenWidth;
    protected int screenHeight;
    protected OrthographicCamera camera;
    protected Viewport viewport;

    protected int portionOfScreen;
    protected int lane1;
    protected int lane2;
    protected int lane3;
    protected int lane4;
    protected int hitLineHeight;

    protected boolean isPaused;
    protected boolean failed;
    protected boolean finished;
    Texture pauseScreenTexture;
    Texture finishedScreenTexture;
    ButtonManager buttonManager;
    Rectangle continueButtonRectPause;
    Rectangle restartButtonRectPause;
    Rectangle exitButtonRectPause;
    Rectangle restartButtonRectFinished;
    Rectangle exitButtonRectFinished;

    RankAchievedManager rankAchievedManager;

    Texture bgTexture;

    public GameLoop(Song selectedSong, Main game) {
        this.song = Gdx.audio.newMusic(Gdx.files.internal(selectedSong.getAudioFile()));
        this.file = Gdx.files.internal(selectedSong.getNoteFile());
        this.game = game;
        this.selectedSong = selectedSong;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Playing music :D
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
        notes = noteManager.fillNotes(file);
        noteManager.setNotes(notes);

        // other textures
        emptyNoteDown = new Texture(Gdx.files.internal("hitline/emptyHit.png"));
        fullNoteDown = new Texture(Gdx.files.internal("hitline/fullHit.png"));

        // screen management
        isPaused = false;
        failed = false;
        finished = false;

        // pause screen
        pauseScreenTexture = new Texture(Gdx.files.internal("screenTextures/pauseScreen.png"));
        buttonManager = new ButtonManager();
        continueButtonRectPause = new Rectangle(17, 220, buttonManager.getContinueTextureWidth(), buttonManager.getContinueTextureHeight());
        restartButtonRectPause = new Rectangle(17, 120, buttonManager.getRestartTextureWidth(), buttonManager.getRestartTextureHeight());
        exitButtonRectPause = new Rectangle(17, 20, buttonManager.getExitTextureWidth(), buttonManager.getExitTextureHeight());

        // background
        bgTexture = new Texture(Gdx.files.internal(selectedSong.getBackgroundFile()));

        // finished screen
        rankAchievedManager = new RankAchievedManager(scoreManager);
        finishedScreenTexture = new Texture(Gdx.files.internal("screenTextures/finishedScreen.png"));
        restartButtonRectFinished = new Rectangle(screenWidth-buttonManager.getRestartTextureWidth()-17, 120,
            buttonManager.getRestartTextureWidth(), buttonManager.getRestartTextureHeight());
        exitButtonRectFinished = new Rectangle(screenWidth-buttonManager.getExitTextureWidth()-17, 20,
            buttonManager.getExitTextureWidth(), buttonManager.getExitTextureHeight());

    }

    @Override
    public void render(float delta) {
        if(!isPaused && !failed && !finished) {
            camera.update();
            batch.setProjectionMatrix(camera.combined);

            songTime += delta;  // delta = time since last frame
            noteManager.update(delta, songTime);

            // drawing everything
            batch.begin();
                // background
                batch.setColor(0.6f, 0.6f, 0.6f, 1f);
                batch.draw(bgTexture, 0, 0, screenWidth, screenHeight);
                batch.setColor(Color.WHITE);

                // notes
                noteManager.draw(batch);
                // text
                font.draw(batch, displayText, 0, screenHeight - 30);

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

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 1, 1);
            shapeRenderer.rect(5, screenHeight - 20, noteManager.getHealthbarManager().getHealth() * 5, 15);
            shapeRenderer.end();

            displayText = "Score: " + scoreManager.getScore() + " Accuracy: " + formatted + "%" + " Combo: " + scoreManager.getCombo();

            if(noteManager.getHealthbarManager().getHealth() <= 0) failed = true;
            if(noteManager.getNotes().isEmpty()) finished = true;
        }
        else if(failed) {
            pause();
            mouseX = Gdx.input.getX();
            mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            renderFailScreen();
            if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {restart(); resume(); failed = false;}

            if(restartButtonRectPause.contains(mouseX, mouseY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {restart(); resume(); failed = false;}
            if(exitButtonRectPause.contains(mouseX, mouseY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {exit();}
        }
        else if(finished) {
            pause();
            mouseX = Gdx.input.getX();
            mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            renderFinishedScreen();
            if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {restart(); resume(); finished = false;}

            if(restartButtonRectFinished.contains(mouseX, mouseY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {restart(); resume(); finished = false;}
            if(exitButtonRectFinished.contains(mouseX, mouseY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {exit();}
        }
        else if(isPaused) {
            // pause screen
            mouseX = Gdx.input.getX();
            mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            renderPauseScreen();
            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {resume();}
            if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {restart(); resume();}

            if(continueButtonRectPause.contains(mouseX, mouseY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {resume();}
            if(restartButtonRectPause.contains(mouseX, mouseY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {restart(); resume();}
            if(exitButtonRectPause.contains(mouseX, mouseY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {exit();}
        }
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        shapeRenderer.setProjectionMatrix(camera.combined);
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

    private void restart() {
        song.stop();
        songTime = 0f;
        song.setVolume(0.2f);
        song.setLooping(false);
        song.play();

        // score n stuff
        scoreManager = new ScoreManager();

        // note manager filling
        noteManager = new NoteManager(tempNote, scoreManager);
        notes = noteManager.fillNotes(file);
        noteManager.setNotes(notes);

        // pause screen
        buttonManager = new ButtonManager();
        continueButtonRectPause = new Rectangle(17, 220, buttonManager.getContinueTextureWidth(), buttonManager.getContinueTextureHeight());
        restartButtonRectPause = new Rectangle(17, 120, buttonManager.getRestartTextureWidth(), buttonManager.getRestartTextureHeight());
        exitButtonRectPause = new Rectangle(17, 20, buttonManager.getExitTextureWidth(), buttonManager.getExitTextureHeight());
    }

    private void renderPauseScreen() {
        batch.begin();

            batch.draw(pauseScreenTexture, 0, 0, 1920, 1080);
            buttonManager.drawContinueButton(17, 220, batch);
            buttonManager.drawRestartButton(17, 120, batch);
            buttonManager.drawExitButton(17, 20, batch);

        batch.end();
    }

    private void renderFailScreen() {
        batch.begin();

            batch.draw(pauseScreenTexture, 0, 0, 1920, 1080);
            buttonManager.drawRestartButton((int) restartButtonRectFinished.getX(), 120, batch);
            buttonManager.drawExitButton((int) exitButtonRectFinished.getX(), 20, batch);

        batch.end();
    }

    private void renderFinishedScreen() {
        batch.begin();

            batch.draw(finishedScreenTexture, 0, 0, 1920, 1080);
            buttonManager.drawRestartButton(17, 120, batch);
            buttonManager.drawExitButton(17, 20, batch);

            batch.draw(rankAchievedManager.calculateAchievedRank(), 300, 300, 400, 400); // CHANGE THIS PLS

        batch.end();
    }

    private void exit() {
        // Dispose SongSelect resources if you don’t need them
        batch.dispose();
        song.stop();

        // Switch to GameLoop screen
        game.setScreen(new SongSelect(game));
    }
}
