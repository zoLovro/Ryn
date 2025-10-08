package io.github.lovrohk.screens;

import com.badlogic.gdx.Screen;
import io.github.lovrohk.Main;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

/** First screen of the application. Displayed after the application is created. */
public class Settings implements Screen {
    protected Main game;
    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;

    
    protected int screenWidth;
    protected int screenHeight;
    protected OrthographicCamera camera;
    protected Viewport viewport;

    protected Texture background; 

    public Settings(Main game) {this.game = game;}

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

        // bg
        background = new Texture(Gdx.files.internal("gameImages/settingsBG.jpg"));
        
        // useful
        screenWidth = (int) viewport.getWorldWidth();
        screenHeight = (int) viewport.getWorldHeight();
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.

        // viewport and camera for window resize
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera); //
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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
    }
}
