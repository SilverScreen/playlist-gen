package com.william.playlistgen.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to contain properties for PlaylistGen app.
 */
public class PlaylistGenProperties {

    private final Properties properties;

    public PlaylistGenProperties(final String propertiesFileName) {
        properties = new Properties();
        final File propertiesFile = new File(propertiesFileName);
        try (final InputStream inputStream = new FileInputStream(propertiesFile)) {
            properties.load(inputStream);
        } catch (final IOException e) {
            System.out.println("File " + propertiesFileName + " not found." + e.getMessage());
        }
    }

    public String getProperty(final String key) {
        return properties.getProperty(key);
    }

}
