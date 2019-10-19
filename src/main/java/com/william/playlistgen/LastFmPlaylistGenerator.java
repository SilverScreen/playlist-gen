package com.william.playlistgen;

import com.william.dev.common.utils.Song;
import com.william.dev.lastfmhelper.lastfmplaylistgen.LastFmPlaylistGen;
import com.william.dev.lastfmhelper.lastfmplaylistgen.LastFmPlaylistGenImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LastFmPlaylistGenerator extends PlaylistGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistGenerator.class);
    private static final String LAST_FM_USERNAME = "Zero1986";

    private final LastFmPlaylistGen lastFmPlaylistGen;

    public LastFmPlaylistGenerator(String playlistDirectory) {
        super(playlistDirectory);
        lastFmPlaylistGen = new LastFmPlaylistGenImpl();
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
}
