/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.file;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.Mp3TagFinder;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William
 */
public class FileScanner implements FilenameFilter {

    private static final String CODEC_MP3 = ".mp3";

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(CODEC_MP3);
    }

    public List<Song> getMp3SongList(final File directory) {
        File[] fileList = directory.listFiles();
        List<Song> mp3SongList = new ArrayList<>();

        if (fileList == null) {
            return mp3SongList;
        }
        for (File file : fileList) {
            if (file.isDirectory()) {
                List<Song> songsInDirectory = getMp3SongList(file);
                mp3SongList.addAll(songsInDirectory);
            } else if (file.getName().endsWith(CODEC_MP3)) {
                System.out.print("Found file " + file.getName() + "                                         \r");
                try {
                    addSongDetailsFromFile(file, mp3SongList);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(FileScanner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Found " + mp3SongList.size() + " files in directory " + directory.getPath() + "\n");
        return mp3SongList;
    }

    private void addSongDetailsFromFile(File file, List<Song> mp3SongList) throws MalformedURLException {
        Mp3TagFinder mp3TagFinder = new Mp3TagFinder(file);
        Song song = new Song();
        song.setTitle(mp3TagFinder.getSongTitle());
        song.setArtist(mp3TagFinder.getArtist());
        song.setAlbum(mp3TagFinder.getAlbum());
        song.setFilePath(file.toURI().toURL().toString());
        mp3SongList.add(song);
    }
}
