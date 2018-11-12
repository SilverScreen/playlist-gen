/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen;

import com.william.playlistgen.properties.PlaylistGenProperties;

import java.util.Scanner;

/**
 * @author William
 */
public class PlaylistGenCli {

    private static final String CHARSET = "UTF-8";
    private static final String CONFIG_PROPERTIES_DEFAULT = "config.properties";
    private final String musicLibrary;
    private final PlaylistGenerator playlistGenerator;
    private final Scanner userInput;

    private PlaylistGenCli() {
        final PlaylistGenProperties properties = new PlaylistGenProperties(CONFIG_PROPERTIES_DEFAULT);
        musicLibrary = properties.getProperty("musicLibrary");
        final String playlistDirectory = properties.getProperty("playlistDirectory");
        playlistGenerator = new PlaylistGenerator(playlistDirectory);
        userInput = new Scanner(System.in, CHARSET);
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
        System.out.println("4 - Generate Last.fm friends loved tracks playlist");
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
                loadMusicLibrary();
                break;
            case 2:
                playlistGenerator.generateTopTracksPlaylist();
                break;
            case 3:
                playlistGenerator.generateMyLovedTracksPlaylist();
                break;
            case 4:
                playlistGenerator.generateFriendsLovedTracksPlaylist();
                break;
            case 0:
                break;
            default:
                System.out.println("\n'" + optionSelected + "' is not a valid option. Try again");
        }
    }

    private void loadMusicLibrary() {
        System.out.print("This will erase any library data you currently have. Continue (y/n)? -> ");
        final String userChoice = userInput.next();
        if (userChoiceIsYes(userChoice)) {
            playlistGenerator.loadLibrary(musicLibrary);
        }
    }

    private boolean userChoiceIsYes(final String userChoice) {
        return (userChoice.toLowerCase().equals("y")) || userChoice.toLowerCase().equals("yes");
    }
}
