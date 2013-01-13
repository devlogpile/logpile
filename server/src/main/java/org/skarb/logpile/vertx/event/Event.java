package org.skarb.logpile.vertx.event;

import org.vertx.java.core.json.JsonObject;

import java.util.Date;
import java.util.Map;

/**
 * the event Bean.
 * User: skarb
 * Date: 29/12/12
 * Time: 19:19
 */
public class Event {


    /**
     * the property for "application".
     */
    public static final String PROP_APPLICATION = "application";
    /**
     * the property for component.
     */
    public static final String PROP_COMPONENT = "component";
    /**
     * the property for the error message.
     */
    public static final String PROP_MESSAGE = "message";
    /**
     * the property for the "exception".
     */
    public static final String PROP_STACK = "stacktrace";
    /**
     * the property for the "date of the error".
     */
    public static final String PROP_DATE = "date";
    private final JsonObject jsonObject;

    private Event(final JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public static Event Builder(final Map<String, Object> map) {
        return new Event(new JsonObject(map));
    }

    public static Event Builder(final JsonObject jsonObject) {
        return new Event(jsonObject);
    }

    public String getApplication() {
        return jsonObject.getString(PROP_APPLICATION);
    }

    public String getMessage() {
        return jsonObject.getString(PROP_MESSAGE);
    }

    public String getStackTrace() {
        return jsonObject.getString(PROP_STACK);
    }

    public String getComponent() {
        return jsonObject.getString(PROP_COMPONENT);
    }

    public Long getTime() {
        final String string = jsonObject.getString(PROP_DATE);
        if (string == null) {
            return null;
        }
        return Long.valueOf(string);
    }

    public Date toDate() {
        final Long aLong = getTime();
        if (aLong == null) {
            return null;
        }

        return new Date(aLong);
    }
}
