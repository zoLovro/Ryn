package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SongTile {
    protected int x;
    protected int y;
    protected Texture songTileImage = new Texture(Gdx.files.internal("screenTextures/songTile.png"));
    protected int songTileImageWidth = 640;
    protected int songTileIMageHeight = 150;
    protected Song song;

    public SongTile(int x, int y, Song song) {
        this.x = x;
        this.y = y;
        this.song = song;
    }

    public int getSongTileImageWidth() {return songTileImageWidth;}
    public int getSongTileIMageHeight() {return songTileIMageHeight;}
    public Texture getSongTileImage() {return songTileImage;}
    public int getX() {return x;}
    public int getY() {return y;}
}
