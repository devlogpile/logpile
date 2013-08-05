package org.skarb.logpile.vertx.handler;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.platform.Container;

/**
 * Default Handlers.
 * User: skarb
 * Date: 29/07/13
 */
public class HandlerUtils {
    /**
     * Constructor.
     */
    private HandlerUtils() {
        //NOP
    }

    /**
     * debug log for creating a new file.
     *
     * @param completePath the complete path to the file.
     * @return the creation handler.
     */
    public static AsyncResultHandler<Void> logCreationFile(final Container container, final String completePath) {
        return new AsyncResultHandler<Void>() {
            @Override
            public void handle(AsyncResult<Void> event) {
                container.logger().debug(new StringBuilder("the log file '").append(completePath).append("' is created."));
            }
        };
    }


    public static Handler<AsyncResult<String>> deployVerticle(final Container container, final Class<?> clazz) {
        return new Handler<AsyncResult<String>>() {

            @Override
            public void handle(AsyncResult<String> internName) {
                container.logger().info("deploy verticle " + clazz.getName() +
                        " :" + internName.result());
            }
        };
    }
}
