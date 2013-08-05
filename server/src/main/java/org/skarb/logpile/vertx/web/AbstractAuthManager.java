package org.skarb.logpile.vertx.web;

import org.skarb.logpile.vertx.utils.MessageUtils;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.java.platform.Verticle;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract Class for authentification Management.
 * User: skarb
 * Date: 06/01/13
 */
abstract class AbstractAuthManager extends Verticle {
    public static final String SESSION_TIMEOUT_FIELD = "session_timeout";
    static final String ADDRESS = "auth-logpile";
    static final long DEFAULT_SESSION_TIMEOUT = 30 * 60 * 1000;
    static final String USERNAME = "username";
    static final String PASSWORD = "password";
    static final String SESSION_ID = "sessionID";
    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthManagerJSonFile.class);
    private final Map<String, String> sessions = new HashMap<>();
    private final Map<String, LoginInfo> logins = new HashMap<>();
    Handler<Message<JsonObject>> loginHandler = new Handler<Message<JsonObject>>() {
        public void handle(Message<JsonObject> message) {
            doLogin(message);
        }
    };
    Handler<Message<JsonObject>> logoutHandler = new Handler<Message<JsonObject>>() {
        public void handle(Message<JsonObject> message) {
            doLogout(message);
        }
    };
    Handler<Message<JsonObject>> authoriseHandler = new Handler<Message<JsonObject>>() {
        public void handle(Message<JsonObject> message) {
            doAuthorise(message);
        }
    };
    private long sessionTimeout = DEFAULT_SESSION_TIMEOUT;

    /**
     * Start the busmod
     */
    public void start() {
        final EventBus eb = getVertx().eventBus();
        final JsonObject config = getContainer().config();


        final Number timeout = config.getNumber(SESSION_TIMEOUT_FIELD);
        if (timeout != null) {
            this.sessionTimeout = timeout.longValue();
        }


        eb.registerHandler(ADDRESS + ".login", loginHandler);

        eb.registerHandler(ADDRESS + ".logout", logoutHandler);

        eb.registerHandler(ADDRESS + ".authorise", authoriseHandler);
    }

    void doLogin(final Message<JsonObject> message) {

        final String username = MessageUtils.getMandatoryString(USERNAME, message);
        if (username == null) {
            return;
        }
        final String password = MessageUtils.getMandatoryString(PASSWORD, message);
        if (password == null) {
            return;
        }
        boolean resultatAuthent = authenticate(username, password, getContainer().config());


        if (resultatAuthent) {
            // Check if already logged in, if so logout of the old session
            final LoginInfo info = logins.get(username);
            if (info != null) {
                logout(info.sessionID);
            }
            // Found
            final String sessionID = UUID.randomUUID().toString();
            final long timerID = getVertx().setTimer(sessionTimeout, new Handler<Long>() {
                public void handle(Long timerID) {
                    sessions.remove(sessionID);
                    logins.remove(username);
                }
            });
            sessions.put(sessionID, username);
            logins.put(username, new LoginInfo(timerID, sessionID));
            JsonObject jsonReply = new JsonObject().putString("sessionID", sessionID);
            MessageUtils.sendOK(message, jsonReply);
        } else {
            logger.error("Failed to execute login query: " + message.body());
            MessageUtils.sendStatus("denied", message);
        }


    }

    /**
     * Method for the authentification.
     *
     * @param username the name.
     * @param password the password.
     * @param config the configuration.
     * @return true if the authentification is OK;
     */
    protected abstract boolean authenticate(final String username, final String password, final JsonObject config);

    /**
     * Logout the session of the authentifcation;
     *
     * @param message the datas.
     */
    void doLogout(final Message<JsonObject> message) {
        final String sessionID = MessageUtils.getMandatoryString("sessionID", message);
        if (sessionID != null) {
            if (logout(sessionID)) {
                MessageUtils.sendOK(message, null);
            } else {
                MessageUtils.sendError(message, "Not logged in");
            }
        }
    }

    private boolean logout(String sessionID) {
        String username = sessions.remove(sessionID);
        if (username != null) {
            LoginInfo info = logins.remove(username);
            getVertx().cancelTimer(info.timerID);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verify the authorization.
     *
     * @param message the datas to verify.
     */
    void doAuthorise(Message<JsonObject> message) {
        final String sessionID = MessageUtils.getMandatoryString(SESSION_ID, message);
        if (sessionID == null) {
            MessageUtils.sendStatus(MessageUtils.DENIED_STATUS, message);
            return;
        }
        final String username = sessions.get(sessionID);

        // In this basic auth manager we don't do any resource specific authorisation
        // The user is always authorised if they are logged in

        if (username != null) {
            JsonObject reply = new JsonObject().putString("username", username);
            MessageUtils.sendOK(message, reply);
        } else {
            MessageUtils.sendStatus(MessageUtils.DENIED_STATUS, message);
        }
    }

    long getSessionTimeout() {
        return sessionTimeout;
    }

    private static final class LoginInfo {
        final long timerID;
        final String sessionID;

        private LoginInfo(long timerID, String sessionID) {
            this.timerID = timerID;
            this.sessionID = sessionID;
        }
    }
}
