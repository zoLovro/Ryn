package io.github.lovrohk.screensHelpful;

import java.util.List;

public class Song {
    protected String title;
    protected String artist;
    protected int bpm;
    protected String audioFile;
    protected String backgroundFile;
    protected String noteFile;

    public Song(String title, String artist, int bpm, String audioFile, String backgroundFile, String noteFile) {
        this.title = title;
        this.artist = artist;
        this.bpm = bpm;
        this.audioFile = audioFile;
        this.backgroundFile = backgroundFile;
        this.noteFile = noteFile;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getBpm() { return bpm; }
    public String getAudioFile() { return audioFile; }
    public String getBackgroundFile() { return backgroundFile; }
    public String getNoteFile() { return noteFile; }
}
