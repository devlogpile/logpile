package org.skarb.log.pile.client.config;

import org.skarb.log.pile.client.util.NameOfConfigurationParameters;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * the Xml file configuration loader.
 * <p>This class does not use jaxb for not getting the apis / implementation.</p>
 * User: skarb
 * Date: 17/01/13
 */
class XmlFile implements Configuration {

    /**
     * The path of the properties file.
     */
    private static final String FILE_PATH = "/logpile.client.xml";
    /**
     * The pattern for retrieving application name.
     */
    private static final Pattern APPLICATION_PATTERN = Pattern.compile("<" + NameOfConfigurationParameters.PROPERTIES_APPLICATION + "[\\s\\t]*>(.*?)</" + NameOfConfigurationParameters.PROPERTIES_APPLICATION + ">");
    /**
     * The pattern for retrieving engine class.
     */
    private static final Pattern ENGINE_CLASS_PATTERN = Pattern.compile("<" + NameOfConfigurationParameters.PROPERTIES_TYPE + "[\\s\\t]*>(.*?)</" + NameOfConfigurationParameters.PROPERTIES_TYPE + ">");

    /**
     * The pattern for retrieving URL.
     */
    private static final Pattern URL_PATTERN = Pattern.compile("<" + NameOfConfigurationParameters.PROPERTIES_URL_REST + "[\\s\\t]*>(.*?)</" + NameOfConfigurationParameters.PROPERTIES_URL_REST + ">");


    /**
     * Parse the file and retrieve the application name.
     * @param content the file content.
     * @return  null if not found.
     */
    static String retrieveApplication(String content) {
        final Matcher matcher = APPLICATION_PATTERN.matcher(content);
        if(matcher.find()){
            final String application = matcher.group(1);
            return application.trim();
        }
        return null;
    }

    /**
     * Parse the file and retrieve the engine class.
     * @param content the file content.
     * @return  null if not found.
     */
    static String retrieveEngine(String content) {
        final Matcher matcher = ENGINE_CLASS_PATTERN.matcher(content);
        if(matcher.find()){
            final String application = matcher.group(1);
            return application.trim();
        }
        return null;
    }

    /**
     * Parse the file and retrieve the url.
     * @param content the file content.
     * @return  null if not found.
     */
    static String retrieveUrl(String content) {
        final Matcher matcher = URL_PATTERN.matcher(content);
        if(matcher.find()){
            final String application = matcher.group(1);
            return application.trim();
        }
        return null;
    }

    /**
     * the content of the file.
     */
    private String content;
    /**
     * file path.
     */
    private String path;

    /**
     * Default constructor.
     */
    XmlFile() {
        this(FILE_PATH);
    }

    /**
     * Used for test.
     *
     * @param path the path.
     */
    XmlFile(String path) {
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    public boolean configured() {
        try {
            final InputStream resourceAsStream = Thread.currentThread().getClass().getResourceAsStream(path);
            final BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
            final StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            content = stringBuilder.toString().trim();
        } catch (Exception ex) {
            System.err.println("Logpile : Xml file not found");

        }
        return content != null && !content.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public String getApplication() {
        return retrieveApplication(content);
    }


    /**
     * {@inheritDoc}
     */
    public String getEngineClass() {
        return retrieveEngine(content);
    }

    /**
     * {@inheritDoc}
     */
    public String getUrl() {
        return retrieveUrl(content);
    }
}
