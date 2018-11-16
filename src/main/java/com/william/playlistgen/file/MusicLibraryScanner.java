package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.database.LibraryDao;
import com.william.playlistgen.exception.DirectoryNotFoundException;
import com.william.playlistgen.mp3tagreader.Mp3TagReader;
import com.william.playlistgen.mp3tagreader.Mp3TagReaderFactory;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author William
 */
public class MusicLibraryScanner implements FilenameFilter {

    private static final String CODEC_MP3 = ".mp3";
    private static final String URI_SCHEME = "file:";
    private Path rootDirectory;
    private LibraryDao libraryDao = LibraryDao.getInstance();

    public MusicLibraryScanner(final String rootDirectoryPath) throws DirectoryNotFoundException {
        try {
            final URL url = new URL(URI_SCHEME + rootDirectoryPath);
            rootDirectory = Paths.get(url.toURI());
        } catch (final MalformedURLException | URISyntaxException | IllegalArgumentException ex) {
            Logger.getLogger(MusicLibraryScanner.class.getName()).log(Level.SEVERE,
                    "Error when initializing Music Library Scanner", ex);
            throw new DirectoryNotFoundException("Directory '" + rootDirectoryPath + "' not found", ex);
        }
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(CODEC_MP3);
    }

    public void start() {
        final long startTime = System.currentTimeMillis();
        libraryDao.dropTable();
        libraryDao.createTable();
        processDirectory(rootDirectory);
        System.out.println("Completed music library scan in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private void processDirectory(final Path directory) {
        final List<Song> songs = new ArrayList<>();
        try {
            final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory);
            for (final Path currentPath : directoryStream) {
                if (Files.isDirectory(currentPath)) {
                    processDirectory(currentPath);
                } else if (currentPath.toString().endsWith(CODEC_MP3)) {
                    processMp3File(songs, currentPath.toFile());
                }
            }
        } catch (final IOException ex) {
            Logger.getLogger(MusicLibraryScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
        libraryDao.insert(songs);
    }

    private void processMp3File(final List<Song> songs, final File currentFile) {
        System.out.println("Found MP3 file: " + currentFile.getName());
        try {
            final Song song = getSongDetailsFromFile(currentFile);
            if (song != null) {
                songs.add(song);
            }
        } catch (final MalformedURLException ex) {
            Logger.getLogger(MusicLibraryScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Song getSongDetailsFromFile(final File mp3File) throws MalformedURLException {
        final Mp3TagReader mp3TagReader = Mp3TagReaderFactory.getTagReader(mp3File);
        try {
            final Song song = new Song();
            song.setTitle(mp3TagReader.getSongTitle());
            song.setArtist(mp3TagReader.getArtist());
            song.setAlbum(mp3TagReader.getAlbum());
            song.setFilePath(mp3File.toURI().toURL().toString());
            return song;
        } catch (final Exception ex) {
            Logger.getLogger(MusicLibraryScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
