package org.skarb.logpile.vertx.handler;

import org.skarb.logpile.vertx.EventManager;
import org.skarb.logpile.vertx.event.AbstractEventMessage;
import org.skarb.logpile.vertx.utils.MessageUtils;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

/**
 * activate or desactivate an event service.
 * User: skarb
 * Date: 15/01/13
 */
public final class ChangeStateService implements Handler<Message<JsonObject>> {

    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(ChangeStateService.class);
    public static final String NAME_FIELD = "name";
    public static final String ACTIVATE_FIELD = "activate";
    /**
     * the current instance.
     */
    private final EventManager eventManager;

    /**
     * Constructor.
     * @param eventManager
     */
    public ChangeStateService(final EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * activate or desactivate an service.
     * @param message
     */
    @Override
    public void handle(final Message<JsonObject> message) {

        final String applicationName = MessageUtils.getMandatoryString(NAME_FIELD, message);
        final Boolean activate = MessageUtils.getMandatoryBoolean(ACTIVATE_FIELD, message);
        if (activate == null || applicationName == null) {
            return;
        }
        // find the srervice
        AbstractEventMessage service = null;
        for (final AbstractEventMessage abstractEventMessage : eventManager.getServiceList()) {
            if (applicationName.equals(abstractEventMessage.getClass().getName())) {
                service = abstractEventMessage;
            }
        }

        if (service != null) {
            service.setActive(activate);
            MessageUtils.sendOK(message, null);
        } else {
            MessageUtils.sendError(message, new StringBuilder(applicationName).append(" not found").toString());
        }
    }
}
