package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import java.util.ArrayList;

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
                        data.audioTrimmed,
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

    public void selectSong(int index) {
        if(index >= 0 && index < songs.size()) {
            selectedSong = songs.get(index);
        }
    }

    public ArrayList<Song> getSongs() { return songs; }
    public Song getSelectedSong() { return selectedSong; }
}
