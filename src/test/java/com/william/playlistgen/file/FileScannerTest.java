/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author William
 */
public class FileScannerTest {

    private FileScanner fileScanner;
    private File musicDirectory;
    private static final String FILE_SEPARATOR = File.separator;
    private static final String MUSIC_DIRECTORY = "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources"
            + FILE_SEPARATOR + "testMp3Folder";
    private static final String MUSIC_DIRECTORY_NOT_FOUND = "E:\\Music";

    @Before
    public void setUp() {
        fileScanner = new FileScanner();
    }

    @Test
    public void getMp3SongListSuccessfully() {
        musicDirectory = new File(MUSIC_DIRECTORY);
        final List<Song> results = fileScanner.getMp3SongList(musicDirectory);
        assertTrue("Number of songs returned should be 7", results.size() == 7);
    }

    @Test
    public void getMp3SongListWrongDirectory() {
        musicDirectory = new File(MUSIC_DIRECTORY_NOT_FOUND);
        final List<Song> results = fileScanner.getMp3SongList(musicDirectory);
        assertTrue("Songs returned should be empty", results.isEmpty());
    }
}
