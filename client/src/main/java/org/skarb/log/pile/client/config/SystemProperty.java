package org.skarb.log.pile.client.config;

import org.skarb.log.pile.client.util.NameOfConfigurationParameters;

/**
 * Search in the System Properties of the Jvm for the configuration.
 * <p>The default implementation if others are not configured.</p>
 * User: skarb
 * Date: 17/01/13
 */
class SystemProperty extends AbstractConfiguration implements Configuration {

    /**
     * Constructor.
     */
    public SystemProperty() {
    }

    /**
     * {@inheritDoc}
     */
    public String getApplication() {
        return System.getProperty(NameOfConfigurationParameters.PROPERTIES_APPLICATION);
    }

    /**
     * {@inheritDoc}
     */
    public String getEngineClass() {
        return System.getProperty(NameOfConfigurationParameters.PROPERTIES_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    public String getUrl() {
        return System.getProperty(NameOfConfigurationParameters.PROPERTIES_URL_REST);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String serverId() {
        return System.getProperty(NameOfConfigurationParameters.PROPERTIES_SERVER_ID);
    }

    /**
     * {@inheritDoc}
     */
    public boolean configured() {
        return true;
    }
}
