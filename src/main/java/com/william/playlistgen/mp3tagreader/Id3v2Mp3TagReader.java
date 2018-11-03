package com.william.playlistgen.mp3tagreader;

import com.mpatric.mp3agic.ID3v2;

/**
 * @author William
 */
public class Id3v2Mp3TagReader implements Mp3TagReader {

    private ID3v2 id3v2Tag;

    public Id3v2Mp3TagReader(final ID3v2 id3v2Tag) {
        this.id3v2Tag = id3v2Tag;
    }

    @Override
    public String getArtist() {
        return id3v2Tag.getArtist();
    }

    @Override
    public String getAlbum() {
        return id3v2Tag.getAlbum();
    }

    @Override
    public String getSongTitle() {
        return id3v2Tag.getTitle();
    }
}
