package com.gocreative.tm.mahrem_ene.Models;

public class Music {
    private String musicTitle, musicArtist;

    public Music(String musicTitle, String musicArtist) {
        this.musicTitle = musicTitle;
        this.musicArtist = musicArtist;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public String getMusicArtist() {
        return musicArtist;
    }
}
