package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.PlaylistGeneratorTest;
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
 * Unit tests for {@link PlaylistFileWriter}.
 */
public class PlaylistWriterTest {

    private File playlistFolder;
    private PlaylistFileWriter objUnderTest;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        playlistFolder = testFolder.newFolder("PlaylistFolder");
        final String playlistFilePath = playlistFolder.getAbsolutePath() + File.separator + "some_playlist.m3u";
        objUnderTest = new PlaylistFileWriter(playlistFilePath);
    }

    @Test
    public void writePlaylistToFile_withSongList_createsPlaylistFile() {
        final String song1Path = "path\\to\\song1.mp3";
        final Song song1 = new Song();
        song1.setFilePath(song1Path);

        final String song2Path = "some\\magical\\song2.mp3";
        final Song song2 = new Song();
        song2.setFilePath(song2Path);

        final List<Song> songList = new ArrayList<>();
        songList.add(song1);
        songList.add(song2);

        objUnderTest.writePlaylistToFile(songList);

        final File[] playlistFolderContents = playlistFolder.listFiles();
        assertNotNull("Playlist folder should not be null", playlistFolderContents);
        assertTrue("Playlist folder '" + playlistFolder.getAbsolutePath() + "' should not be empty",
                playlistFolderContents.length > 0);

        final String fileContents = readFileContents(playlistFolderContents[0]);
        assertTrue("Playlist folder should contain song 1", fileContents.contains(song1Path));
        assertTrue("Playlist folder should contain song 2", fileContents.contains(song2Path));
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
