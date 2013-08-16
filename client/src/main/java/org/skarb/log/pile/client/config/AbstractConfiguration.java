package org.skarb.log.pile.client.config;

import java.net.InetAddress;

/**
 * Abstract class for the configuration.
 * <p/>
 * User: skarb
 * Date: 09/08/13
 */
public abstract class AbstractConfiguration implements Configuration {

    private String serverId;

    /**
     * The specific getter for retreive the serverId.
     *
     * @return the value.
     */
    public abstract String serverId();

    /**
     * {@inheritDoc}
     */
    public String getServerId() {
        if (serverId == null) {
            synchronized (this) {
                serverId = getMandatoryServerId(serverId());
            }
        }

        return serverId;
    }

    /**
     * return the specific value or the server hostname.
     * <p>Visible for test</p>
     *
     * @param value the specific value
     * @return the server id.
     */
    String getMandatoryServerId(String value) {
        if (value != null) {
            return value;
        }
        String result = "";
        try {
            final InetAddress inetAddress = InetAddress.getLocalHost();
            result = inetAddress.getHostName();
        } catch (Exception ex) {
            System.err.println("Impossible to retrieve the hostname of the server");
            ex.printStackTrace(System.err);
        }


        return result;
    }
}
