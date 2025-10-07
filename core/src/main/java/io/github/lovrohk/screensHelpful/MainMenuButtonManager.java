package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MainMenuButtonManager {
    protected Texture gameStartButton = new Texture(Gdx.files.internal("gameImages/gameStartButton.png"));
    protected Texture selectGameStartButton = new Texture(Gdx.files.internal("gameImages/gameStartButtonSelect.png"));
    protected Texture editButton = new Texture(Gdx.files.internal("gameImages/editButton.png"));
    protected Texture selectEditButton = new Texture(Gdx.files.internal("gameImages/editButtonSelect.png"));
    protected Texture settingsButton = new Texture(Gdx.files.internal("gameImages/settingsButton.png"));
    protected Texture selectSettingsButton = new Texture(Gdx.files.internal("gameImages/settingsButtonSelect.png"));
    protected Texture githubButton = new Texture(Gdx.files.internal("gameImages/githubButton.png"));
    protected Texture selectGithubButton = new Texture(Gdx.files.internal("gameImages/githubButtonSelect.png"));
    protected Texture exitButton = new Texture(Gdx.files.internal("gameImages/exitButton.png"));
    protected Texture selectExitButton = new Texture(Gdx.files.internal("gameImages/exitButtonSelect.png"));

    public MainMenuButtonManager() {}

    public void drawGameStartButton(Batch batch, int x, int y) {
        batch.draw(gameStartButton, x, y, gameStartButton.getWidth(), gameStartButton.getHeight());
    }
    public void drawSelectGameStartButton(Batch batch, int x, int y) {
        batch.draw(selectGameStartButton, x, y, selectGameStartButton.getWidth(), selectGameStartButton.getHeight());
    }

    public void drawEditButton(Batch batch, int x, int y) {
        batch.draw(editButton, x, y, editButton.getWidth(), editButton.getHeight());
    }
    public void drawSelectEditButton(Batch batch, int x, int y) {
        batch.draw(selectEditButton, x, y, selectEditButton.getWidth(), selectEditButton.getHeight());
    }

    public void drawSettingsButton(Batch batch, int x, int y) {
        batch.draw(settingsButton, x, y, settingsButton.getWidth(), settingsButton.getHeight());
    }
    public void drawSelectSettingsButton(Batch batch, int x, int y) {
        batch.draw(selectSettingsButton, x, y, selectSettingsButton.getWidth(), selectSettingsButton.getHeight());
    }

    public void drawGithubButton(Batch batch, int x, int y) {
        batch.draw(githubButton, x, y, githubButton.getWidth(), githubButton.getHeight());
    }
    public void drawSelectGithubButton(Batch batch, int x, int y) {
        batch.draw(selectGithubButton, x, y, selectGithubButton.getWidth(), selectGithubButton.getHeight());
    }

    public void drawExitButton(Batch batch, int x, int y) {
        batch.draw(exitButton, x, y, exitButton.getWidth(), exitButton.getHeight());
    }
    public void drawSelectExitButton(Batch batch, int x, int y) {
        batch.draw(selectExitButton, x, y, selectExitButton.getWidth(), selectExitButton.getHeight());
    }

    public int getButtonWidth() { return gameStartButton.getWidth(); }
    public int getButtonHeight() { return gameStartButton.getHeight(); }
}
