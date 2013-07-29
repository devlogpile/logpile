package org.skarb.logpile.vertx.handler;


import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.skarb.logpile.vertx.EventManager;
import org.skarb.logpile.vertx.utils.Charsets;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.impl.CaseInsensitiveMultiMap;
import org.vertx.java.platform.Container;

import java.net.URLDecoder;
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
    public static final Splitter.MapSplitter SPLITTER_ATTRIBUTE = Splitter.on('&').withKeyValueSeparator("=");
    /**
     * The current instance.
     */
    private final EventManager eventManager;
    private final Container container;

    /**
     * Constructor.
     *
     * @param eventManager current instance.
     */
    public PostParams(final EventManager eventManager, final Container container) {
        this.eventManager = eventManager;
        this.container = container;
    }

    /**
     * Parse the buffer to retreive parapmeters and treat them.
     *
     * @param buff the datas.
     */
    @Override
    public void handle(final Buffer buff) {
        final CaseInsensitiveMultiMap params = new CaseInsensitiveMultiMap();
        final String sequence = buff.toString(Charsets.DEFAULT_CHARSET);
        if (!Strings.isNullOrEmpty(sequence)) {
            final Map<String, String> attributes = SPLITTER_ATTRIBUTE.split(sequence);
            //params.add(attributes);
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                try {
                    final String value = entry.getValue();
                    final String decode = URLDecoder.decode(value, Charsets.DEFAULT_CHARSET);
                    params.add(entry.getKey(), decode);
                } catch (Exception ex) {
                    container.logger().error("error decoder " + entry);
                }
            }

        }
        eventManager.run(params);

    }


}
