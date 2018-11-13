/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.mp3tagreader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author William
 */
public class Mp3TagReaderTest {

    private static final String ARTIST = "Air";
    private static final String SONG_TITLE = "All I Need";
    private static final String ALBUM_NAME = "Moon Safari";
    private static final String MP3_FILE_NAME = "testMp3Folder/03 All I Need.mp3";
    private static final String FILE_PATH_NOT_FOUND = "usghhsuhuu4782";

    private Mp3TagReader objUnderTest;

    @Before
    public void setUp() {
        objUnderTest = Mp3TagReaderFactory.getTagReader(getMp3File(MP3_FILE_NAME));
    }

    private File getMp3File(final String fileName) {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL fileUrl = classLoader.getResource(fileName);
        return fileUrl == null ? null : new File(fileUrl.getPath());
    }

    @After
    public void tearDown() {
        objUnderTest = null;
    }

    @Test
    public void testGetArtist() {
        assertEquals(ARTIST, objUnderTest.getArtist());
    }

    @Test
    public void testGetSongTitle() {
        assertEquals(SONG_TITLE, objUnderTest.getSongTitle());
    }

    @Test
    public void testGetAlbum() {
        assertEquals(ALBUM_NAME, objUnderTest.getAlbum());
    }

    @Test
    public void testFileNotFound() {
        objUnderTest = Mp3TagReaderFactory.getTagReader(new File(FILE_PATH_NOT_FOUND));
        assertNull(objUnderTest);
    }
}
