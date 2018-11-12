/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;
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
public class FileScanner implements FilenameFilter {

    private static final String CODEC_MP3 = ".mp3";
    private static final int LENGTH_OF_WHITESPACE = 41;

    @Override
    public boolean accept(final File dir, final String name) {
        return name.endsWith(CODEC_MP3);
    }

    public List<Song> getMp3SongList(final File directory) {
        final File[] fileList = directory.listFiles();
        final List<Song> mp3SongList = new ArrayList<>();

        if (fileList == null) {
            return mp3SongList;
        }
        for (final File file : fileList) {
            processFile(mp3SongList, file);
        }
        System.out.println("Found " + mp3SongList.size() + " files in directory " + directory.getPath() + "\n");
        return mp3SongList;
    }

    private void processFile(final List<Song> mp3SongList, final File file) {
        if (file.isDirectory()) {
            final List<Song> songsInDirectory = getMp3SongList(file);
            mp3SongList.addAll(songsInDirectory);
        } else if (file.getName().endsWith(CODEC_MP3)) {
            System.out.println("Found file" + file.getName()
                    + String.format("%" + LENGTH_OF_WHITESPACE + "s", "") + "\r");
            try {
                addSongDetailsFromFile(file, mp3SongList);
            } catch (MalformedURLException ex) {
                Logger.getLogger(FileScanner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void addSongDetailsFromFile(final File file, final List<Song> mp3SongList) throws MalformedURLException {
        final Mp3TagReader mp3TagReader = Mp3TagReaderFactory.getTagReader(file);
        final Song song = new Song();
        song.setTitle(mp3TagReader.getSongTitle());
        song.setArtist(mp3TagReader.getArtist());
        song.setAlbum(mp3TagReader.getAlbum());
        song.setFilePath(file.toURI().toURL().toString());
        mp3SongList.add(song);
    }
}
