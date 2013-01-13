package org.skarb.logpile.vertx.handler;

import org.vertx.java.core.Handler;
import org.vertx.java.deploy.Container;

/**
 * the simple handler for the deployement.
 * User: skarb
 * Date: 29/12/12
 * Time: 18:58
 */
public class DeployVerticle implements Handler<String> {

    private final Class<?> clazz;
    private final Container container;

    public DeployVerticle(final Container cont, final Class<?> cla) {
        this.clazz = cla;
        this.container = cont;
    }

    public void handle(String verticleName) {
        container.getLogger().info("deploy verticle " + clazz.getName() +
                " :" + verticleName);


    }
}
