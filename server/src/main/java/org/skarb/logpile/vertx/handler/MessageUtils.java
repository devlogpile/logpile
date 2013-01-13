package org.skarb.logpile.vertx.handler;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

/**
 * User: skarb
 * Date: 04/01/13
 */
public class MessageUtils {

    public static final String STATUS_FIELD = "status";
    public static final String ERROR_STATUS = "error";
    public static final String RESULT_FIELD = "result";
    public static final String OK_VALUE = "ok";
    public static final String MESSAGE_FIELD = "message";
    public static final String DENIED_STATUS = "denied";
    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);

    private MessageUtils() {
    }

    public static void sendStatus(String status, Message<JsonObject> message) {
        sendStatus(status, message, null);
    }

    public static void sendStatus(String status, Message<JsonObject> message, JsonObject json) {
        if (json == null) {
            json = new JsonObject();
        }
        json.putString(STATUS_FIELD, status);
        json.putBoolean(RESULT_FIELD, OK_VALUE.equals(status));
        message.reply(json);
    }

    public static void sendOK(Message<JsonObject> message, JsonObject json) {
        sendStatus(OK_VALUE, message, json);
    }

    public static String getMandatoryString(String field, Message<JsonObject> message) {
        String val = message.body.getString(field);
        if (val == null) {
            sendError(message, field + " must be specified");
        }
        return val;
    }

    public static void sendError(Message<JsonObject> message, String error) {
        sendError(message, error, null);
    }

    public static void sendError(Message<JsonObject> message, String error, Exception e) {
        logger.error(error, e);
        JsonObject json = new JsonObject().putString(STATUS_FIELD, ERROR_STATUS).putString(MESSAGE_FIELD, error);
        message.reply(json);
    }
}
