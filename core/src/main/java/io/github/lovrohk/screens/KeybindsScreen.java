package io.github.lovrohk.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.lovrohk.Main;
import io.github.lovrohk.screensHelpful.SettingsManager;

/** First screen of the application. Displayed after the application is created. */
public class KeybindsScreen implements Screen {
    protected Main game;
    protected SpriteBatch batch;

    protected int key1, key2, key3, key4;
    protected int currentKey = 0;


    public KeybindsScreen(Main game) {
        this.game = game;
    }
    @Override
    public void show() {
        // Prepare your screen here.
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (currentKey) {
                    case 0: key1 = keycode; break;
                    case 1: key2 = keycode; break;
                    case 2: key3 = keycode; break;
                    case 3: key4 = keycode; break;
                }
                currentKey++;
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2f);

        // instructions
        font.draw(batch, "Press keys for each lane:", 100, 600);

        // show which key is currently being assigned
        String[] keyLabels = {"Lane 1", "Lane 2", "Lane 3", "Lane 4"};
        int[] keys = {key1, key2, key3, key4};

        for (int i = 0; i < 4; i++) {
            String keyText = (keys[i] == 0) ? "..." : Input.Keys.toString(keys[i]);
            if (i == currentKey && currentKey < 4)
                font.setColor(Color.YELLOW);
            else
                font.setColor(Color.WHITE);

            font.draw(batch, keyLabels[i] + ": " + keyText, 100, 500 - i * 80);
        }

        // when done, show confirmation text
        if (currentKey >= 4) {
            font.setColor(Color.GREEN);
            font.draw(batch, "All keys set! Press ENTER to confirm.", 100, 150);

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                // convert int keycodes to readable strings
                SettingsManager.settings.keybinds = new String[] {
                    Input.Keys.toString(key1),
                    Input.Keys.toString(key2),
                    Input.Keys.toString(key3),
                    Input.Keys.toString(key4)
                };

                SettingsManager.save();
                game.setScreen(new Settings(game));
            }
        }

        batch.end();
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
