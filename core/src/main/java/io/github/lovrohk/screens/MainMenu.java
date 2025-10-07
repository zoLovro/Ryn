package io.github.lovrohk.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.lovrohk.Main;
import io.github.lovrohk.screensHelpful.MainMenuButtonManager;

import java.awt.*;

/** First screen of the application. Displayed after the application is created. */
public class MainMenu implements Screen {
    protected Main game;

    protected int screenWidth;
    protected int screenHeight;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Batch batch;
    protected ShapeRenderer shapeRenderer;
    protected MainMenuButtonManager mainMenuButtonManager;

    protected int buttonsX;
    protected int gameStartButtonY;
    protected int editButtonY;
    protected int settingsButtonY;
    protected int githubButtonY;
    protected int exitButtonY;
    protected float mouseX;
    protected float mouseY;

    protected Rectangle gameStartButtonRect;
    protected Rectangle editButtonRect;
    protected Rectangle settingsButtonRect;
    protected Rectangle githubButtonRect;
    protected Rectangle exitButtonRect;

    protected Texture mainMenuBG;
    protected Texture logo;

    public MainMenu(Main game) {this.game = game;}

    @Override
    public void show() {
        // Prepare your screen here.
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // viewport and camera for window resize
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera); //
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        // button manager
        mainMenuButtonManager = new MainMenuButtonManager();
        buttonsX = 40;
        gameStartButtonY = 800;
        editButtonY = 700;
        settingsButtonY = 600;
        githubButtonY = 500;
        exitButtonY = 400;

        gameStartButtonRect = new Rectangle(buttonsX, gameStartButtonY, mainMenuButtonManager.getButtonWidth(),
            mainMenuButtonManager.getButtonHeight());
        editButtonRect = new Rectangle(buttonsX, editButtonY, mainMenuButtonManager.getButtonWidth(),
            mainMenuButtonManager.getButtonHeight());
        settingsButtonRect = new Rectangle(buttonsX, settingsButtonY, mainMenuButtonManager.getButtonWidth(),
            mainMenuButtonManager.getButtonHeight());
        githubButtonRect = new Rectangle(buttonsX, githubButtonY, mainMenuButtonManager.getButtonWidth(),
            mainMenuButtonManager.getButtonHeight());
        exitButtonRect = new Rectangle(buttonsX, exitButtonY, mainMenuButtonManager.getButtonWidth(),
            mainMenuButtonManager.getButtonHeight());

        mainMenuBG = new Texture(Gdx.files.internal("gameImages/mainMenuBG.png"));
        logo = new Texture(Gdx.files.internal("gameImages/RYN_logo_mid.png"));
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

            // bg goes here
            batch.draw(mainMenuBG, 0, 0);
            batch.draw(logo, buttonsX, 824);

            // mouse capture
            mouseX = Gdx.input.getX();
            mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // buttons
            if (gameStartButtonRect.contains(mouseX, mouseY)) {
                mainMenuButtonManager.drawSelectGameStartButton(batch, buttonsX, gameStartButtonY);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    game.setScreen(new SongSelect(game));
                }
            } else {
                mainMenuButtonManager.drawGameStartButton(batch, buttonsX, gameStartButtonY);
            }

            if (editButtonRect.contains(mouseX, mouseY)) {
                mainMenuButtonManager.drawSelectEditButton(batch, buttonsX, editButtonY);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    Gdx.net.openURI("https://github.com/zoLovro/Ryn");
                }
            } else {
                mainMenuButtonManager.drawEditButton(batch, buttonsX, editButtonY);
            }

            if (settingsButtonRect.contains(mouseX, mouseY)) {
                mainMenuButtonManager.drawSelectSettingsButton(batch, buttonsX, settingsButtonY);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    game.setScreen(new Settings(game));
                }
            } else {
                mainMenuButtonManager.drawSettingsButton(batch, buttonsX, settingsButtonY);
            }

            if (githubButtonRect.contains(mouseX, mouseY)) {
                mainMenuButtonManager.drawSelectGithubButton(batch, buttonsX, githubButtonY);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    Gdx.net.openURI("https://github.com/zoLovro/Ryn");
                }
            } else {
                mainMenuButtonManager.drawGithubButton(batch, buttonsX, githubButtonY);
            }

            if (exitButtonRect.contains(mouseX, mouseY)) {
                mainMenuButtonManager.drawSelectExitButton(batch, buttonsX, exitButtonY);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    Gdx.app.exit();
                }
            } else {
                mainMenuButtonManager.drawExitButton(batch, buttonsX, exitButtonY);
            }

        batch.end();
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
    }
}
