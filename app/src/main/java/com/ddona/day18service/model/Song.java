package com.ddona.day18service.model;

public class Song {
    private String name;
    private String path;
    private String duration;
    private String album;
    private String artist;

    public Song() {
    }

    public Song(String name, String path, String duration, String album, String artist) {
        this.name = name;
        this.path = path;
        this.duration = duration;
        this.album = album;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
