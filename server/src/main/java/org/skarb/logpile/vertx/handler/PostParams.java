package org.skarb.logpile.vertx.handler;

import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.skarb.logpile.vertx.EventManager;
import org.skarb.logpile.vertx.utils.Charsets;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler for retreiving post parameter of an post http request.
 * <p>Use for receiving EVent by the event manager.</p>
 * User: skarb
 * Date: 03/01/13
 */
public class PostParams implements Handler<Buffer> {

    /**
     * Dumb prefixe of an http request;
     * <p>For parsing the parameters and use the same class parser than http request parameters ({@link QueryStringDecoder }) </p>
     */
    public static final String DUMB_PREFIXE = "http://localhost:80/event?";
    /**
     * The current instance.
     */
    private final EventManager eventManager;

    /**
     * Constructor.
     *
     * @param eventManager current instance.
     */
    public PostParams(final EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Parse the buffer to retreive parapmeters and treat them.
     *
     * @param buff the datas.
     */
    @Override
    public void handle(final Buffer buff) {
        if (buff != null) {
            final Map<String, String> params = new HashMap<>();
            // get datas
            final String string = buff.toString(Charsets.DEFAULT_CHARSET);
            // parse the request
            final QueryStringDecoder queryStringDecoder = new QueryStringDecoder(DUMB_PREFIXE + string);
            // create the return object
            for (final Map.Entry<String, List<String>> tmp : queryStringDecoder.getParameters().entrySet()) {
                if (!tmp.getValue().isEmpty()) {
                    params.put(tmp.getKey(), tmp.getValue().get(0));
                }
            }
            // treat.
            eventManager.run(params);
        }
    }


}
