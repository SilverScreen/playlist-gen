package com.william.playlistgen;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.database.LibraryDao;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author William
 */
public class PlaylistGeneratorInvalidDirectoryTest {

    private static final String NON_EXISTENT_MUSIC_DIRECTORY = "non_existent_directory";

    private LibraryDao libraryDao = LibraryDao.getInstance();
    private PlaylistGenerator objUnderTest;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setup() {
        libraryDao.dropTable();
        final File playlistFolder = testFolder.newFolder("PlaylistFolder");
        objUnderTest = new PlaylistGenerator(playlistFolder.getAbsolutePath());
    }

    @Test
    public void loadLibraryWithInvalidDirectory() {
        objUnderTest.loadLibrary(NON_EXISTENT_MUSIC_DIRECTORY);
        final List<Song> allSongsEntered = libraryDao.retrieveAllSongs();
        assertTrue("Songs entered in DB should be None.", allSongsEntered.isEmpty());
    }
}
