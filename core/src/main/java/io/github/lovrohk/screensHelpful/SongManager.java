package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;

public class SongManager {
    private ArrayList<Song> songs = new ArrayList<>();

    public SongManager() {}

    public void readJson() {
        FileHandle songsDir = Gdx.files.internal("songs/");
        for (FileHandle folder : songsDir.list()) {
            FileHandle jsonFile = folder.child("song.json");
            if (jsonFile.exists()) {
                Json json = new Json();
                SongData data = json.fromJson(SongData.class, jsonFile);

                Song song = new Song(
                    data.title,
                    data.artist,
                    data.bpm,
                    data.audio,
                    data.background
                );
                songs.add(song);
            }
        }
    }
}
