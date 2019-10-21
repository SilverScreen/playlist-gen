/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.mp3tagreader;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author William
 */
public class Mp3TagReaderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mp3TagReaderFactory.class.getName());
    private static final String WHITESPACE = "%20";

    public static Mp3TagReader getTagReader(final File file) {
        Mp3TagReader mp3TagReader = null;
        try {
            final String filePath = file.getAbsolutePath().replaceAll(WHITESPACE, " ");
            final Mp3File mp3File = new Mp3File(filePath);
            if (mp3File.hasId3v1Tag()) {
                mp3TagReader = new Id3v1Mp3TagReader(mp3File.getId3v1Tag());
            } else if (mp3File.hasId3v2Tag()) {
                mp3TagReader = new Id3v2Mp3TagReader(mp3File.getId3v2Tag());
            }
        } catch (final IOException | UnsupportedTagException | InvalidDataException | IllegalArgumentException ex) {
            LOGGER.error("Error resolving mp3 tag reader for file [{}]", file.getName(), ex);
        }
        return mp3TagReader;
    }
}