/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author William
 */
public class PlaylistGeneratorTestBase {

    File playlistFolder;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    String getMusicLibraryPath() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL url = classLoader.getResource("testMp3Folder");
        return url != null ? url.getPath() : "";
    }

    void assertPlaylistFileCreated(final String expectedFile) {
        final File[] playlistFolderContents = playlistFolder.listFiles();
        assertNotNull("Playlist folder should not be null", playlistFolderContents);
        assertTrue("Playlist folder '" + playlistFolder.getAbsolutePath() + "' should not be empty",
                playlistFolderContents.length > 0);
        assertEquals("File name should be " + expectedFile, expectedFile, playlistFolderContents[0].getName());
    }

    void assertNoPlaylistFileCreated() {
        final File[] playlistFolderContents = playlistFolder.listFiles();
        assertNotNull("Playlist folder should not be null", playlistFolderContents);
        assertEquals("Playlist folder should be empty", 0, playlistFolderContents.length);
    }
}
