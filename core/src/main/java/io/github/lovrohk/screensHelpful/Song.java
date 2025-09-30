package io.github.lovrohk.screensHelpful;

import java.util.List;

public class Song {
    private String title;
    private String artist;
    private int bpm;
    private String audioFile;
    private String backgroundFile;

    public Song(String title, String artist, int bpm, String audioFile, String backgroundFile) {
        this.title = title;
        this.artist = artist;
        this.bpm = bpm;
        this.audioFile = audioFile;
        this.backgroundFile = backgroundFile;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getBpm() { return bpm; }
    public String getAudioFile() { return audioFile; }
    public String getBackgroundFile() { return backgroundFile; }
}
