/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William
 */
public class Mp3TagFinder {

    private static final String WHITESPACE = "%20";
    private Mp3File mp3File;
    private ID3v2 id3v2Tag;
    private ID3v1 id3v1Tag;

    public Mp3TagFinder(final File file) {
        try {
            mp3File = new Mp3File(file.getAbsolutePath().replaceAll(WHITESPACE, " "));
            id3v2Tag = mp3File.getId3v2Tag();
            id3v1Tag = mp3File.getId3v1Tag();
        } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
            Logger.getLogger(Mp3TagFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isArtist(final String artistNameToCheck) {
        String artistNameFromMp3 = getArtist();

        if (artistNameFromMp3 != null) {
            return artistNameFromMp3.equalsIgnoreCase(artistNameToCheck);
        }
        return false;
    }

    public String getArtist() {
        String artistNameFromMp3 = "";

        if (mp3File == null) {
            return artistNameFromMp3;
        }
        if (mp3File.hasId3v2Tag()) {
            artistNameFromMp3 = id3v2Tag.getArtist();
        } else if (mp3File.hasId3v1Tag()) {
            artistNameFromMp3 = id3v1Tag.getArtist();
        }
        return artistNameFromMp3;
    }

    public String getSongTitle() {
        String songTitleFromMp3 = "";

        if (mp3File == null) {
            return songTitleFromMp3;
        }
        if (mp3File.hasId3v2Tag()) {
            songTitleFromMp3 = id3v2Tag.getTitle();
        } else if (mp3File.hasId3v1Tag()) {
            songTitleFromMp3 = id3v1Tag.getTitle();
        }
        return songTitleFromMp3;
    }

    public String getAlbum() {
        String albumNameFromMp3 = "";

        if (mp3File == null) {
            return albumNameFromMp3;
        }
        if (mp3File.hasId3v2Tag()) {
            albumNameFromMp3 = id3v2Tag.getAlbum();
        } else if (mp3File.hasId3v1Tag()) {
            albumNameFromMp3 = id3v1Tag.getAlbum();
        }
        return albumNameFromMp3;
    }
}