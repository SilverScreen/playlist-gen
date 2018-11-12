package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.database.LibraryDao;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author William
 */
public class MusicLibraryScannerTest {

    private LibraryDao libraryDao = LibraryDao.getInstance();
    private MusicLibraryScanner objUnderTest;

    @Before
    public void setup() {
        libraryDao.dropTable();
    }

    private String getMusicLibraryPath() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL url = classLoader.getResource("testMp3Folder");
        return url != null ? url.getPath() : "";
    }

    @Test
    public void processMusicFolderSuccessfully() {
        final String musicLibraryPath = getMusicLibraryPath();
        objUnderTest = new MusicLibraryScanner(new File(musicLibraryPath));
        objUnderTest.start();
        final List<Song> allSongsEntered = libraryDao.retrieveAllSongs();
        assertEquals("The number of songs entered should be 7", 7, allSongsEntered.size());
    }

    @Test
    public void processNonExistentMusicFolder() {
        objUnderTest = new MusicLibraryScanner(new File("non_existent_path"));
        objUnderTest.start();
        final List<Song> allSongsEntered = libraryDao.retrieveAllSongs();
        assertEquals("The number of songs entered should be 0", 0, allSongsEntered.size());
    }
}
