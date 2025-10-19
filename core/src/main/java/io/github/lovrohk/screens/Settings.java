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
import io.github.lovrohk.screensHelpful.SettingsManager;

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
    private float backTextX, backTextY;
    private boolean hovered;

    protected SettingsManager settingsManager;


    // menu navigation
    protected String[] menuItems = {
        "Master Volume",
        "Music Volume",
        "SFX Volume",
        "Language",
        "Resolution",
        "Fullscreen",
        "Background dim",
        "Note speed",
        "Keybinds",
        "Back"
    };
    protected int selectedIndex = 0;

    public Settings(Main game) {this.game = game;}

    @Override
    public void show() {
        settingsManager = new SettingsManager();

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

        backTextX = 13;
        backTextY = 50;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                float y = screenHeight - screenY;
                layout.setText(font, "Back");
                hovered = screenX >= backTextX && screenX <= backTextX + layout.width &&
                    y >= backTextY - layout.height && y <= backTextY;
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
        for (int i = 0; i < menuItems.length; i++) {
            boolean selected = (i == selectedIndex);
            font.setColor(selected ? Color.YELLOW : Color.WHITE);

            String label = menuItems[i];
            String value = getSettingValue(label);

            float y = 600 - i * 60;
            font.draw(batch, label, 100, y);

            // draw value aligned to the right
            layout.setText(font, value);
            font.draw(batch, value, 1000 - layout.width, y);
        }
        batch.end();


        if ((Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) || (hovered && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
            SettingsManager.save();
            this.dispose();
            game.setScreen(new MainMenu(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % menuItems.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + menuItems.length) % menuItems.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) adjustValue(-1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) adjustValue(1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            handleSelection();
        }

    }

    private void handleSelection() {
        String selected = menuItems[selectedIndex];

        switch (selected) {
            case "Back":
                game.setScreen(new MainMenu(game)); // or however you return
                break;

            case "Master Volume":
            case "Music Volume":
            case "SFX Volume":
            case "Language":
            case "Resolution":
            case "Fullscreen":
            case "Background dim":
            case "Note speed":
            case "Keybinds":
                break;

            default:
                break;
        }
    }
    private void adjustValue(int dir) {
        String selected = menuItems[selectedIndex];
        switch (selected) {
            case "Master Volume":
                SettingsManager.settings.masterVolume = Math.min(1f, Math.max(0f,
                    SettingsManager.settings.masterVolume + dir * 0.1f));
                SettingsManager.save();
                break;

            case "Music Volume":
                SettingsManager.settings.musicVolume = Math.min(1f, Math.max(0f,
                    SettingsManager.settings.musicVolume + dir * 0.1f));
                SettingsManager.save();
                break;

            case "SFX Volume":
                SettingsManager.settings.sfxVolume = Math.min(1f, Math.max(0f,
                    SettingsManager.settings.sfxVolume + dir * 0.1f));
                SettingsManager.save();
                break;

            case "Language":
                cycleLanguage(dir);
                SettingsManager.save();
                break;

            case "Resolution":
                cycleResolution(dir);
                SettingsManager.save();
                break;

            case "Fullscreen":
                SettingsManager.settings.fullscreen = !SettingsManager.settings.fullscreen;
                SettingsManager.save();
                break;

            case "Background dim":
                SettingsManager.settings.backgrounDim = Math.min(1f, Math.max(0f,
                    SettingsManager.settings.backgrounDim + dir * 0.1f));
                SettingsManager.save();
                break;

            case "Note speed":
                SettingsManager.settings.noteSpeed = Math.min(1f, Math.max(0f,
                    SettingsManager.settings.noteSpeed + dir * 0.1f));
                SettingsManager.save();
                break;

            case "Keybinds":
                break;
        }

        SettingsManager.save();
    }
    private void cycleLanguage(int dir) {
        String[] langs = {"en", "sl", "de"};
        int i = 0;
        for (int j = 0; j < langs.length; j++)
            if (langs[j].equals(SettingsManager.settings.language)) i = j;
        i = (i + dir + langs.length) % langs.length;
        SettingsManager.settings.language = langs[i];
    }

    private void cycleResolution(int dir) {
        int[][] resolutions = {{1280, 720}, {1600, 900}, {1920, 1080}};
        int i = 0;
        for (int j = 0; j < resolutions.length; j++)
            if (resolutions[j][0] == SettingsManager.settings.resolutionWidth) i = j;
        i = (i + dir + resolutions.length) % resolutions.length;
        SettingsManager.settings.resolutionWidth = resolutions[i][0];
        SettingsManager.settings.resolutionHeight = resolutions[i][1];
    }
    private String getSettingValue(String name) {
        switch (name) {
            case "Master Volume":
                return String.format("%.0f%%", SettingsManager.settings.masterVolume * 100);
            case "Music Volume":
                return String.format("%.0f%%", SettingsManager.settings.musicVolume * 100);
            case "SFX Volume":
                return String.format("%.0f%%", SettingsManager.settings.sfxVolume * 100);
            case "Language":
                return SettingsManager.settings.language.toUpperCase();
            case "Resolution":
                return SettingsManager.settings.resolutionWidth + "x" + SettingsManager.settings.resolutionHeight;
            case "Fullscreen":
                return SettingsManager.settings.fullscreen ? "ON" : "OFF";
            case "Background dim":
                return String.format("%.0f%%", SettingsManager.settings.backgrounDim * 100);
            case "Note speed":
                return String.format("%.0f", SettingsManager.settings.noteSpeed * 100);
            case "Keybinds":
            default:
                return "";
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
