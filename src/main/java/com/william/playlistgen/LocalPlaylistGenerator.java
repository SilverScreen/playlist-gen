package com.william.playlistgen;

import com.william.dev.common.utils.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LocalPlaylistGenerator extends PlaylistGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalPlaylistGenerator.class);
    private static final int NUMBER_OF_SHUFFLES = 10;
    private static final int RANDOMNESS_FACTOR = 100;
    private static final int PLAYLIST_LIMIT = 25;
    private static final String PLAYLIST_NAME_TEMPLATE = "my%sPlaylist";

    public LocalPlaylistGenerator(String playlistDirectory) {
        super(playlistDirectory);
    }

    public void generateGenrePlaylist(final String genre) {
        LOGGER.info("Generating [{}] playlist", genre);
        List<Song> songsByGenre = libraryDao.retrieveByGenre(genre);
        for (int i = 0; i <= NUMBER_OF_SHUFFLES; i++) {
            Collections.shuffle(songsByGenre, new Random(RANDOMNESS_FACTOR));
        }
        songsByGenre = songsByGenre.stream().limit(PLAYLIST_LIMIT).collect(Collectors.toList());
        generatePlaylistFile(songsByGenre, String.format(PLAYLIST_NAME_TEMPLATE, genre));
    }

    public void generateYearPlaylist(final String year) {
        LOGGER.info("Generating [{}] playlist", year);
        List<Song> songsByYear = libraryDao.retrieveByYear(year);
        Collections.shuffle(songsByYear, new Random(RANDOMNESS_FACTOR));
        songsByYear = songsByYear.stream().limit(PLAYLIST_LIMIT).collect(Collectors.toList());
        generatePlaylistFile(songsByYear, String.format(PLAYLIST_NAME_TEMPLATE, year));
    }
}
