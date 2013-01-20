package org.skarb.log.pile.client.example;

/**
 * User: skarb
 * Date: 17/01/13
 */
public enum TypeLogger {
    LOGBACK {
        @Override
        public Error create() {
            return new LogbackError();
        }
    },

    LOG4J {
        @Override
        public Error create() {
            return new Log4jError();
        }
    },

    JAVA_LOGGING {
        @Override
        public Error create() {
            return new JavaUtilError();
        }
    };

    public abstract Error create();
}
