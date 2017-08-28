/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.play;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

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
    private static final String FILE_SEPARATOR = File.separator;
    private static final String FILE_PATH = "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources"
            + FILE_SEPARATOR + "testMp3Folder" + FILE_SEPARATOR + "03 All I Need.mp3";
    private static final String FILE_PATH_NOT_FOUND = "C:\\usghhsuhuu4782";

    private Mp3TagFinder mp3TagFinder;
    private Mp3TagFinder mp3TagFinderFileNotFound;
    private File mp3File;
    private File mp3FileWhichDoesNotExist;

    @Before
    public void setUp() {
        mp3File = new File(FILE_PATH);
        mp3TagFinder = new Mp3TagFinder(mp3File);
        mp3FileWhichDoesNotExist = new File(FILE_PATH_NOT_FOUND);
        mp3TagFinderFileNotFound = new Mp3TagFinder(mp3FileWhichDoesNotExist);
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
