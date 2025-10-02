package io.github.lovrohk.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.lovrohk.Main;
import io.github.lovrohk.screensHelpful.Song;
import io.github.lovrohk.screensHelpful.SongManager;

/** First screen of the application. Displayed after the application is created. */
public class SongSelect implements Screen {
    SongManager songManager;
    ShapeRenderer shapeRenderer;
    private final Main game;

    private int screenWidth;
    private int screenHeight;
    private OrthographicCamera camera;
    private Viewport viewport;

    private int songTileWidth;
    private int songTileHeight;
    private Batch batch;

    protected float mouseX;
    protected float mouseY;

    public SongSelect(Main game) {this.game = game;}

    @Override
    public void show() {
        batch = new SpriteBatch();

        // camera stuff
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera); //
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        // useful
        screenWidth = (int) viewport.getWorldWidth();
        screenHeight = (int) viewport.getWorldHeight();

        // preparing the ui
        shapeRenderer = new ShapeRenderer();

        // Preparing the song manager
        songManager = new SongManager();
        songManager.fillSongs();
    }

    @Override
    public void render(float delta) {
        // camera stuff
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // getting mouse x, y
        mouseX = Gdx.input.getX();
        mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        // drawing song tiles
        songManager.drawSongTile(screenHeight, batch);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {changeToGame();}

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

    private void changeToGame() {
        // Dispose SongSelect resources if you don’t need them
        batch.dispose();

        // Get the selected song (here we pick the first one as an example)
        songManager.selectSong(0); // replace 0 with your selected index
        Song selectedSong = songManager.getSelectedSong();

        // Switch to GameLoop screen
        game.setScreen(new GameLoop(selectedSong));
    }
}
