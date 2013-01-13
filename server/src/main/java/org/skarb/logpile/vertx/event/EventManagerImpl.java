package org.skarb.logpile.vertx.event;

import org.skarb.logpile.vertx.EventManager;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of ImportManager.
 * User: skarb
 * Date: 03/01/13
 */
public class EventManagerImpl implements EventManager,Handler<Message<JsonArray>> {
    /**
     * Field name for the events Handler.
     */
    static final String FIELD_SERVICES = "services";
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(EventManagerImpl.class);
    /**
     * current vertx instance.
     */
    private Vertx vertx;
    /**
     * service register List.
     */
    private List<AbstractEventMessage> serviceList = new ArrayList<>();

    @Override
    public void init(final Vertx vertx, final JsonObject config) {
        this.vertx = vertx;
        final JsonArray array = config.getArray(FIELD_SERVICES);
        if (array == null) {
            return;
        }
        for (int i = 0; i < array.size(); i++) {

            try {
                final String clazz = array.get(i).toString();
                final AbstractEventMessage abstractEventMessage = (AbstractEventMessage) Class.forName(array.get(i).toString()).newInstance();
                abstractEventMessage.setVertx(vertx);
                vertx.eventBus().registerHandler(clazz, abstractEventMessage);
                serviceList.add(abstractEventMessage);
            } catch (Exception e) {
                log.error("Error in registering event Handler", e);
            }
        }

        vertx.eventBus().registerHandler(SERVICE_STATE,this);
    }



    @Override
    public void handle(final Message<JsonArray> message) {
        final JsonArray array = new JsonArray();
        for (final AbstractEventMessage evtMessage : serviceList){
            array.addObject(new JsonObject()
                    .putString("name",evtMessage.getClass().getName())
                    .putString("describe",evtMessage.describe())
                    .putBoolean("active",evtMessage.isActive()));
        }
        message.reply(array);
    }

    @Override
    public void run(final Map<String, String> params) {

        final JsonObject jsonObject = new JsonObject();
        for (final Map.Entry<String, String> entry : params.entrySet()) {
            jsonObject.putString(entry.getKey(), entry.getValue());
        }
        for (final AbstractEventMessage adress : serviceList) {
            if (adress.isActive()) {
                final String name = adress.getClass().getName();
                vertx.eventBus().send(name, jsonObject, new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(final Message<JsonObject> message) {
                        log.debug(name + " treatment : " + message.body.getBoolean(AbstractEventMessage.RESULT));
                    }
                });
            }
        }
    }

    /**
     * Getter on serviceList.
     *
     * @return the current instance.
     */
    public List<AbstractEventMessage> getServiceList() {
        return serviceList;
    }

    void setServiceList(List<AbstractEventMessage> serviceList) {
        this.serviceList = serviceList;
    }
}
