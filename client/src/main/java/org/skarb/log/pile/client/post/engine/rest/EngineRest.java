package org.skarb.log.pile.client.post.engine.rest;

import org.skarb.log.pile.client.event.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;
import org.skarb.log.pile.client.util.LogpileException;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * First implementation for server communication.
 * <p/>
 * Send an html Message to a http server.
 */
public class EngineRest implements Engine {

    /**
     * The HttpConnector which call http WS.
     */
    private final HttpConnector connector;
    /**
     * Cache the url value.
     */
    private String url;

    /**
     * Visible for Test
     */
    EngineRest(HttpConnector connector) {
        this.connector = connector;
    }

    /**
     * Default Constructor.
     */
    public EngineRest() {
        this(new HttpConnector());
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
     * @throws Exception
     */
    public String encode(final String value) throws LogpileException {
        try{
        if (value != null && !value.trim().isEmpty()) {
            return URLEncoder.encode(value.trim(), HttpConnector.CHARSET);
        }
        return "";
        }catch (final Exception ex){
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
     * @throws Exception
     */
    @Override
    public void post(final Event event) throws LogpileException {


        final Map<String, String> map = new HashMap<>();
        map.put(PROP_APPLICATION, encode(event.getApplication()));
        map.put(PROP_COMPONENT, encode(event.getComponent()));
        map.put(PROP_MESSAGE, encode(event.getMessage()));
        map.put(PROP_STACK, encode(event.getStacktrace()));
        map.put(PROP_DATE, encode(event.getDate()));


        if (connector != null) {
            connector.send(getUrl(), HttpConnector.Method.GET, map);
        } else {
            throw new LogpileException("connector null");
        }

    }
}
