package com.william.playlistgen.validation;

import com.william.dev.common.utils.Song;

/**
 *
 * @author William
 */
public class SongDataValidator {

    public static boolean isValid(final Song song) {
        final String artist = song.getArtist();
        final String album = song.getAlbum();
        final String title = song.getTitle();
        final String filePath = song.getFilePath();
        return artist != null && album !=null && title != null && filePath != null;
    }
}
