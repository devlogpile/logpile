package org.skarb.logpile.vertx;

import org.skarb.logpile.vertx.handler.DeployVerticle;
import org.skarb.logpile.vertx.web.LogpileWeb;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;


/**
 * Main verticle.
 * <p>it launchs all verticles.</p>
 * User: skarb
 * Date: 29/12/12
 */
public class MainVerticle extends Verticle {

    public static final String INSTANCE_FIELD = "instance";
    public static final String EVENT_JSON = "event_json";
    public static final String LOG_PILE_WEB = "log_pile_web";
    public static final String ACTIVE_FIELD = "active";

    /**
     * start all the services.
     *
     * @throws Exception
     */
    public void start() throws Exception {
        final JsonObject config = getContainer().getConfig();
        getContainer().getLogger().debug("config :" + config);

        final JsonObject configEvents = config.getObject(EVENT_JSON);
        getContainer().deployVerticle(EventJsonVerticle.class.getName(), configEvents, configEvents.getInteger(INSTANCE_FIELD),
                new DeployVerticle(getContainer(), EventJsonVerticle.class));
        final JsonObject configLogpileWeb = config.getObject(LOG_PILE_WEB);
        // TEST if the server must launch the web interface.
        if (Boolean.TRUE.equals(configLogpileWeb.getBoolean(ACTIVE_FIELD))) {
            getContainer().deployVerticle(LogpileWeb.class.getName(), config, 1,
                    new DeployVerticle(getContainer(), LogpileWeb.class));
        }

    }
}
