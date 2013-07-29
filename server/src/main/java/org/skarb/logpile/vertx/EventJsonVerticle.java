package org.skarb.logpile.vertx;

import org.skarb.logpile.vertx.event.EventManagerImpl;
import org.skarb.logpile.vertx.handler.PostParams;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.java.platform.Verticle;



/**
 * Event error register verticle.
 * User: skarb
 * Date: 29/12/12
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class EventJsonVerticle extends Verticle implements Handler<HttpServerRequest> {
    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(EventJsonVerticle.class);
    /**
     * The instance of the manager.
     */
    private EventManager eventmanager;

    /**
     * response method for the return value.
     *
     * @param req the http request.
     */
    static void returnResponse(final HttpServerRequest req) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.putBoolean("result", true);
        req.response().setStatusCode(200);
        req.response().end(jsonObject.encode());
    }

    /**
     * Management of the event creation Url.
     *
     * @param req the request
     */
    @Override
    public void handle(final HttpServerRequest req) {
        logger.debug("req : " + req);


        switch (req.method()) {
            case "GET":
                final MultiMap params = req.params();
                eventmanager.run(params);
                break;
            default:
                req.bodyHandler( new PostParams(eventmanager,getContainer())
                );
               /* req.endHandler(new VoidHandler() {
                    public void handle() {
                        req.expectMultiPart(true);
                        System.out.println("1"+req.params().isEmpty());
                        System.out.println("2"+req.formAttributes().isEmpty());
                         final PostParams bodyHandler = new PostParams(eventmanager);
                            bodyHandler.handle(req);
                    }
                });*/
                break;
        }
        returnResponse(req);
    }

    /**
     * the rest ws for registering the events.
     *
     * @throws Exception
     */
    @Override
    public void start() {
        // configure manager.
        eventmanager = new EventManagerImpl();
        eventmanager.init(getVertx(), getContainer());
        // configure http server
        final HttpServer httpServer = getVertx().createHttpServer();
        final RouteMatcher routeMatcher = new RouteMatcher();
        routeMatcher.allWithRegEx("/event", this);
        httpServer.requestHandler(routeMatcher);
        httpServer.setClientAuthRequired(false);

        // launch rest ws server.
        httpServer.listen(getContainer().config().getInteger("port"));


    }
}
