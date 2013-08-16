package org.skarb.log.pile.client.post.engine.rest;

import org.skarb.log.pile.client.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;
import org.skarb.log.pile.client.util.LogpileException;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for calling the ws rest service for registering the errors.
 * User: skarb
 * Date: 16/01/13
 */
abstract class AbstractEngineRest implements Engine {

    /**
     * The HttpConnector which call http WS.
     */
    private final HttpConnector connector;
    private final HttpConnector.Method method;
    /**
     * Cache the url value.
     */
    private String url;

    /**
     * Visible for Test
     */
    protected AbstractEngineRest(final HttpConnector connector, final HttpConnector.Method method) {
        this.connector = connector;
        this.method = method;
    }

    /**
     * get current URl or calculate the new.
     *
     * @return the current URl
     */
    public String getUrl() {
        if (url == null) {
            synchronized (this) {
                url = JavaUtilLogData.getInstance().url();
            }
        }
        return url;
    }

    /**
     * reset the calling url.
     */
    public void reset() {
        url = null;
    }

    /**
     * http encode for String
     *
     * @param value the val.
     * @return the val encoding or empty string.
     * @throws LogpileException
     */
    public String encode(final String value) throws LogpileException {
        try {
            if (value != null && !value.trim().isEmpty()) {
                return URLEncoder.encode(value.trim(), HttpConnector.CHARSET);
            }
            return "";
        } catch (final Exception ex) {
            throw new LogpileException(ex);
        }
    }

    /**
     * http encode for Date.
     *
     * @param value the val.
     * @return the val encoding or empty string.
     */
    public String encode(final Date value) {
        if (value != null) {
            return "" + value.getTime();
        }
        return "";
    }

    /**
     * @param event the event
     * @throws LogpileException
     */

    public void post(final Event event) throws LogpileException {


        final Map<String, String> map = new HashMap<String, String>();
        map.put(PROP_APPLICATION, encode(event.getApplication()));
        map.put(PROP_COMPONENT, encode(event.getComponent()));
        map.put(PROP_MESSAGE, encode(event.getMessage()));
        map.put(PROP_STACK, encode(event.getStacktrace()));
        map.put(PROP_DATE, encode(event.getDate()));
        map.put(PROP_SERVER_ID, encode(event.getServerId()));


        if (connector != null ) {

            connector.send(getUrl(), method, map);
        } else {
            throw new LogpileException("connector null");
        }

    }
}
