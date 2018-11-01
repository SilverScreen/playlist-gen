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
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author William
 */
public class PlaylistGeneratorTest {

    private static final String NON_EXISTENT_MUSIC_DIRECTORY = "non_existent_directory";
    private String musicLibraryPath;
    private File playlistFolder;
    private PlaylistGenerator objUnderTest;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        musicLibraryPath = getMusicLibraryPath();
        playlistFolder = testFolder.newFolder("PlaylistFolder");
        objUnderTest = new PlaylistGenerator(playlistFolder.getAbsolutePath());
    }

    private String getMusicLibraryPath() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL url = classLoader.getResource("testMp3Folder");
        return url != null ? url.getPath() : "";
    }

    @After
    public void tearDown() {
        objUnderTest = null;
    }

    @Test
    public void generateTopTracksPlaylist_successfullyGeneratesFile() {
        assertTrue(objUnderTest.loadLibrary(musicLibraryPath));
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
    public void generateMyLovedTracksPlaylist_successfullyGeneratesFile() {
        assertTrue(objUnderTest.loadLibrary(musicLibraryPath));
        objUnderTest.generateMyLovedTracksPlaylist();
        assertPlaylistFileCreated("lastfmloved.m3u");
    }

    @Test
    public void generateMyLovedTracksPlaylist_withEmptyMusicLibrary_doesNotGenerateFile() {
        assertTrue(objUnderTest.loadLibrary(NON_EXISTENT_MUSIC_DIRECTORY));
        objUnderTest.generateMyLovedTracksPlaylist();
        assertPlaylistFileNotCreated();
    }

    @Test
    public void generateFriendsLovedTracksPlaylist_successfullyGeneratesFile() {
        assertTrue(objUnderTest.loadLibrary(musicLibraryPath));
        objUnderTest.generateFriendsLovedTracksPlaylist();
        assertPlaylistFileCreated("friendslastfmloved.m3u");
    }

    @Test
    public void generateFriendsLovedTracksPlaylist_withEmptyMusicLibrary_doesNotGenerateFile() {
        assertTrue(objUnderTest.loadLibrary(NON_EXISTENT_MUSIC_DIRECTORY));
        objUnderTest.generateFriendsLovedTracksPlaylist();
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
