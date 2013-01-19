package org.skarb.log.pile.client.example;

/**
 * User: skarb
 * Date: 17/01/13
 */
public enum TypeLogger {
    LOG4J {
        public Error create(String message, String exception, String messageException){
            return new Log4jError(message, exception, messageException);
        }

        @Override
        public Error create() {
            return new Log4jError();
        }
    },

    JAVA_LOGGING {
        public Error create(String message, String exception, String messageException){
           return new JavaUtilError(message, exception, messageException);
        }

        @Override
        public Error create() {
            return new JavaUtilError();
        }
    };



    public abstract Error create(String message, String exception, String messageException);
    public abstract Error create();
}
