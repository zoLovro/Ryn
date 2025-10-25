package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class KeybindsInputting extends InputAdapter{
    private int currentIndex = -1; // -1 means not listening
    private final int totalKeys = 4;
    private int[] keybinds = new int[]{Input.Keys.D, Input.Keys.F, Input.Keys.J, Input.Keys.K};

    public void startListening() {
        currentIndex = 0; // begin from first key
        System.out.println("Press a key for Lane " + (currentIndex + 1));
    }

    public boolean isFinished() {
        return currentIndex == -1;
    }

    public int[] getKeybinds() {
        return keybinds.clone();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (currentIndex == -1) return false;

        keybinds[currentIndex] = keycode;
        System.out.println("Set lane " + (currentIndex + 1) + " to key: " + Input.Keys.toString(keycode));

        currentIndex++;
        if (currentIndex >= totalKeys) {
            currentIndex = -1;
            System.out.println("All keys set!");
        } else {
            System.out.println("Press a key for Lane " + (currentIndex + 1));
        }
        return true;
    }
}
