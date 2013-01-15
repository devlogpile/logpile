package org.skarb.logpile.vertx.handler;

import org.vertx.java.core.Handler;
import org.vertx.java.deploy.Container;

/**
 * the simple handler for the deployement which write the logs.
 * User: skarb
 * Date: 29/12/12
 *
 */
public class DeployVerticle implements Handler<String> {
    /**
     * The class of the verticle.
     */
    private final Class<?> clazz;
    /**
     * the current contaienr.
     */
    private final Container container;

    /**
     * Constructor.
     * @param cont the current container.
     * @param cla the class of the verticle.
     */
    public DeployVerticle(final Container cont, final Class<?> cla) {
        this.clazz = cla;
        this.container = cont;
    }

    /**
     * the handle method
     * @param internName the intern name of the verticle.
     */
    @Override
    public void handle(String internName) {
        container.getLogger().info("deploy verticle " + clazz.getName() +
                " :" + internName);


    }
}
