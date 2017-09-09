/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen;

import com.william.dev.common.utils.Song;
import com.william.dev.lastfmhelper.lastfmplaylistgen.LastFmPlaylistGen;
import com.william.dev.lastfmhelper.lastfmplaylistgen.LastFmPlaylistGenImpl;
import com.william.playlistgen.database.LibraryDao;
import com.william.playlistgen.file.FileScanner;
import com.william.playlistgen.file.PlaylistFileWriter;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author William
 */
public class PlaylistGenerator {

    private static final String LAST_FM_USERNAME = "Zero1986";
    private final static String PLAYLIST_FILE_EXT = ".m3u";
    private final LastFmPlaylistGen lastFmPlaylistGen;
    private final FileScanner fileScanner;
    private final LibraryDao libraryDao;
    private final String playlistDirectory;

    public PlaylistGenerator(final String playlistDirectory) {
        lastFmPlaylistGen = new LastFmPlaylistGenImpl();
        fileScanner = new FileScanner();
        libraryDao = LibraryDao.getInstance();
        this.playlistDirectory = playlistDirectory;
    }

    public boolean loadLibrary(final String musicLibraryPath) {
        final List<Song> songList = fileScanner.getMp3SongList(new File(musicLibraryPath));
        return libraryDao.createTable() && libraryDao.insert(songList);
    }

    public void generateTopTracksPlaylist() {
        final List<Song> topTracks = lastFmPlaylistGen.getTopTracks(LAST_FM_USERNAME);
        final List<Song> songsFromDatabase = getSongsFromDatabase(topTracks);
        final String playlistFileName = "lastfmtoptracks" + PLAYLIST_FILE_EXT;
        generatePlaylistFile(songsFromDatabase, playlistFileName);
    }

    public void generateMyLovedTracksPlaylist() {
        final List<Song> lastFmLovedTracks = lastFmPlaylistGen.getLovedTracks(LAST_FM_USERNAME);
        final List<Song> songsFromDatabase = getSongsFromDatabase(lastFmLovedTracks);
        final String playlistFileName = "lastfmloved" + PLAYLIST_FILE_EXT;
        generatePlaylistFile(songsFromDatabase, playlistFileName);
    }

    public void generateFriendsLovedTracksPlaylist() {
        final List<Song> friendsLovedTracks = lastFmPlaylistGen.getFriendsLastLovedTracks(LAST_FM_USERNAME);
        final List<Song> songsFromDatabase = getSongsFromDatabase(friendsLovedTracks);
        final String playlistFileName = "friendslastfmloved" + PLAYLIST_FILE_EXT;
        generatePlaylistFile(songsFromDatabase, playlistFileName);
    }

    private List<Song> getSongsFromDatabase(final List<Song> lastFmFetchedTracks) {
        List<Song> mp3List = libraryDao.retrieveAllSongs();
        mp3List.retainAll(lastFmFetchedTracks);
        Collections.shuffle(mp3List);
        return mp3List;
    }

    private void generatePlaylistFile(final List<Song> songList, final String playlistFileName) {
        if (!songList.isEmpty()) {
            final String filePath = playlistDirectory + File.separator + playlistFileName;
            final PlaylistFileWriter playlistFileWriter = new PlaylistFileWriter(filePath);
            playlistFileWriter.writePlaylistToFile(songList);
            System.out.println("\nPlaylist written to '" + playlistFileName + "'");
        } else {
            System.out.println("No matching songs found. Playlist not generated");
        }
    }
}
