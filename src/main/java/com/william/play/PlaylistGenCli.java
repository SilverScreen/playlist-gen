/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.play;

import com.william.dev.common.utils.Song;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author William
 */
public class PlaylistGenCli {

    private final static String PLAYLIST_FILE_EXT = ".m3u";
    private final String musicLibrary;
    private final String playlistDirectory;
    private final PlaylistGenerator playlistGenerator;
    private final Scanner userInput;

    private PlaylistGenCli() {
        final Properties properties = new Properties();
        try {
            final File configProperties = new File("config.properties");
            final FileInputStream fileInput = new FileInputStream(configProperties);
            properties.load(fileInput);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        musicLibrary = properties.getProperty("musicLibrary");
        playlistDirectory = properties.getProperty("playlistDirectory");
        playlistGenerator = new PlaylistGenerator();
        userInput = new Scanner(System.in);
    }

    public static void main(final String[] args) {
        final PlaylistGenCli cli = new PlaylistGenCli();
        int optionSelected;
        do {
            optionSelected = cli.getMenuChoice();
            cli.handleUserInput(optionSelected);
        } while (optionSelected != 0);
        System.out.println("Exiting...");
    }

    private int getMenuChoice() {
        System.out.println("\n=========== PlAyLiStGeN ===========");
        System.out.println();
        System.out.println("\tSelect an option...\n");
        System.out.println("1 - Load local library");
        System.out.println("2 - Generate Last.fm top tracks playlist");
        System.out.println("3 - Generate Last.fm loved tracks playlist");
        System.out.println("0 - Exit");
        System.out.println();
        System.out.println("===================================");
        System.out.print("\nEnter choice -> ");

        int menuChoice = 9999;
        if (userInput.hasNextInt()) {
            menuChoice = userInput.nextInt();
        } else {
            userInput.next();
        }
        return menuChoice;
    }

    private void handleUserInput(final int optionSelected) {
        switch (optionSelected) {
            case 1:
                loadLocalLibrary();
                break;
            case 2:
                generateLastFmTopTracksPlaylist();
                break;
            case 3:
                generateLastFmLovedPlaylist();
                break;
            case 0:
                break;
            default:
                System.out.println("\n'" + optionSelected + "' is not a valid option. Try again");
        }
    }

    private void loadLocalLibrary() {
        System.out.print("This will erase any library data you currently have. Continue (y/n)? -> ");
        final String userChoice = userInput.next();
        if (userChoiceIsYes(userChoice)) {
            if (playlistGenerator.loadLibrary(musicLibrary)) {
                System.out.println("Library successfully loaded");
            } else {
                System.out.println("Error occurred during loading of library");
            }
        }
    }

    private boolean userChoiceIsYes(final String userChoice) {
        return (userChoice.toLowerCase().equals("y")) || userChoice.toLowerCase().equals("yes");
    }

    private void generateLastFmTopTracksPlaylist() {
        final String playlistFileName = "lastfmtoptracks" + PLAYLIST_FILE_EXT;
        final List<Song> songList = playlistGenerator.getTopTracks();
        generatePlaylistFile(songList, playlistFileName);
    }

    private void generateLastFmLovedPlaylist() {
        final String playlistFileName = "lastfmloved" + PLAYLIST_FILE_EXT;
        final List<Song> songList = playlistGenerator.getLastFmLovedTracks();
        generatePlaylistFile(songList, playlistFileName);
    }

    private void generatePlaylistFile(final List<Song> songList, final String playlistFileName) {
        final String filePath = playlistDirectory + File.separator + playlistFileName;
        final PlaylistFileWriter playlistFileWriter = new PlaylistFileWriter(filePath);
        playlistFileWriter.writePlaylistToFile(songList);
        System.out.println("\nPlaylist written to '" + playlistFileName + "'");
    }
}
