/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.database;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.testutils.SongBuilder;
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

    private static boolean testDataEntered = false;

    private static final String FILE_SEPARATOR = File.separator;
    private static final String TEST_FOLDER = "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources"
            + FILE_SEPARATOR + "testMp3Folder" + FILE_SEPARATOR;
    private LibraryDao libraryDao = null;
    private Song fadeIntoYou;
    private Song sexyBoy;
    private Song newNoise;
    private Song testSong;

    @Before
    public void setup() {
        libraryDao = LibraryDao.getInstance();
        fadeIntoYou = new SongBuilder().setTitle("Fade Into You").setArtist("Mazzy Star")
                .setAlbum("So Tonight That I Might See").setYear("1993").setGenre("Alternative")
                .setFilePath(TEST_FOLDER + "01 Fade Into You").build();
        sexyBoy = new SongBuilder().setTitle("Sexy Boy").setArtist("Air").setAlbum("Moon Safari").setYear("1998")
                .setGenre("Electronic").setFilePath(TEST_FOLDER + "02 Sexy Boy.mp3").build();
        newNoise = new SongBuilder().setTitle("New Noise").setArtist("Refused").setAlbum("The Shape of Punk to Come")
                .setYear("1998").setGenre("Punk").setFilePath(TEST_FOLDER + "06 New Noise.mp3").build();
        testSong = new SongBuilder().setTrackNumber(1).setTitle("'Test'").setArtist("Blah's").setAlbum("\"test\"")
                .setYear("2017").setGenre("").setFilePath("D:\\Music\\test\\\'test\'\\09. test.mp3").build();

        if (testDataEntered) {
            return;
        }
        libraryDao.dropTable();
        assertTrue(libraryDao.createTable());
        List<Song> songs = new ArrayList<>();
        songs.add(fadeIntoYou);
        songs.add(sexyBoy);
        songs.add(newNoise);
        songs.add(testSong);
        assertTrue(libraryDao.insert(songs));
        testDataEntered = true;
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
        assertEquals("File paths should be the same", sexyBoy.getFilePath(), resultFilePath);
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
        assertEquals("File paths should be the same", sexyBoy.getFilePath(), resultFilePath);
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
        assertEquals("File paths should be the same", newNoise.getFilePath(), returnedSong.getFilePath());
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

    @Test
    public void retrievesAllSongsByGenre() {
        final List<Song> songs = libraryDao.retrieveByGenre("Punk");
        assertFalse("Song list should not be empty", songs.isEmpty());
        assertEquals("File paths should be the same", newNoise.getFilePath(), songs.get(0).getFilePath());
    }

    @Test
    public void retrievesNoSongsForNonInvalidGenre() {
        final List<Song> songs = libraryDao.retrieveByGenre("Irish Polka");
        assertTrue("Song list should be empty", songs.isEmpty());
    }
}
