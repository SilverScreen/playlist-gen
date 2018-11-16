package com.william.playlistgen.exception;

/**
 * @author William
 */
public class DirectoryNotFoundException extends Exception {

    public DirectoryNotFoundException(final String message, final Throwable ex) {
        super(message, ex);
    }
}
