package com.example.notificationapp;

public class Track {
    private String title;
    private String artist;
    private int image;
    private int urlMusic;

    public Track(String title, String artist, int image, int urlMusic) {
        this.title = title;
        this.artist = artist;
        this.image = image;
        this.urlMusic = urlMusic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getUrlMusic() {
        return urlMusic;
    }

    public void setUrlMusic(int urlMusic) {
        this.urlMusic = urlMusic;
    }
}
