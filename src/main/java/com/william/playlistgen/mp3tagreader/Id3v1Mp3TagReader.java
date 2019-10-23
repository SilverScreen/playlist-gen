package com.william.playlistgen.mp3tagreader;

import com.mpatric.mp3agic.ID3v1;

/**
 * @author William
 */
public class Id3v1Mp3TagReader implements Mp3TagReader {

    private ID3v1 id3v1Tag;

    public Id3v1Mp3TagReader(final ID3v1 id3v1Tag) {
        this.id3v1Tag = id3v1Tag;
    }

    @Override
    public String getArtist() {
        return id3v1Tag.getArtist();
    }

    @Override
    public String getAlbum() {
        return id3v1Tag.getAlbum();
    }

    @Override
    public String getSongTitle() {
        return id3v1Tag.getTitle();
    }

    @Override
    public String getYear() {
        return id3v1Tag.getYear();
    }

    @Override
    public String getGenre() {
        return id3v1Tag.getGenreDescription();
    }
}
