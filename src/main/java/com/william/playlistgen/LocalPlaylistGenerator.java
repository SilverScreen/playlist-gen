package com.william.playlistgen;

import com.william.dev.common.utils.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LocalPlaylistGenerator extends PlaylistGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalPlaylistGenerator.class);

    public LocalPlaylistGenerator(String playlistDirectory) {
        super(playlistDirectory);
    }

    public void generateGenrePlaylist(final String genre) {
        LOGGER.info("Generating [{}] playlist", genre);
        List<Song> songsByGenre = libraryDao.retrieveByGenre(genre);
        Collections.shuffle(songsByGenre);
        songsByGenre = songsByGenre.stream().limit(25).collect(Collectors.toList());
        generatePlaylistFile(songsByGenre, "my" + genre + "Playlist");
    }
}
