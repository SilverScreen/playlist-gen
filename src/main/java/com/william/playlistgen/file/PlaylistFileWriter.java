package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author William
 */
public class PlaylistFileWriter {

    public final String filename;

    public PlaylistFileWriter(final String filename) {
        this.filename = filename;
    }

    public void writePlaylistToFile(final List<Song> mp3List) {
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
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
