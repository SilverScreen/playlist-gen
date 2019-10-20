package com.william.playlistgen.testutils;

import com.william.dev.common.utils.Song;

public class SongBuilder {
    private int trackNumber;
    private String artist;
    private String album;
    private String title;
    private String year;
    private String genre;
    private String filePath;

    public Song build() {
        return new Song(trackNumber, artist, album, title, year, genre, filePath, null);
    }

    public SongBuilder setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
        return this;
    }

    public SongBuilder setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public SongBuilder setAlbum(String album) {
        this.album = album;
        return this;
    }

    public SongBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public SongBuilder setYear(String year) {
        this.year = year;
        return this;
    }

    public SongBuilder setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public SongBuilder setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
}
