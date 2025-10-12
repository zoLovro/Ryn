package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import io.github.lovrohk.Main;

import java.util.ArrayList;
import java.util.List;

public class SettingsManager {
    protected Rectangle volumeBar;

    protected int volume;
    protected int audioOffset;
    protected float noteSpeed;
    protected String[] keybinds;

    public SettingsManager(int volume, int audioOffset, float noteSpeed, String[] keybinds) {
        this.volume = volume;
        this.audioOffset = audioOffset;
        this.noteSpeed = noteSpeed;
        this.keybinds = keybinds;
    }



    // getters/setters
    public void setKeybinds(String[] a) {keybinds = a;}
    public String[] getKeybinds() {return keybinds;}

    public void setAudioOffset(int a) {audioOffset = a;}
    public int getAudioOffset() {return audioOffset;}

    public void setNoteSpeed(float a) {noteSpeed = a;}
    public float getNoteSpeed() {return noteSpeed;}

    public void setVolume(int a) {volume = a;}
    public int getVolume() {return volume;}
}
