package org.skarb.logpile.vertx.event;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

import java.util.ArrayList;
import java.util.List;

/**
 * Memory reporter of the events.
 * User: skarb
 * Date: 05/01/13
 */
public class MemoryStorage extends AbstractEventMessage {

    public static final String SERVICE_REFRESH = "logpile.resume";
    public static final String ERRORS_FIELD = "totalError";
    public static final String APPLICATIONS_LIST_FIELD = "applications";
    public static final String APP_NAME_FIELD = "name";
    public static final String COMPONENTS_LIST_FIELD = "components";
    public static final String APP_ERRORS_FIELD = "count";
    public static final int DELAY = 5000;
    final List<ErrorApplication> lastApplications = new ArrayList<>();
    /**
     * Refresh service.
     */
    final Handler<Long> handler = new Handler<Long>() {
        @Override
        public void handle(Long event) {
            final JsonArray jsonArray = new JsonArray();

            for (final ErrorApplication ea : lastApplications) {
                jsonArray.addObject(ea.toJson());
            }
            final JsonObject result = new JsonObject().putNumber(ERRORS_FIELD, totalError).putArray(APPLICATIONS_LIST_FIELD, jsonArray);

            getVertx().eventBus().send(SERVICE_REFRESH, result);
        }
    };
    int totalError = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handle(final Event event) {
        totalError++;
        // search for the application in error.
        ErrorApplication errorApplication = null;
        for (final ErrorApplication ea : lastApplications) {
            if (event.getApplication().equals(ea.applicationName)) {
                errorApplication = ea;
                break;
            }
        }
        // If a new application in error
        if (errorApplication == null) {
            // then create a new Entry
            errorApplication = new ErrorApplication();
            errorApplication.applicationName = event.getApplication();
            lastApplications.add(errorApplication);
        }
        // add event to the applictaion.
        errorApplication.add(event.getComponent());


        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String describe() {
        return new StringBuilder("Resume states of the errors since its launch.<br>Delay of the refreshment : ")
                .append(DELAY).append(" ms <br>").toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatas(final Vertx vertx, final Container container) {
        super.setDatas(vertx, container);

        vertx.setPeriodic(DELAY, handler);
    }

    /**
     * bean which contains the resume erros for one application.
     */
    public static class ErrorApplication {
        /**
         * The last 10 components which are in error for the application.
         */
        public final List<String> lastComponents = new ArrayList<>();
        /**
         * the application id.
         */
        public String applicationName;
        /**
         * the number of error for the application.
         */
        public int countError = 0;

        /**
         * add an error.
         *
         * @param component the service in error
         */
        public void add(final String component) {
            if (!lastComponents.contains(component)) {
                if (lastComponents.size() == 10) {
                    lastComponents.remove(lastComponents.size() - 1);
                }
                lastComponents.add(0, component);
            }
            countError++;
        }

        /**
         * transform the   object in Json Object.
         *
         * @return the json object.
         */
        public JsonObject toJson() {
            final JsonArray components = new JsonArray();
            for (final String component : lastComponents) {
                components.addString(component);
            }
            return new JsonObject().putString(APP_NAME_FIELD, applicationName).putArray(COMPONENTS_LIST_FIELD, components)
                    .putNumber(APP_ERRORS_FIELD, countError);
        }

    }
}
