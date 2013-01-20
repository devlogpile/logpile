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
    /**
     * the current object.
     */
    private final JsonObject jsonObject;

    /**
     * COnstructor.
     *
     * @param jsonObject the json object.
     */
    private Event(final JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * Transform an map to Event.
     *
     * @param map the map
     * @return the instance.
     */
    public static Event Builder(final Map<String, Object> map) {
        return new Event(new JsonObject(map));
    }

    /**
     * Transform an Json object to Event.
     *
     * @param jsonObject the object to trasnform.
     * @return the instance.
     */
    public static Event Builder(final JsonObject jsonObject) {
        return new Event(jsonObject);
    }

    /**
     * Getter on application value.
     *
     * @return application value.
     */
    public String getApplication() {
        return jsonObject.getString(PROP_APPLICATION);
    }

    /**
     * Getter on message value.
     *
     * @return message value.
     */
    public String getMessage() {
        return jsonObject.getString(PROP_MESSAGE);
    }

    /**
     * Getter on stackTrace value.
     *
     * @return stackTrace value.
     */
    public String getStackTrace() {
        return jsonObject.getString(PROP_STACK);
    }

    /**
     * Getter on component value.
     *
     * @return component value.
     */
    public String getComponent() {
        return jsonObject.getString(PROP_COMPONENT);
    }

    /**
     * Getter on date of the error.
     *
     * @return date of the error.
     */
    public Long getTime() {
        final String string = jsonObject.getString(PROP_DATE);
        if (string == null) {
            return null;
        }
        return Long.valueOf(string);
    }

    /**
     * Getter on date of the error.
     *
     * @return date of the error.
     */
    public Date toDate() {
        final Long aLong = getTime();
        if (aLong == null) {
            return null;
        }

        return new Date(aLong);
    }

    public JsonObject toJson(){
        return jsonObject;
    }
}
