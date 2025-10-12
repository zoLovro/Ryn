package io.github.lovrohk.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import io.github.lovrohk.Main;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.InputAdapter;

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

    private BitmapFont font;
    private GlyphLayout layout;
    private float textX, textY;
    private boolean hovered;

    public Settings(Main game) {this.game = game;}

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Set fixed world size
        int worldWidth = 1920;
        int worldHeight = 1080;

        // Camera + viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();
        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
        camera.update();

        screenWidth = worldWidth;
        screenHeight = worldHeight;

        // Background
        background = new Texture(Gdx.files.internal("gameImages/settingsBG.jpg"));

        // Font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skins/testSkin/CAVOLINI.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        layout = new GlyphLayout();

        textX = screenWidth / 2f - 100;
        textY = screenHeight / 2f;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                float y = screenHeight - screenY;
                layout.setText(font, "Back");
                hovered = screenX >= textX && screenX <= textX + layout.width &&
                    y >= textY - layout.height && y <= textY;
                return false;
            }
        });
    }


    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0, screenWidth, screenHeight);

        font.setColor(hovered ? Color.YELLOW : Color.WHITE);
        font.draw(batch, "Back", textX, textY);

        batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {this.dispose(); game.setScreen(new MainMenu(game));}
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
