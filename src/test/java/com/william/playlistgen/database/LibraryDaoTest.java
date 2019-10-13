/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.database;

import com.william.dev.common.utils.Song;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author William
 */
public class LibraryDaoTest {

    private static final String FILE_SEPARATOR = File.separator;
    private static final String TEST_FOLDER = "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources"
            + FILE_SEPARATOR + "testMp3Folder" + FILE_SEPARATOR;
    private LibraryDao libraryDao = null;
    private Song song1;
    private Song song2;
    private Song song3;
    private Song song4;

    @Before
    public void setup() {
        libraryDao = LibraryDao.getInstance();
        song1 = createSong("Fade Into You", "Mazzy Star", "So Tonight That I Might See",
                TEST_FOLDER + "01 Fade Into You");
        song2 = createSong("Sexy Boy", "Air", "Moon Safari", TEST_FOLDER + "02 Sexy Boy.mp3");
        song3 = createSong("New Noise", "Refused", "The Shape of Punk to Come", TEST_FOLDER + "06 New Noise.mp3");
        song4 = new Song(1, "'Test'", "Blah's", "\"test\"", "2017", "D:\\Music\\test\\\'test\'\\09. test.mp3", null);

        assertTrue(libraryDao.createTable());
        List<Song> songs = new ArrayList<>();
        songs.add(song1);
        songs.add(song2);
        songs.add(song3);
        songs.add(song4);
        assertTrue(libraryDao.insert(songs));
    }

    private Song createSong(final String title, final String artist, final String album, final String filePath) {
        final Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setFilePath(filePath);
        return song;
    }

    @After
    public void teardown() {
        libraryDao.dropTable();
        libraryDao = null;
    }

    @Test
    public void testInsertNullDataIntoDb() {
        final Song invalidSong = new Song(0, null, null, null, null, null, null);
        final List<Song> invalidSongs = new ArrayList<>();
        invalidSongs.add(invalidSong);
        assertFalse(libraryDao.insert(invalidSongs));
    }

    @Test
    public void testDataRetrieveByArtist() {
        final List<Song> songList = libraryDao.retrieveByArtist("Air");
        assertFalse("Song list should not be empty", songList.isEmpty());
        final String resultFilePath = songList.get(0).getFilePath();
        assertEquals("File paths should be the same", song2.getFilePath(), resultFilePath);
    }

    @Test
    public void testDataRetrieveByArtistWhoDoesNotExist() {
        final List<Song> songList = libraryDao.retrieveByArtist("blahblah");
        assertTrue("Song list should be empty", songList.isEmpty());
    }

    @Test
    public void testDataRetrieveByAlbum() {
        final List<Song> songList = libraryDao.retrieveByAlbum("air", "Moon Safari");
        assertFalse("Song list should not be empty", songList.isEmpty());
        final String resultFilePath = songList.get(0).getFilePath();
        assertEquals("File paths should be the same", song2.getFilePath(), resultFilePath);
    }

    @Test
    public void testDataRetrieveByAlbumWhereArtistExistsButAlbumDoesNotExist() {
        final List<Song> songList = libraryDao.retrieveByAlbum("Mazzy Star", "blahblahblah");
        assertTrue("Song list should be empty", songList.isEmpty());
    }

    @Test
    public void testDataRetrieveByAlbumWhereArtistDoesNotExistButAlbumDoesExist() {
        final List<Song> songList = libraryDao.retrieveByAlbum("BlahBlahBlah", "Moon Safari");
        assertTrue("Song list should be empty", songList.isEmpty());
    }

    @Test
    public void testDataRetrieveBySong() {
        final Song returnedSong = libraryDao.retrieveBySong("Refused", "New Noise");
        assertEquals("File paths should be the same", song3.getFilePath(), returnedSong.getFilePath());
    }

    @Test
    public void testDataRetrieveBySongWhereArtistExistsButSongDoesNotExist() {
        final Song returnedSong = libraryDao.retrieveBySong("Mazzy Star", "blahblahblah");
        assertNull("Returned song should be null", returnedSong);
    }

    @Test
    public void testDataRetrieveBySongWhereArtistDoesNotExistButSongDoesExist() {
        final Song returnedSong = libraryDao.retrieveBySong("BlahBlahBlah", "Slunk");
        assertNull("Returned song should be null", returnedSong);
    }

    @Test
    public void retrieveAllSongs_getsSongsSuccessfully() {
        final List<Song> songList = libraryDao.retrieveAllSongs();
        assertEquals("Number of songs retrieve should be 4", 4, songList.size());
    }
}
