/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.play;

import com.william.dev.common.utils.Song;
import com.william.dev.lastfmhelper.lastfmplaylistgen.LastFmPlaylistGen;
import com.william.dev.lastfmhelper.lastfmplaylistgen.LastFmPlaylistGenImpl;
import com.william.play.database.LibraryDao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/**
 * @author William
 */
public class PlaylistGenerator {

    private static final String LAST_FM_USERNAME = "Zero1986";
    private final LastFmPlaylistGen lastFmPlaylistGen;
    private final FileScanner fileScanner;
    private final LibraryDao libraryDao;

    public PlaylistGenerator() {
        lastFmPlaylistGen = new LastFmPlaylistGenImpl();
        fileScanner = new FileScanner();
        libraryDao = LibraryDao.getInstance();
    }

    public boolean loadLibrary(final String musicLibraryPath) {
        final List<Song> songList = fileScanner.getMp3SongList(new File(musicLibraryPath));
        return libraryDao.createTable() && libraryDao.insert(songList);
    }

    public List<Song> getTopTracks() {
        final List<Song> topTracks = lastFmPlaylistGen.getTopTracks(LAST_FM_USERNAME);
        return getSongsFromDatabase(topTracks);
    }

    public List<Song> getLastFmLovedTracks() {
        final List<Song> lastFmLovedTracks = lastFmPlaylistGen.getLovedTracks(LAST_FM_USERNAME);
        return getSongsFromDatabase(lastFmLovedTracks);
    }

    private List<Song> getSongsFromDatabase(final List<Song> lastFmFetchedTracks) {
        List<Song> mp3List = libraryDao.retrieveAllSongs();
        mp3List.retainAll(lastFmFetchedTracks);
        Collections.shuffle(mp3List);
        return mp3List;
    }

    public void writePlaylistToFile(final List<Song> mp3List, final String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Song song : mp3List) {
                String filePath = song.getFilePath()
                        .replace("file:/", "")
                        .replace("%20", " ")
                        .replace("%5B", "[")
                        .replace("%5D", "]")
                        .replace("/", "\\");
                pw.write(filePath + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
