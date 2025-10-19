package io.github.lovrohk;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import io.github.lovrohk.screens.GameLoop;
import io.github.lovrohk.screens.MainMenu;
import io.github.lovrohk.screens.SongSelect;
import io.github.lovrohk.screensHelpful.SettingsManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public AssetManager assetManager;
    @Override
    public void create() {
        SettingsManager.load();
        assetManager = new AssetManager();
        setScreen(new MainMenu(this));
    }
}

