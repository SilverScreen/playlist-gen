package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.database.LibraryDao;
import com.william.playlistgen.exception.DirectoryNotFoundException;
import com.william.playlistgen.mp3tagreader.Mp3TagReader;
import com.william.playlistgen.mp3tagreader.Mp3TagReaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author William
 */
public class MusicLibraryScanner implements FilenameFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicLibraryScanner.class);
    private static final String CODEC_MP3 = ".mp3";
    private static final String URI_SCHEME = "file:";
    private Path musicDirectoryRoot;
    private LibraryDao libraryDao = LibraryDao.getInstance();

    public MusicLibraryScanner(final String rootDirectoryPath) throws DirectoryNotFoundException {
        try {
            final URL musicDirectoryUrl = new URL(URI_SCHEME + rootDirectoryPath);
            musicDirectoryRoot = Paths.get(musicDirectoryUrl.toURI());
        } catch (final MalformedURLException | URISyntaxException | IllegalArgumentException ex) {
            LOGGER.error("Error when initializing Music Library Scanner", ex);
            throw new DirectoryNotFoundException("Directory '" + rootDirectoryPath + "' not found", ex);
        }
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(CODEC_MP3);
    }

    public void startScan() {
        final long startTime = System.currentTimeMillis();
        libraryDao.dropTable();
        libraryDao.createTable();
        processDirectory(musicDirectoryRoot);
        LOGGER.info("Completed music library scan in [{}] ms", (System.currentTimeMillis() - startTime));
    }

    private void processDirectory(final Path directory) {
        final long start = System.currentTimeMillis();
        final List<Song> songs = new ArrayList<>();
        try {
            final DirectoryStream<Path> filesInCurrentDirectory = Files.newDirectoryStream(directory);
            for (final Path currentFile : filesInCurrentDirectory) {
                if (currentFile.toString().endsWith(CODEC_MP3)) {
                    processMp3File(songs, currentFile.toFile());
                } else if (Files.isDirectory(currentFile)) {
                    processDirectory(currentFile);
                }
            }
        } catch (final IOException ex) {
            LOGGER.error("Error processing directory [{}]", directory, ex);
        }
        LOGGER.info("Inserting [{}] songs into music database", songs.size());
        libraryDao.insertSongs(songs);
        LOGGER.info("Processed directory [{}] in [{}] ms", directory, System.currentTimeMillis() - start);
    }

    private void processMp3File(final List<Song> songs, final File currentFile) {
        LOGGER.info("Found MP3 file [{}]", currentFile.getName());
        final Song song = getSongDetailsFromFile(currentFile);
        if (song != null) {
            songs.add(song);
        }
    }

    private Song getSongDetailsFromFile(final File mp3File) {
        final Mp3TagReader mp3TagReader = Mp3TagReaderFactory.getTagReader(mp3File);
        try {
            final Song song = new Song();
            song.setTitle(mp3TagReader.getSongTitle());
            song.setArtist(mp3TagReader.getArtist());
            song.setAlbum(mp3TagReader.getAlbum());
            song.setGenre(mp3TagReader.getGenre());
            song.setFilePath(mp3File.toURI().toURL().toString());
            return song;
        } catch (final Exception ex) {
            LOGGER.error("Error reading song details from file [{}]", mp3File.getName(), ex);
        }
        return null;
    }
}
