package org.skarb.logpile.vertx.handler;

import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.skarb.logpile.vertx.EventManager;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: skarb
 * Date: 03/01/13
 */
public class PostParams implements Handler<Buffer> {

    public static final String CHARSET = "UTF-8";

    private final EventManager eventManager;


    public PostParams(final EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void handle(final Buffer buff) {
        if (buff != null) {
            final Map<String, String> params = new HashMap<>();
            final String string = buff.toString(CHARSET);

            QueryStringDecoder queryStringDecoder = new QueryStringDecoder("http://localhost:80/event?"+string);

            for (final Map.Entry<String, List<String>> tmp : queryStringDecoder.getParameters().entrySet()) {
                if (!tmp.getValue().isEmpty()) {
                    params.put(tmp.getKey(), tmp.getValue().get(0));
                }
            }
            eventManager.run(params);
        }
    }



}
