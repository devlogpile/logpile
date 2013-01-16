package org.skarb.log.pile.client.post.util.log;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * DEcorator for {@link java.util.logging.LogManager}
 *
 * @author trashy
 */
public class MyLogManager {

    private final LogManager decoration;

    static MyLogManager getInstance() {
        return new MyLogManager(LogManager.getLogManager());
    }

    MyLogManager(final LogManager decoration) {
        super();
        this.decoration = decoration;
    }



    Level getLevelProperty(String name, Level defaultValue) {
        String val = decoration.getProperty(name);
        if (val == null) {
            return defaultValue;
        }
        try {
            return Level.parse(val.trim());
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    @SuppressWarnings("rawtypes")
    Filter getFilterProperty(String name, Filter defaultValue) {
        String val = decoration.getProperty(name);
        try {
            if (val != null) {
                Class clz = ClassLoader.getSystemClassLoader().loadClass(val);
                return (Filter) clz.newInstance();
            }
        } catch (Exception ex) {
            // We got one of a variety of exceptions in creating the
            // class or creating an instance.
            // Drop through.
        }
        // We got an exception.  Return the defaultValue.
        return defaultValue;
    }

    @SuppressWarnings("rawtypes")
    Formatter getFormatterProperty(String name, Formatter defaultValue) {
        String val = decoration.getProperty(name);
        try {
            if (val != null) {
                Class clz = ClassLoader.getSystemClassLoader().loadClass(val);
                return (Formatter) clz.newInstance();
            }
        } catch (Exception ex) {
            // We got one of a variety of exceptions in creating the
            // class or creating an instance.
            // Drop through.
        }
        // We got an exception.  Return the defaultValue.
        return defaultValue;
    }

    String getStringProperty(String name, String defaultValue) {
        String val = decoration.getProperty(name);
        if (val == null) {
            return defaultValue;
        }
        return val.trim();
    }

}