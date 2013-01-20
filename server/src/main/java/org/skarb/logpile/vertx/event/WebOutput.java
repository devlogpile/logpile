package org.skarb.logpile.vertx.event;

import org.skarb.logpile.vertx.event.format.FormatterUtils;
import org.skarb.logpile.vertx.utils.MessageUtils;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Container;

/**
 * send the datas to the web console.
 * User: skarb
 * Date: 20/01/13
 */
public class WebOutput extends AbstractEventMessage {

    public static final String SERVICE_NEW = "logpile.weboutput.new.event";
    public static final String SERVICE_STATUS = "logpile.weboutput.status";
    static final Handler<Message<JsonObject>> STATUS_HANDLER = new Handler<Message<JsonObject>>() {
        @Override
        public void handle(Message<JsonObject> message) {
            MessageUtils.sendOK(message, new JsonObject().putString("address", SERVICE_NEW));
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatas(final Vertx vertx, final Container container) {
        super.setDatas(vertx, container);
        getVertx().eventBus().registerHandler(SERVICE_STATUS, STATUS_HANDLER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handle(final Event event) {
        final JsonObject message = event.toJson();
        final JsonObject returnValue = new JsonObject(message.toString()).putString("formattedDate",
                FormatterUtils.formatDate(FormatterUtils.DEFAULT_FORMAT, event.toDate()));
        getVertx().eventBus().send(SERVICE_NEW, returnValue);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String describe() {
        return "Web output console";
    }
}
