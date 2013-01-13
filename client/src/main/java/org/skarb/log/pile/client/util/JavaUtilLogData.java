package org.skarb.log.pile.client.util;

import org.skarb.log.pile.client.post.engine.Engine;

import java.io.Serializable;
import java.util.Random;

/**
 * Utils for the properties management.
 */
public class JavaUtilLogData implements Serializable {

    /**
     * Serial Number.
     */
    private static final long serialVersionUID = 2938442916558043005L;
    /**
     * Singleton.
     */
    private static final JavaUtilLogData instance = new JavaUtilLogData();
    /**
     * Current Application ID.
     */
    private String application;
    /**
     * Current Engine implementation.
     */
    private Engine engine;

    /**
     * Constructor.
     */
    private JavaUtilLogData() {

    }

    /**
     * Factory for the singleton.
     *
     * @return the singleton.
     */
    public static JavaUtilLogData getInstance() {
        if (instance.isNotConfigure()) {
            synchronized (instance) {
                instance.configure();
            }

        }
        return instance;
    }


    /**
     * init the datas.
     */
    private void configure() {
        try {
            final String property = System.getProperty(ClientConstantes.PROPERTIES_APPLICATION);
            if (property != null && property.length() > 0) {
                application = property;
            } else {
                final Random randomNumberGenerator = new Random();
                application = String.valueOf(randomNumberGenerator.nextLong()) + "/generated";

            }

            String engineClass = System.getProperty(ClientConstantes.PROPERTIES_TYPE);

            if (engineClass == null) {
                engineClass = ClientConstantes.DEFAULT_ENGINE;
            }

            engine = (Engine) Class.forName(engineClass).newInstance();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * Test if the singleton is configured.
     *
     * @return true if it is configired.
     */
    private boolean isNotConfigure() {
        return application == null;
    }

    public String url() {
        return System.getProperty(ClientConstantes.PROPERTIES_URL_REST);
    }

    /**
     * Getter on application.
     *
     * @return the application.
     */
    public String getApplication() {
        return application;
    }

    /**
     * Setter on application.
     *
     * @param application the application.
     */
    public void setApplication(final String application) {
        this.application = application;
    }

    /**
     * Getter on Engine.
     *
     * @return the engine.
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Setter on Engine.
     *
     * @param engine the engine.
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
    }


}
