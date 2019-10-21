/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.mp3tagreader;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author William
 */
public class Mp3TagReaderTest {

    private static final String ARTIST = "Air";
    private static final String SONG_TITLE = "All I Need";
    private static final String ALBUM_NAME = "Moon Safari";
    private static final String GENRE = "Electronic";
    private static final String MP3_FILE_NAME = "testMp3Folder/Air/03 All I Need.mp3";
    private static final String FILE_PATH_NOT_FOUND = "usghhsuhuu4782";
    private static final String CORRUPT_FILE_PATH = "corruptMp3Folder/13 Corrupt File.mp3";

    private Mp3TagReader objUnderTest;

    @Before
    public void setUp() {
        objUnderTest = Mp3TagReaderFactory.getTagReader(Objects.requireNonNull(getMp3File(MP3_FILE_NAME)));
        assertNotNull(objUnderTest);
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
    public void getArtist() {
        assertEquals(ARTIST, objUnderTest.getArtist());
    }

    @Test
    public void getSongTitle() {
        assertEquals(SONG_TITLE, objUnderTest.getSongTitle());
    }

    @Test
    public void getAlbum() {
        assertEquals(ALBUM_NAME, objUnderTest.getAlbum());
    }

    @Test
    public void getGenre() {
        assertEquals(GENRE, objUnderTest.getGenre());
    }

    @Test
    public void testFileNotFound() {
        objUnderTest = Mp3TagReaderFactory.getTagReader(new File(FILE_PATH_NOT_FOUND));
        assertNull(objUnderTest);
    }

    @Test
    public void testCorruptFile() {
        objUnderTest = Mp3TagReaderFactory.getTagReader(Objects.requireNonNull(getMp3File(CORRUPT_FILE_PATH)));
        assertNull(objUnderTest);
    }
}
