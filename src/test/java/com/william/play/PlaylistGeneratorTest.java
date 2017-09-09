/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.play;

import com.william.dev.common.utils.Song;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private PlaylistGenerator playlistGenerator;
    private File playlistFolder;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        playlistFolder = testFolder.newFolder("PlaylistFolder");
        playlistGenerator = new PlaylistGenerator();
    }

    @After
    public void tearDown() {
        playlistGenerator = null;
    }

    @Test
    public void getTopTracks_returnsSongList() {
        assertTrue(playlistGenerator.loadLibrary(MUSIC_DIRECTORY));
        List<Song> topTracks = playlistGenerator.getTopTracks();
        assertTrue("Song list should not be empty", !topTracks.isEmpty());
    }

    @Test
    public void getLastFmLovedTracks_returnsSongList() {
        assertTrue(playlistGenerator.loadLibrary(MUSIC_DIRECTORY));
        List<Song> lastFmLovedTracks = playlistGenerator.getLastFmLovedTracks();
        assertTrue("Song list should not be empty", !lastFmLovedTracks.isEmpty());
    }

    @Test
    public void getLastFmLovedTracks_withEmptyMusicLibrary_returnsEmptySongList() {
        assertTrue(playlistGenerator.loadLibrary(NON_EXISTENT_MUSIC_DIRECTORY));
        List<Song> lastFmLovedTracks = playlistGenerator.getLastFmLovedTracks();
        assertTrue("Song list should be empty", lastFmLovedTracks.isEmpty());
    }

    @Test
    public void writePlaylistToFile_withSongList_createsPlaylistFile() {
        final String SONG_1_PATH = "path\\to\\song1.mp3";
        final Song song1 = new Song();
        song1.setFilePath(SONG_1_PATH);

        final String SONG_2_PATH = "some\\magical\\song2.mp3";
        final Song song2 = new Song();
        song2.setFilePath(SONG_2_PATH);

        final List<Song> songList = new ArrayList<>();
        songList.add(song1);
        songList.add(song2);

        final String playlistFilePath = playlistFolder.getAbsolutePath() + File.separator + "some_playlist.m3u";
        final PlaylistFileWriter playlistFileWriter = new PlaylistFileWriter(playlistFilePath);
        playlistFileWriter.writePlaylistToFile(songList);

        final File[] playlistFolderContents = playlistFolder.listFiles();
        assertNotNull("Playlist folder should not be null", playlistFolderContents);
        assertTrue("Playlist folder '" + playlistFolder.getAbsolutePath() + "' should not be empty",
                playlistFolderContents.length > 0);

        final String fileContents = readFileContents(playlistFolderContents[0]);
        assertTrue("Playlist folder should contain song 1", fileContents.contains(SONG_1_PATH));
        assertTrue("Playlist folder should contain song 2", fileContents.contains(SONG_2_PATH));
    }

    private String readFileContents(final File playlistFile) {
        final StringBuilder fileContents = new StringBuilder();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(playlistFile))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                fileContents.append(line);
            }
        } catch (final IOException ex) {
            Logger.getLogger(PlaylistGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileContents.toString();
    }
}
