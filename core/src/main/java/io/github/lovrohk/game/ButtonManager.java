package io.github.lovrohk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class ButtonManager {
    public ButtonManager() {}

    public void drawContinueButton(int posX, int posY, SpriteBatch batch) {
        ContinueButton continueButton = new ContinueButton(posX, posY);
        batch.draw(continueButton.getTexture(), posX, posY);
    }
    public void drawRestartButton(int posX, int posY, SpriteBatch batch) {
        RestartButton restartButton = new RestartButton(posX, posY);
        batch.draw(restartButton.getTexture(), posX, posY);
    }
    public void drawExitButton(int posX, int posY, SpriteBatch batch) {
        ExitButton exitButton = new ExitButton(posX, posY);
        batch.draw(exitButton.getTexture(), posX, posY);
    }

    // getters
    ContinueButton tempContBut = new ContinueButton(0, 0);
    RestartButton tempRestartButton = new RestartButton(0, 0);
    ExitButton tempExitButton = new ExitButton(0, 0);
    public int getContinueTextureWidth() {return tempContBut.getTextureWidth();}
    public int getContinueTextureHeight() {return tempContBut.getTextureHeight();}
    public int getRestartTextureWidth() {return tempRestartButton.getTextureWidth();}
    public int getRestartTextureHeight() {return tempRestartButton.getTextureHeight();}
    public int getExitTextureWidth() {return tempExitButton.getTextureWidth();}
    public int getExitTextureHeight() {return tempExitButton.getTextureHeight();}

    // button classes
    private class ContinueButton {
        protected int posX;
        protected int posY;
        protected Texture continueButtonTexture = new Texture(Gdx.files.internal("skins/testSkin/continueButtonTexture.png"));

        public ContinueButton(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }

        // getters
        public int getPosX() {
            return posX;
        }
        public int getPosY() {
            return posY;
        }
        public int getTextureWidth() {
            return continueButtonTexture.getWidth();
        }
        public int getTextureHeight() {
            return continueButtonTexture.getHeight();
        }
        public Texture getTexture() {
            return continueButtonTexture;
        }
    }

    private class RestartButton {
        protected int posX;
        protected int posY;
        protected Texture restartButtonTexture = new Texture(Gdx.files.internal("skins/testSkin/restartButtonTexture.png"));

        public RestartButton(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }

        // getters
        public int getPosX() {
            return posX;
        }
        public int getPosY() {
            return posY;
        }
        public int getTextureWidth() {
            return restartButtonTexture.getWidth();
        }
        public int getTextureHeight() {
            return restartButtonTexture.getHeight();
        }
        public Texture getTexture() {
            return restartButtonTexture;
        }
    }

    private class ExitButton {
        protected int posX;
        protected int posY;
        protected Texture exitButtonTexture = new Texture(Gdx.files.internal("skins/testSkin/exitButtonTexture.png"));

        public ExitButton(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }

        // getters
        public int getPosX() {
            return posX;
        }
        public int getPosY() {
            return posY;
        }
        public int getTextureWidth() {
            return exitButtonTexture.getWidth();
        }
        public int getTextureHeight() {
            return exitButtonTexture.getHeight();
        }
        public Texture getTexture() {
            return exitButtonTexture;
        }
    }
}
