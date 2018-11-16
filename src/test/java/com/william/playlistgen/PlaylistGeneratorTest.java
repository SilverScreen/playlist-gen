/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen;

import com.william.playlistgen.database.LibraryDao;
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

    private File playlistFolder;
    private PlaylistGenerator objUnderTest;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setup() throws IOException {
        playlistFolder = testFolder.newFolder("PlaylistFolder");
        objUnderTest = new PlaylistGenerator(playlistFolder.getAbsolutePath());
        objUnderTest.loadLibrary(getMusicLibraryPath());
    }

    private String getMusicLibraryPath() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL url = classLoader.getResource("testMp3Folder");
        return url != null ? url.getPath() : "";
    }

    @After
    public void tearDown() {
        LibraryDao.getInstance().dropTable();
    }

    @Test
    public void generateTopTracksPlaylist_successfullyGeneratesFile() {
        objUnderTest.generateTopTracksPlaylist();
        assertPlaylistFileCreated("lastfmtoptracks.m3u");
    }

    @Test
    public void generateMyLovedTracksPlaylist_successfullyGeneratesFile() {
        objUnderTest.generateMyLovedTracksPlaylist();
        assertPlaylistFileCreated("lastfmloved.m3u");
    }

    @Test
    public void generateFriendsLovedTracksPlaylist_successfullyGeneratesFile() {
        objUnderTest.generateFriendsLovedTracksPlaylist();
        assertPlaylistFileCreated("friendslastfmloved.m3u");
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
