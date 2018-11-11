package com.william.playlistgen.database;

/**
 * @author William
 */
public class LibrarySqlStatements {
    private static final String TABLE_NAME = "MyMusic";
    static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
    static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + "("
            + "title    TEXT    NOT NULL,"
            + "artist   TEXT    NOT NULL,"
            + "album    TEXT    NOT NULL,"
            + "path     TEXT    NOT NULL)";
    static final String INSERT_SONG_SQL = "INSERT INTO " + TABLE_NAME + "(title, artist, album, path) VALUES (?,?,?,?)";
    static final String RETRIEVE_BY_ARTIST_SQL = "SELECT * FROM " + TABLE_NAME + " WHERE LOWER(artist) = LOWER(?)";
    static final String RETRIEVE_BY_ALBUM_SQL = "SELECT * FROM " + TABLE_NAME
            + " WHERE LOWER(artist) = LOWER(?) AND LOWER(album) = LOWER(?)";
    static final String RETRIEVE_BY_SONG_SQL = "SELECT * FROM " + TABLE_NAME
            + " WHERE LOWER(artist) = LOWER(?) AND LOWER(title) = LOWER(?)";
    static final String RETRIEVE_ALL_SONGS = "SELECT * FROM " + TABLE_NAME;

}
