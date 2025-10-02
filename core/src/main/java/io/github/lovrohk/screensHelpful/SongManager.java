package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Json;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SongManager {
    protected ArrayList<Song> songs = new ArrayList<>();
    private Song selectedSong;

    public SongManager() {}

    public void fillSongs() {
        Json json = new Json();
        FileHandle indexFile = Gdx.files.internal("maps/index.json");

        MapIndex index = json.fromJson(MapIndex.class, indexFile);
        for (String mapName : index.maps) {
            FileHandle folder = Gdx.files.internal("maps/" + mapName);
            FileHandle jsonFile = folder.child("songInfo.json");
            if (jsonFile.exists()) {
                try {
                    SongData data = json.fromJson(SongData.class, jsonFile);
                    Song song = new Song(
                        data.title,
                        data.artist,
                        data.bpm,
                        data.audio,
                        data.background,
                        data.noteFile
                    );
                    songs.add(song);
                    System.out.println("Loaded: " + song.getTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static class MapIndex {
        public ArrayList<String> maps;
    }

    public void drawSongTile(int screenHeight, Batch batch) {
        System.out.println(songs.size());
        int x = 0;
        int y = screenHeight/2;
        int offset = 0;
        int counter = 1;
        batch.begin();

            for(Song song : songs) {
                SongTile songTile = new SongTile(x, y, song);
                batch.draw(songTile.getSongTileImage(), x, y+offset, songTile.getSongTileImageWidth(), songTile.getSongTileIMageHeight());
                if(counter%2 == 0) {
                    offset = (offset+(Math.abs(offset*2)))+ songTile.getSongTileIMageHeight()+10;
                }else{
                    offset = (offset-(offset*2))-10;
                }
                counter++;
            }

        batch.end();
    }

    public void selectSong(int index) {
        if(index >= 0 && index < songs.size()) {
            selectedSong = songs.get(index);
        }
    }

    public ArrayList<Song> getSongs() { return songs; }
    public Song getSelectedSong() { return selectedSong; }
}
