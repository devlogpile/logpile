package org.skarb.logpile.vertx.utils;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

/**
 * util class for managing event bus response.
 * User: skarb
 * Date: 04/01/13
 */
public final class MessageUtils {
    /**
     * the name of status field.
     */
    public static final String STATUS_FIELD = "status";
    /**
     * the name of error field.
     */
    public static final String ERROR_STATUS = "error";
    /**
     * the name of result field.
     */
    public static final String RESULT_FIELD = "result";
    /**
     * the name of message field.
     */
    public static final String MESSAGE_FIELD = "message";
    /**
     * the value of the status field when it's OK.
     */
    public static final String OK_STATUS = "ok";
    /**
     * the value of the status field when it's denied.
     * <p>just use with the {@link org.skarb.logpile.vertx.web.AbstractAuthManager}. </p>
     */
    public static final String DENIED_STATUS = "denied";
    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);

    /**
     * COnstructor.
     */
    private MessageUtils() {
    }

    /**
     * add an a status to a reply.
     *
     * @param status the value of the status.
     * @param message the message.
     */
    public static void sendStatus(final String status, final Message<JsonObject> message) {
        sendStatus(status, message, null);
    }

    /**
     * add an a status to a reply.
     *
     * @param status the value of the status.
     * @param message the message.
     * @param json the others datas to send in reply.
     */
    public static void sendStatus(final String status, final Message<JsonObject> message, JsonObject json) {
        if (json == null) {
            json = new JsonObject();
        }
        json.putString(STATUS_FIELD, status);
        json.putBoolean(RESULT_FIELD, OK_STATUS.equals(status));
        message.reply(json);
    }

    /**
     * Send an ok response.
     *
     * @param message the message.
     * @param json the others datas.
     */
    public static void sendOK(final Message<JsonObject> message, final JsonObject json) {
        sendStatus(OK_STATUS, message, json);
    }

    /**
     * get a rewquired field from an json object.
     *
     * @param field the field to retreive.
     * @param message the message which contains the  data
     * @return the value or an exception.
     */
    public static String getMandatoryString(final String field, final Message<JsonObject> message) {
        String val = message.body().getString(field);
        if (val == null) {
            sendError(message, field + " must be specified");
        }
        return val;
    }

    /**
     * get a rewquired field from an json object.
     *
     * @param field the field to retreive.
     * @param message the message which contains the  data
     * @return the value or an exception.
     */
    public static Boolean getMandatoryBoolean(final String field, final Message<JsonObject> message) {
        Boolean val = message.body().getBoolean(field);
        if (val == null) {
            sendError(message, field + " must be specified");
        }
        return val;
    }
    /**
     * send an error message.
     *
     * @param message the message to reply.
     * @param error the error message
     */
    public static void sendError(Message<JsonObject> message, String error) {
        sendError(message, error, null);
    }

    /**
     * send an error message.
     *
     * @param message the message to reply.
     * @param error the error message
     * @param e the attached exception.
     */
    public static void sendError(Message<JsonObject> message, String error, Exception e) {
        logger.error(error, e);
        JsonObject json = new JsonObject().putString(STATUS_FIELD, ERROR_STATUS).putString(MESSAGE_FIELD, error);
        message.reply(json);
    }

}
