package io.github.lovrohk;

import com.badlogic.gdx.Game;
import io.github.lovrohk.screens.GameLoop;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new GameLoop());
    }
}
