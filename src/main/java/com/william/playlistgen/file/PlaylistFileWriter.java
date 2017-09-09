package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
