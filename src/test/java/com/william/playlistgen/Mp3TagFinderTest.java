/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author William
 */
public class Mp3TagFinderTest {

    private static final String ARTIST = "Air";
    private static final String ARTIST_NOT_FOUND = "The Doors";
    private static final String SONG_TITLE = "All I Need";
    private static final String ALBUM_NAME = "Moon Safari";
    private static final String MP3_FILE_NAME = "testMp3Folder" + File.separator + "03 All I Need.mp3";
    private static final String FILE_PATH_NOT_FOUND = "usghhsuhuu4782";

    private Mp3TagFinder mp3TagFinder;
    private Mp3TagFinder mp3TagFinderFileNotFound;
    private File mp3File;
    private File mp3FileWhichDoesNotExist;

    @Before
    public void setUp() {
        mp3File = getMp3File();
        mp3TagFinder = new Mp3TagFinder(mp3File);
        mp3FileWhichDoesNotExist = new File(FILE_PATH_NOT_FOUND);
        mp3TagFinderFileNotFound = new Mp3TagFinder(mp3FileWhichDoesNotExist);
    }

    private File getMp3File() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL fileUrl = classLoader.getResource(MP3_FILE_NAME);
        return fileUrl == null ? null : new File(fileUrl.getPath());
    }

    @After
    public void tearDown() {
        mp3File = null;
        mp3TagFinder = null;
        mp3FileWhichDoesNotExist = null;
        mp3TagFinderFileNotFound = null;
    }

    @Test
    public void testIsArtistTrue() {
        assertTrue(mp3TagFinder.isArtist(ARTIST));
    }

    @Test
    public void testIsArtistFalse() {
        assertFalse(mp3TagFinder.isArtist(ARTIST_NOT_FOUND));
    }

    @Test
    public void testFilePathNotFound() {
        assertFalse(mp3TagFinderFileNotFound.isArtist(ARTIST));
    }

    @Test
    public void testGetArtist() {
        assertEquals(ARTIST, mp3TagFinder.getArtist());
    }

    @Test
    public void testGetArtistNotFound() {
        assertEquals("", mp3TagFinderFileNotFound.getArtist());
    }

    @Test
    public void testGetSongTitle() {
        assertEquals(SONG_TITLE, mp3TagFinder.getSongTitle());
    }

    @Test
    public void testGetSongTitleNotFound() {
        assertEquals("", mp3TagFinderFileNotFound.getSongTitle());
    }

    @Test
    public void testGetAlbum() {
        assertEquals(ALBUM_NAME, mp3TagFinder.getAlbum());
    }
}
