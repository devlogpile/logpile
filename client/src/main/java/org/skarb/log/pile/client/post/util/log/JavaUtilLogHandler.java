package org.skarb.log.pile.client.post.util.log;

import org.skarb.log.pile.client.event.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.*;

class JavaUtilLogHandler extends Handler {
    /**
     * DEcorator for {@link LogManager}
     *
     * @author trashy
     */
    static class MyLogManager {

        private final LogManager decoration;

        private static MyLogManager getInstance() {
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


    /**
     * Method for formatting the stackTrace.
     *
     * @param exception the exception of the stacktrace.
     * @return the stackTrace Value.
     */
    public static String formatStackTrace(final Throwable exception) {
        String throwable = "";
        if (exception != null) {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            pw.println();
            exception.printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }
        return throwable;
    }

    /**
     * format the service name.
     *
     * @param record the source object.
     * @return the class name or the loggerName.
     */
    private static String formatService(final LogRecord record) {
        String source;
        if (record.getSourceClassName() != null) {
            source = record.getSourceClassName();
            if (record.getSourceMethodName() != null) {
                source += " " + record.getSourceMethodName();
            }
        } else {
            source = record.getLoggerName();
        }
        return source;
    }

    public JavaUtilLogHandler() {
        configure(MyLogManager.getInstance());
    }

    void configure(final MyLogManager manager) {

        String cname = getClass().getName();

        setLevel(manager.getLevelProperty(cname + ".level", Level.INFO));
        setFilter(manager.getFilterProperty(cname + ".filter", null));
        setFormatter(manager.getFormatterProperty(cname + ".format", new SimpleFormatter()));
        try {
            setEncoding(manager.getStringProperty(cname + ".encoding", null));
        } catch (Exception ex) {
            try {
                setEncoding(null);
            } catch (Exception ex2) {
                // doing a setEncoding with null should always work.
                // assert false;
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        try {
            JavaUtilLogData logData = JavaUtilLogData.getInstance();

            final Engine engine = logData.getEngine();
            final Event event = new Event();
            event.setApplication(logData.getApplication());
            event.setDate(new Date(record.getMillis()));
            event.setComponent(formatService(record));
            event.setMessage(getFormatter().formatMessage(record)
            );
            event.setStacktrace(formatStackTrace(record.getThrown()));

            engine.post(event);


        } catch (final Exception e) {
            reportError("LogPile handler failure", e, ErrorManager.GENERIC_FAILURE);
        }
    }


    /**
     * Null Implemntation
     */
    @Override
    public void flush() {

    }

    /**
     * Null Implemntation
     */
    @Override
    public void close() throws SecurityException {

    }


}
