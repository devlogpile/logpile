package org.skarb.logpile.vertx.utils;

import java.nio.charset.Charset;

/**
 * Charsets utils.
 * User: skarb
 * Date: 15/01/13
 */
public final class Charsets {

    /**
     * the value of the default CHARSET.
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * Get the default charset instance.
     * @return the charset.
     */
    public static Charset getDefault() {
        return Charset.forName("UTF-8");
    }

    /**
     * Constructor.
     */
    private Charsets() {
    }



}
