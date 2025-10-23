package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SongTile extends Actor {
    protected BitmapFont font;
    protected Texture songTileImage = new Texture(Gdx.files.internal("skins/testSkin/songTile.png"));;
    protected int songTileImageWidth = 640;
    protected int songTileIMageHeight = 150;
    protected int paddingX = 10;
    protected int paddingY = 25;
    protected Song song;

    // font

    public SongTile(Song song, BitmapFont font) {
        this.song = song;
        this.font = font;
        setSize(songTileImageWidth, songTileIMageHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Apply the parent's alpha to the font color
        font.setColor(1, 1, 1, parentAlpha);

        // Draw the background
        batch.draw(songTileImage, getX(), getY(),
            getWidth() * getScaleX(), getHeight() * getScaleY());

        // Draw the text
        font.draw(batch, song.getTitle(), getX() + paddingX, getY() + songTileIMageHeight - 10);
        font.draw(batch, song.getArtist(), getX() + paddingX, getY() + songTileIMageHeight - 2 * paddingY);
    }

    @Override
    public float getWidth() { return songTileImageWidth; }

    @Override
    public float getHeight() { return songTileIMageHeight; }

    public Texture getSongTileImage() {return songTileImage;}

    public Song getSong() {
        return song;
    }
}
