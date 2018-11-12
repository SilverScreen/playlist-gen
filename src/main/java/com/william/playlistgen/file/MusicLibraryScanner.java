package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.database.LibraryDao;
import com.william.playlistgen.mp3tagreader.Mp3TagReader;
import com.william.playlistgen.mp3tagreader.Mp3TagReaderFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author William
 */
public class MusicLibraryScanner implements FilenameFilter {

    private static final String CODEC_MP3 = ".mp3";
    private File rootDirectory;
    private LibraryDao libraryDao = LibraryDao.getInstance();

    public MusicLibraryScanner(final File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(CODEC_MP3);
    }

    public void start() {
        libraryDao.dropTable();
        libraryDao.createTable();
        processDirectory(rootDirectory);
    }

    private void processDirectory(final File directory) {
        final File[] filesInDirectory = directory.listFiles();
        if (filesInDirectory != null && filesInDirectory.length > 0) {
            final List<Song> songs = new ArrayList<>();
            for (final File currentFile : filesInDirectory) {
                if (currentFile.isDirectory()) {
                    processDirectory(currentFile);
                } else if (currentFile.getName().endsWith(CODEC_MP3)) {
                    processMp3File(songs, currentFile);
                }
            }
            libraryDao.insert(songs);
        }
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
