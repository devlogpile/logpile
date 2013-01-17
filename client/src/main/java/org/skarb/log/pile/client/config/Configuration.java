package org.skarb.log.pile.client.config;

/**
 * Get the configuration parameters used by logpile client framework.
 * User: skarb
 * Date: 17/01/13
 */
public interface Configuration {

    /**
     * Test if the configuration retriever can be used.
     *
     * @return true if is acive.
     */
    public boolean configured();

    /**
     * Get the application Name.
     *
     * @return the name.
     */
    public String getApplication();

    /**
     * get the name of the class used for calling registering server.
     *
     * @return the class name.
     */
    public String getEngineClass();

    /**
     * Get The url of the server.
     *
     * @return The url of the server.
     */
    public String getUrl();
}
