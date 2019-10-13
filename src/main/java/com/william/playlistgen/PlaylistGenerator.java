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
import com.william.playlistgen.exception.DirectoryNotFoundException;
import com.william.playlistgen.file.MusicLibraryScanner;
import com.william.playlistgen.file.PlaylistFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author William
 */
public class PlaylistGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistGenerator.class);
    private static final String LAST_FM_USERNAME = "Zero1986";
    private final static String PLAYLIST_FILE_EXT = ".m3u";
    private final LastFmPlaylistGen lastFmPlaylistGen;
    private final LibraryDao libraryDao;
    private final String playlistDirectory;

    public PlaylistGenerator(final String playlistDirectory) {
        lastFmPlaylistGen = new LastFmPlaylistGenImpl();
        libraryDao = LibraryDao.getInstance();
        this.playlistDirectory = playlistDirectory;
    }

    public void loadLibrary(final String musicLibraryPath) {
        try {
            final MusicLibraryScanner libraryScanner = new MusicLibraryScanner(musicLibraryPath);
            libraryScanner.start();
        } catch (final DirectoryNotFoundException ex) {
            LOGGER.error("Error loading music library [{}]", musicLibraryPath, ex);
        }
    }

    public void generateTopTracksPlaylist() {
        LOGGER.info("Generating Top Tracks playlist");
        final List<Song> topTracks = lastFmPlaylistGen.getTopTracks(LAST_FM_USERNAME);
        final List<Song> songsFromDatabase = getSongsFromDatabase(topTracks);
        generatePlaylistFile(songsFromDatabase, "lastfmtoptracks");
    }

    public void generateMyLovedTracksPlaylist() {
        LOGGER.info("Generating My Loved Tracks playlist");
        final List<Song> lastFmLovedTracks = lastFmPlaylistGen.getLovedTracks(LAST_FM_USERNAME);
        final List<Song> songsFromDatabase = getSongsFromDatabase(lastFmLovedTracks);
        generatePlaylistFile(songsFromDatabase, "lastfmloved");
    }

    public void generateFriendsLovedTracksPlaylist() {
        LOGGER.info("Generating Friends Loved Tracks playlist");
        final List<Song> friendsLovedTracks = lastFmPlaylistGen.getFriendsLastLovedTracks(LAST_FM_USERNAME);
        final List<Song> songsFromDatabase = getSongsFromDatabase(friendsLovedTracks);
        generatePlaylistFile(songsFromDatabase, "friendslastfmloved");
    }

    private List<Song> getSongsFromDatabase(final List<Song> lastFmFetchedTracks) {
        LOGGER.debug("Getting song data from database");
        List<Song> mp3List = libraryDao.retrieveAllSongs();
        mp3List.retainAll(lastFmFetchedTracks);
        Collections.shuffle(mp3List);
        LOGGER.debug("Found [{}] songs in database", mp3List.size());
        return mp3List;
    }

    private void generatePlaylistFile(final List<Song> songList, final String playlistFileName) {
        final String playlistFile = playlistFileName + PLAYLIST_FILE_EXT;
        if (!songList.isEmpty()) {
            final String filePath = playlistDirectory + File.separator + playlistFile;
            final PlaylistFileWriter playlistFileWriter = new PlaylistFileWriter(filePath);
            playlistFileWriter.writePlaylistToFile(songList);
            LOGGER.info("Playlist written to [{}]", playlistFile);
        } else {
            LOGGER.warn("No matching songs found. Playlist not generated");
        }
    }
}
