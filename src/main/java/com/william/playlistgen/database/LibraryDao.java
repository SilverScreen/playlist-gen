/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.database;

import com.william.dev.common.utils.Song;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William
 */
public class LibraryDao {

    private final String tableName = "MyMusic";
    private static LibraryDao instance = null;

    private LibraryDao() {
    }

    public static LibraryDao getInstance() {
        if (instance == null) {
            instance = new LibraryDao();
        }
        return instance;
    }

    public boolean createTable() {
        connectionSetup();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement statement = connection.createStatement();) {
            String dropTableSql = "DROP TABLE IF EXISTS " + tableName;
            statement.execute(dropTableSql);

            String createTableSql = "CREATE TABLE " + tableName + "("
                    + "title    TEXT    NOT NULL,"
                    + "artist   TEXT    NOT NULL,"
                    + "album    TEXT    NOT NULL,"
                    + "path     TEXT    NOT NULL)";
            statement.executeUpdate(createTableSql);
            System.out.println("Table '" + tableName + "' created successfully!");
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public boolean insert(List<Song> songList) {
        connectionSetup();
        Connection connection;
        Statement statement;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        boolean allDataEnteredSuccessfully = true;
        int numberOfSongsSuccessfullyEntered = 0;
        for (Song song : songList) {
            String title = formatSingleQuotes(song.getTitle());
            String artist = formatSingleQuotes(song.getArtist());
            String album = formatSingleQuotes(song.getAlbum());
            String filePath = formatSingleQuotes(song.getFilePath());

            String insertSql = ""
                    + "INSERT INTO " + tableName + "(title, artist, album, path)"
                    + " VALUES ('" + title + "', '" + artist + "', '" + album + "', '" + filePath + "')";
            try {
                statement.executeUpdate(insertSql);
                numberOfSongsSuccessfullyEntered++;
                System.out.print("Entered " + numberOfSongsSuccessfullyEntered + " / " + songList.size() + " songs\r");
            } catch (SQLException ex) {
                Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
                allDataEnteredSuccessfully = false;
            }
        }
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allDataEnteredSuccessfully;
    }

    private String formatSingleQuotes(String stringToFormat) {
        if (stringToFormat == null) {
            return stringToFormat;
        } else {
            return stringToFormat.replace("'", "''");
        }
    }

    public List<Song> retrieveByArtist(String artist) {
        String sqlStatementToExecute = ""
                + "SELECT * FROM " + tableName + " "
                + "WHERE LOWER(artist) = LOWER('" + formatSingleQuotes(artist) + "')";
        return getResultsFromSqlStatement(sqlStatementToExecute);
    }

    public List<Song> retrieveByAlbum(String artist, String album) {
        String sqlStatementToExecute = ""
                + "SELECT * FROM " + tableName + " "
                + "WHERE LOWER(artist) = LOWER('" + formatSingleQuotes(artist) + "') "
                + "AND LOWER(album) = LOWER('" + formatSingleQuotes(album) + "')";
        return getResultsFromSqlStatement(sqlStatementToExecute);
    }

    public Song retrieveBySong(String artist, String title) {
        String sqlStatementToExecute = ""
                + "SELECT * FROM " + tableName + " "
                + "WHERE LOWER(artist) = LOWER('" + formatSingleQuotes(artist) + "') "
                + "AND LOWER(title) = LOWER('" + formatSingleQuotes(title) + "')";
        List<Song> resultSongList = getResultsFromSqlStatement(sqlStatementToExecute);
        if (resultSongList.isEmpty()) {
            return null;
        } else {
            return resultSongList.get(0);
        }
    }

    public List<Song> retrieveAllSongs() {
        String sqlStatement = "SELECT * FROM " + tableName;
        return getResultsFromSqlStatement(sqlStatement);
    }

    private List<Song> getResultsFromSqlStatement(String sqlStatementToExecute) {
        connectionSetup();
        List<Song> resultSongList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(sqlStatementToExecute);

            while (resultSet.next()) {
                Song song = new Song();
                song.setTitle(resultSet.getString("title"));
                song.setArtist(resultSet.getString("artist"));
                song.setAlbum(resultSet.getString("album"));
                song.setFilePath(resultSet.getString("path"));
                resultSongList.add(song);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return resultSongList;
    }

    private void connectionSetup() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
