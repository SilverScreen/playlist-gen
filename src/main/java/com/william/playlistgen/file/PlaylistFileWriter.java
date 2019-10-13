package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author William
 */
public class PlaylistFileWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistFileWriter.class);
    private final String filename;

    public PlaylistFileWriter(final String filename) {
        this.filename = filename;
    }

    public void writePlaylistToFile(final List<Song> mp3List) {
        if (mp3List.isEmpty()) {
            LOGGER.warn("Song list is empty. Not writing playlist to file");
            return;
        }
        try (final OutputStreamWriter writer
                     = new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8)) {
            for (final Song song : mp3List) {
                final String filePath = song.getFilePath()
                        .replace("file:/", "")
                        .replace("%20", " ")
                        .replace("%5B", "[")
                        .replace("%5D", "]")
                        .replace("/", "\\");
                writer.write(filePath);
            }
        } catch (final IOException ex) {
            LOGGER.error("Error writing playlist to file", ex);
        }
    }
}
