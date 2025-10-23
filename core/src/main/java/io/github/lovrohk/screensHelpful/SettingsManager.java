package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Json;

public class SettingsManager {
    private static final String FILE_NAME = "settings.json";
    private static final Json json = new Json();

    // This is your actual settings object
    public static SettingsData settings = new SettingsData();

    /** Load settings from file, or create defaults if none exist */
    public static void load() {
        if (Gdx.files.local(FILE_NAME).exists()) {
            settings = json.fromJson(SettingsData.class, Gdx.files.local(FILE_NAME));
        } else {
            setDefaults();
            save();
        }
    }

    /** Save current settings to file */
    public static void save() {
        json.toJson(settings, Gdx.files.local(FILE_NAME));
    }

    /** Fill settings with reasonable defaults */
    private static void setDefaults() {
        settings.masterVolume = 1.0f;
        settings.musicVolume = 1.0f;
        settings.sfxVolume = 1.0f;
        settings.language = "en";
        settings.resolutionWidth = 1920;
        settings.resolutionHeight = 1080;
        settings.fullscreen = false;
        settings.backgrounDim = 0.6f;
        settings.noteSpeed = 1.0f;
        // Add defaults for keybinds if you have any
        settings.keybinds = new String[]{"D", "F", "J", "K"};
    }
}
