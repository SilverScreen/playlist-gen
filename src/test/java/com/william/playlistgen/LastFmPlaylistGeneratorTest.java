package com.william.playlistgen;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Ignoring until Last FM API is fixed")
public class LastFmPlaylistGeneratorTest extends PlaylistGeneratorTestBase {

    private LastFmPlaylistGenerator objUnderTest;

    @Before
    public void setup() {
        playlistFolder = testFolder.newFolder("PlaylistFolder");
        objUnderTest = new LastFmPlaylistGenerator(playlistFolder.getAbsolutePath());
        objUnderTest.loadLibrary(getMusicLibraryPath());
    }

    @Test
    public void generateTopTracksPlaylist_successfullyGeneratesFile() {
        objUnderTest.generateTopTracksPlaylist();
        assertPlaylistFileCreated("lastfmtoptracks.m3u");
    }

    @Test
    @Ignore
    public void generateMyLovedTracksPlaylist_successfullyGeneratesFile() {
        objUnderTest.generateMyLovedTracksPlaylist();
        assertPlaylistFileCreated("lastfmloved.m3u");
    }

    @Test
    public void generateFriendsLovedTracksPlaylist_successfullyGeneratesFile() {
        objUnderTest.generateFriendsLovedTracksPlaylist();
        assertPlaylistFileCreated("friendslastfmloved.m3u");
    }
}
