/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.database;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.validation.SongDataValidator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.william.playlistgen.database.LibrarySqlStatements.CREATE_TABLE_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.DROP_TABLE_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.INSERT_SONG_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.RETRIEVE_ALL_SONGS;
import static com.william.playlistgen.database.LibrarySqlStatements.RETRIEVE_BY_ALBUM_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.RETRIEVE_BY_ARTIST_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.RETRIEVE_BY_SONG_SQL;

/**
 * @author William
 */
public class LibraryDao {

    private static final String DATABASE_NAME = "jdbc:sqlite:test.db";
    private static final String tableName = "MyMusic";
    private static LibraryDao instance = null;

    private LibraryDao() {
    }

    public static LibraryDao getInstance() {
        if (instance == null) {
            loadJdbcDriver();
            instance = new LibraryDao();
        }
        return instance;
    }

    private static void loadJdbcDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean createTable() {
        if (!dropTable()) {
            return false;
        }
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement createTableStatement = connection.prepareStatement(CREATE_TABLE_SQL)) {
            createTableStatement.executeUpdate();
            System.out.println("Table '" + tableName + "' created successfully!");
            return true;
        } catch (final SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public boolean dropTable() {
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement dropTableStatement = connection.prepareStatement(DROP_TABLE_SQL)) {
            dropTableStatement.executeUpdate();
        } catch (final SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insert(final List<Song> songList) {
        boolean allDataEnteredSuccessfully = true;
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME)) {
            int numberOfSongsSuccessfullyEntered = 0;
            for (final Song song : songList) {
                if (!SongDataValidator.isValid(song)) {
                    System.out.println("Skipping song with invalid data...");
                    allDataEnteredSuccessfully = false;
                    continue;
                }
                allDataEnteredSuccessfully = insertSong(connection, song);
                numberOfSongsSuccessfullyEntered++;
                System.out.print("Entered " + numberOfSongsSuccessfullyEntered + " / " + songList.size() + " songs\r");
            }
        } catch (final SQLException ex) {
            Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
            allDataEnteredSuccessfully = false;
        }
        return allDataEnteredSuccessfully;
    }

    private boolean insertSong(final Connection connection, final Song song) {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SONG_SQL)) {
            preparedStatement.setString(1, formatSingleQuotes(song.getTitle()));
            preparedStatement.setString(2, formatSingleQuotes(song.getArtist()));
            preparedStatement.setString(3, formatSingleQuotes(song.getAlbum()));
            preparedStatement.setString(4, formatSingleQuotes(song.getFilePath()));
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    private String formatSingleQuotes(final String stringToFormat) {
        if (stringToFormat == null) {
            return null;
        } else {
            return stringToFormat.replace("'", "''");
        }
    }

    public List<Song> retrieveByArtist(final String artist) {
        List<Song> songsByArtist = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement statement = connection.prepareStatement(RETRIEVE_BY_ARTIST_SQL)) {
            statement.setString(1, formatSingleQuotes(artist));
            songsByArtist = getResultsFromPreparedStatement(statement);
        } catch (final SQLException ex) {
            Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return songsByArtist;
    }

    public List<Song> retrieveByAlbum(final String artist, final String album) {
        List<Song> songsByAlbum = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement statement = connection.prepareStatement(RETRIEVE_BY_ALBUM_SQL)) {
            statement.setString(1, formatSingleQuotes(artist));
            statement.setString(2, formatSingleQuotes(album));
            songsByAlbum = getResultsFromPreparedStatement(statement);
        } catch (final SQLException ex) {
            Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return songsByAlbum;
    }

    public Song retrieveBySong(final String artist, final String title) {
        List<Song> songs = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement statement = connection.prepareStatement(RETRIEVE_BY_SONG_SQL)) {
            statement.setString(1, formatSingleQuotes(artist));
            statement.setString(2, formatSingleQuotes(title));
            songs = getResultsFromPreparedStatement(statement);
        } catch (final SQLException ex) {
            Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return songs.isEmpty() ? null : songs.get(0);
    }

    public List<Song> retrieveAllSongs() {
        List<Song> allSongs = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement statement = connection.prepareStatement(RETRIEVE_ALL_SONGS)) {
            allSongs = getResultsFromPreparedStatement(statement);
        } catch (final SQLException ex) {
            Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allSongs;
    }

    private List<Song> getResultsFromPreparedStatement(final PreparedStatement statement) {
        final List<Song> songs = new ArrayList<>();
        try (final ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Song song = new Song();
                song.setTitle(resultSet.getString("title"));
                song.setArtist(resultSet.getString("artist"));
                song.setAlbum(resultSet.getString("album"));
                song.setFilePath(resultSet.getString("path"));
                songs.add(song);
            }
        } catch (final SQLException ex) {
            Logger.getLogger(LibraryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return songs;
    }
}
