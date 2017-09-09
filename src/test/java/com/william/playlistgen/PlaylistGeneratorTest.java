/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author William
 */
public class PlaylistGeneratorTest {

    private static final String FILE_SEPARATOR = File.separator;
    private static final String MUSIC_DIRECTORY = "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources"
            + FILE_SEPARATOR + "testMp3Folder";
    private static final String NON_EXISTENT_MUSIC_DIRECTORY = "C:\\non\\existent\\directory";
    private File playlistFolder;
    private PlaylistGenerator objUnderTest;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        playlistFolder = testFolder.newFolder("PlaylistFolder");
        objUnderTest = new PlaylistGenerator(playlistFolder.getAbsolutePath());
    }

    @After
    public void tearDown() {
        objUnderTest = null;
    }

    @Test
    public void generateTopTracksPlaylist_successfullyGeneratesFile() {
        assertTrue(objUnderTest.loadLibrary(MUSIC_DIRECTORY));
        objUnderTest.generateTopTracksPlaylist();
        assertPlaylistFileCreated("lastfmtoptracks.m3u");
    }

    @Test
    public void generateTopTracksPlaylist_withEmptyMusicLibrary_doesNotGenerateFile() {
        assertTrue(objUnderTest.loadLibrary(NON_EXISTENT_MUSIC_DIRECTORY));
        objUnderTest.generateTopTracksPlaylist();
        assertPlaylistFileNotCreated();
    }

    @Test
    public void generateLovedTracksPlaylist_successfullyGeneratesFile() {
        assertTrue(objUnderTest.loadLibrary(MUSIC_DIRECTORY));
        objUnderTest.generateLovedTracksPlaylist();
        assertPlaylistFileCreated("lastfmloved.m3u");
    }

    @Test
    public void generateLovedTracksPlaylist_withEmptyMusicLibrary_doesNotGenerateFile() {
        assertTrue(objUnderTest.loadLibrary(NON_EXISTENT_MUSIC_DIRECTORY));
        objUnderTest.generateLovedTracksPlaylist();
        assertPlaylistFileNotCreated();
    }

    private void assertPlaylistFileCreated(final String expectedFile) {
        final File[] playlistFolderContents = playlistFolder.listFiles();
        assertNotNull("Playlist folder should not be null", playlistFolderContents);
        assertTrue("Playlist folder '" + playlistFolder.getAbsolutePath() + "' should not be empty",
                playlistFolderContents.length > 0);
        assertEquals("File name should be " + expectedFile, expectedFile, playlistFolderContents[0].getName());
    }

    private void assertPlaylistFileNotCreated() {
        final File[] playlistFolderContents = playlistFolder.listFiles();
        assertNotNull("Playlist folder should not be null", playlistFolderContents);
        assertTrue("Playlist folder '" + playlistFolder.getAbsolutePath() + "' should be empty",
                playlistFolderContents.length == 0);
    }
}
