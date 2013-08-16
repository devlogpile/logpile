package org.skarb.log.pile.client.config;

import org.skarb.log.pile.client.util.NameOfConfigurationParameters;

import java.io.InputStream;
import java.util.Properties;

/**
 * COnfiguration by Properties file.
 * User: skarb
 * Date: 17/01/13
 */
class PropertiesFile extends AbstractConfiguration implements Configuration {
    /**
     * The path of the properties file.
     */
    public static final String FILE_PATH = "/logpile.client.properties";
    /**
     * The properties of the file.
     */
    private Properties properties;
    /**
     * File path.
     */
    private final String path;

    /**
     * Used for test.
     * @param path  the path.
     */
    PropertiesFile(String path) {
        this.path = path;
    }

    /**
     * Default Constructor.
      */
    PropertiesFile() {
        this(FILE_PATH);
    }

    /**
     * {@inheritDoc}
     */
    public boolean configured() {
        try {
            final InputStream resourceAsStream = Thread.currentThread().getClass().getResourceAsStream(path);
            properties = new Properties();
            properties.load(resourceAsStream);
        } catch (Exception ex) {
            System.err.println("Logpile : Properties file not found");
        }
        return properties != null && !properties.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public String getApplication() {
        return properties.getProperty(NameOfConfigurationParameters.PROPERTIES_APPLICATION);
    }

    /**
     * {@inheritDoc}
     */
    public String getEngineClass() {
        return properties.getProperty(NameOfConfigurationParameters.PROPERTIES_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    public String getUrl() {
        return properties.getProperty(NameOfConfigurationParameters.PROPERTIES_URL_REST);
    }

    public String serverId(){
        return properties.getProperty(NameOfConfigurationParameters.PROPERTIES_SERVER_ID);
    }
}
