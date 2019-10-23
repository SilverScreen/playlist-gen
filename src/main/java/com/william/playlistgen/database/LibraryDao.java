/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.william.playlistgen.database;

import com.william.dev.common.utils.Song;
import com.william.playlistgen.validation.SongDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.william.playlistgen.database.LibrarySqlStatements.CREATE_TABLE_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.DROP_TABLE_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.INSERT_SONG_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.RETRIEVE_ALL_SONGS;
import static com.william.playlistgen.database.LibrarySqlStatements.RETRIEVE_BY_ALBUM_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.RETRIEVE_BY_ARTIST_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.RETRIEVE_BY_GENRE_SQL;
import static com.william.playlistgen.database.LibrarySqlStatements.RETRIEVE_BY_SONG_SQL;

/**
 * @author William
 */
public class LibraryDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryDao.class);
    private static final String DATABASE_NAME = "jdbc:sqlite:test.db";
    private static final String MUSIC_DATA_TABLE = "MyMusic";
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
        } catch (final ClassNotFoundException ex) {
            LOGGER.error("Error loading JDBC driver", ex);
        }
    }

    public boolean createTable() {
        if (!dropTable()) {
            return false;
        }
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement createTableStatement = connection.prepareStatement(CREATE_TABLE_SQL)) {
            createTableStatement.executeUpdate();
            LOGGER.info("Table [{}] created successfully!", MUSIC_DATA_TABLE);
            return true;
        } catch (final SQLException ex) {
            LOGGER.error("Error creating table [{}]", MUSIC_DATA_TABLE, ex);
            return false;
        }
    }

    public boolean dropTable() {
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement dropTableStatement = connection.prepareStatement(DROP_TABLE_SQL)) {
            dropTableStatement.executeUpdate();
        } catch (final SQLException ex) {
            LOGGER.error("Error dropping table [{}]", MUSIC_DATA_TABLE, ex);
            return false;
        }
        return true;
    }

    public boolean insertSongs(final List<Song> songList) {
        boolean allDataEnteredSuccessfully = false;
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SONG_SQL)) {
            int songCounter = 0;
            for (final Song song : songList) {
                if (!SongDataValidator.isValid(song)) {
                    LOGGER.warn("Skipping song with invalid data...");
                    continue;
                }
                preparedStatement.setString(1, formatSingleQuotes(song.getTitle()));
                preparedStatement.setString(2, formatSingleQuotes(song.getArtist()));
                preparedStatement.setString(3, formatSingleQuotes(song.getAlbum()));
                preparedStatement.setString(4, formatSingleQuotes(song.getGenre()));
                preparedStatement.setString(5, formatSingleQuotes(song.getFilePath()));
                preparedStatement.addBatch();
                songCounter++;
            }
            preparedStatement.executeBatch();
            LOGGER.info("Entered {} / {} songs", songCounter, songList.size());
            allDataEnteredSuccessfully = (songCounter == songList.size());
        } catch (final SQLException ex) {
            LOGGER.error("Error inserting music data into table [{}]", MUSIC_DATA_TABLE, ex);
        }
        return allDataEnteredSuccessfully;
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
            songsByArtist = executeRetrieveAllSongsQuery(statement);
        } catch (final SQLException ex) {
            LOGGER.error("Error retrieving data for artist [{}]", artist, ex);
        }
        return songsByArtist;
    }

    public List<Song> retrieveByAlbum(final String artist, final String album) {
        List<Song> songsByAlbum = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement statement = connection.prepareStatement(RETRIEVE_BY_ALBUM_SQL)) {
            statement.setString(1, formatSingleQuotes(artist));
            statement.setString(2, formatSingleQuotes(album));
            songsByAlbum = executeRetrieveAllSongsQuery(statement);
        } catch (final SQLException ex) {
            LOGGER.error("Error retrieving data for artist [{}], album [{}]", artist, album, ex);
        }
        return songsByAlbum;
    }

    public Song retrieveBySong(final String artist, final String title) {
        List<Song> songs = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement statement = connection.prepareStatement(RETRIEVE_BY_SONG_SQL)) {
            statement.setString(1, formatSingleQuotes(artist));
            statement.setString(2, formatSingleQuotes(title));
            songs = executeRetrieveAllSongsQuery(statement);
        } catch (final SQLException ex) {
            LOGGER.error("Error retrieving data for artist [{}], title [{}]", artist, title, ex);
        }
        return songs.isEmpty() ? null : songs.get(0);
    }

    public List<Song> retrieveByGenre(final String genre) {
        List<Song> songs = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement statement = connection.prepareStatement(RETRIEVE_BY_GENRE_SQL)) {
            final String genreWithWildcardMatch = "%" + formatSingleQuotes(genre) + "%";
            statement.setString(1, genreWithWildcardMatch);
            songs = executeRetrieveAllSongsQuery(statement);
        } catch (final SQLException ex) {
            LOGGER.error("Error retrieving data for genre [{}]", genre, ex);
        }
        return songs;
    }

    public List<Song> retrieveAllSongs() {
        List<Song> allSongs = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(DATABASE_NAME);
             final PreparedStatement retrieveAllSongsQuery = connection.prepareStatement(RETRIEVE_ALL_SONGS)) {
            allSongs = executeRetrieveAllSongsQuery(retrieveAllSongsQuery);
        } catch (final SQLException ex) {
            LOGGER.error("Error retrieving all songs from database", ex);
        }
        return allSongs;
    }

    private List<Song> executeRetrieveAllSongsQuery(final PreparedStatement retrieveAllSongsQuery) {
        final List<Song> songs = new ArrayList<>();
        try (final ResultSet resultSet = retrieveAllSongsQuery.executeQuery()) {
            while (resultSet.next()) {
                Song song = new Song();
                song.setTitle(resultSet.getString("title"));
                song.setArtist(resultSet.getString("artist"));
                song.setAlbum(resultSet.getString("album"));
                song.setGenre(resultSet.getString("genre"));
                song.setFilePath(resultSet.getString("path"));
                songs.add(song);
            }
        } catch (final SQLException ex) {
            LOGGER.error("Error executing retrieveAllSongsQuery [{}]", retrieveAllSongsQuery, ex);
        }
        return songs;
    }
}
