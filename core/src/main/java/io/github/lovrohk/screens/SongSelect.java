package io.github.lovrohk.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.lovrohk.Main;
import io.github.lovrohk.screensHelpful.Song;
import io.github.lovrohk.screensHelpful.SongManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.lovrohk.screensHelpful.SongTile;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


/** First screen of the application. Displayed after the application is created. */
public class SongSelect implements Screen {
    protected Stage stage;
    ScrollPane scrollPane;
    Table table;
    BitmapFont font;

    SongManager songManager;
    ShapeRenderer shapeRenderer;
    protected final Main game;

    protected int screenWidth;
    protected int screenHeight;
    protected OrthographicCamera camera;
    protected Viewport viewport;

    protected int songTileWidth;
    protected int songTileHeight;
    protected Batch batch;

    protected float mouseX;
    protected float mouseY;

    Texture bgTexture;
    Image bgImage;
    Song selectedSong = null; // store currently selected song
    protected float curVol = 0;
    protected Music currentSongPreview;
    protected float targetVolume = 0.2f; // or whatever you want
    protected float fadeDuration = 2f; // seconds
    protected float fadeElapsed = 0f;
    protected boolean fadingIn = false;

    protected Table infoPanel;
    protected Label titleLabel;
    protected Label artistLabel;
    protected Label bpmLabel;
    protected Label.LabelStyle labelStyle;
    protected float infoPanelX, infoPanelY, infoPanelWidth, infoPanelHeight;
    protected SongTile selectedTile;


    public SongSelect(Main game) {this.game = game;}

    @Override
    public void show() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // so stage receives input

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

        // font stuff
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skins/testSkin/CAVOLINI.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36; // font size in pixels
        font = generator.generateFont(parameter);
        generator.dispose();


        // info panel
        labelStyle = new Label.LabelStyle(font, Color.WHITE);

        infoPanel = new Table();
        infoPanel.top().right(); // align top-right
        infoPanel.pad(10); // some padding from the edges

        infoPanelWidth = 700; // width of the panel
        infoPanelHeight = 180; // height of the panel (enough for 3 labels for now)
        infoPanelX = screenWidth - infoPanelWidth - 20; // padding from right
        infoPanelY = screenHeight - infoPanelHeight - 10; // padding from top

        // positioning the table to match the rectangle
        infoPanel.setPosition(infoPanelX, infoPanelY);
        infoPanel.setSize(infoPanelWidth, infoPanelHeight);
        infoPanel.align(Align.topLeft);
        infoPanel.defaults().left().padLeft(10).padTop(5);

        // creating labels with initial empty text
        titleLabel = new Label("Title: ", labelStyle);
        artistLabel = new Label("Artist: ", labelStyle);
        bpmLabel = new Label("BPM: ", labelStyle);

        // adding labels to the table vertically
        infoPanel.add(titleLabel).row();
        infoPanel.add(artistLabel).row();
        infoPanel.add(bpmLabel).row();
        infoPanel.pack();

        // add panel to stage
        stage.addActor(infoPanel);

        // drawing tiles and other stuff
        table = new Table();
        table.top().left(); // align everything to the top
        table.setFillParent(true); // table fills the stage
        table.padLeft(0); // optional padding around table
        table.defaults().spaceBottom(10); // spacing between rows

        for (Song song : songManager.getSongs()) {
            SongTile tile = new SongTile(song, font);
            tile.setSize(tile.getWidth(), tile.getHeight());

            tile.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (selectedSong == song) {
                        // Second click: start the game
                        int index = songManager.getSongs().indexOf(song);
                        changeToGame(index);
                    } else {
                        if (selectedTile != null) {
                            selectedTile.clearActions();
                            selectedTile.addAction(Actions.scaleTo(1f, 1f, 0.2f)); // smooth shrink
                        }

                        // select new tile
                        selectedTile = tile;
                        selectedSong = song;

                        // animating the tile when it's clicked on
                        tile.clearActions();
                        tile.addAction(Actions.scaleTo(1.15f, 1.05f, 0.2f)); // smooth grow

                        // Update background
                        bgTexture = new Texture(Gdx.files.internal(selectedSong.getBackgroundFile()));

                        // updating info label
                        titleLabel.setText("Title: " + song.getTitle());
                        artistLabel.setText("Artist: " + song.getArtist());
                        bpmLabel.setText("BPM: " + song.getBpm());

                        // Dispose previous preview if exists
                        if (currentSongPreview != null) {
                            currentSongPreview.stop();
                            currentSongPreview.dispose();
                        }

                        // Load new song preview
                        currentSongPreview = Gdx.audio.newMusic(Gdx.files.internal(selectedSong.getAudioFileTrimmed()));
                        currentSongPreview.setLooping(true);
                        currentSongPreview.setVolume(0f); // start silent
                        currentSongPreview.play();

                        // Start fade-in
                        fadeElapsed = 0f;
                        fadingIn = true;
                    }
                }
            });

            table.add(tile).left().row(); // add the SongTile itself
        }
        scrollPane = new ScrollPane(table);
        scrollPane.setFillParent(true); // make it fill the stage
        scrollPane.setScrollingDisabled(true, false); // allow vertical scrolling only
        stage.addActor(scrollPane);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (bgTexture != null) {
            batch.draw(bgTexture, 0, 0, screenWidth, screenHeight);
        }
        batch.end();

        stage.act(delta);

        if (fadingIn && currentSongPreview != null) {
            fadeElapsed += Gdx.graphics.getDeltaTime();
            float volume = Math.min(fadeElapsed / fadeDuration * targetVolume, targetVolume);
            currentSongPreview.setVolume(volume);

            if (volume >= targetVolume) fadingIn = false; // done fading in
        }

        // Draw panel background
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f); // semi-transparent black
        shapeRenderer.rect(infoPanelX, infoPanelY, infoPanelWidth, infoPanelHeight);
        shapeRenderer.end();


        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.dispose();
            if(currentSongPreview != null) currentSongPreview.stop();
            game.setScreen(new MainMenu(game));}
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
        batch.dispose();
    }

    private void changeToGame(int song) {
        // Dispose SongSelect resources if you don’t need them
        batch.dispose();
        stage.dispose();
        if(currentSongPreview.isPlaying()) currentSongPreview.stop();

        // Get the selected song (here we pick the first one as an example)
        songManager.selectSong(song); // replace 0 with your selected index
        Song selectedSong = songManager.getSelectedSong();

        // Switch to GameLoop screen
        game.setScreen(new GameLoop(selectedSong, game));
    }

    private void fadeInMusic(Music music, float value) {
        music.setVolume(value);
    }
}
