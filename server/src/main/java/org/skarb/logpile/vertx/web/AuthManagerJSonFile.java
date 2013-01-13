/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.skarb.logpile.vertx.web;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

/**
 * Basic Authentication Manager Bus Module<p>
 * Please see the busmods manual for a full description<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class AuthManagerJSonFile extends AbstractAuthManager {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthManagerJSonFile.class);

    public AuthManagerJSonFile() {
    }

    @Override
    protected boolean authenticate(final String username, final String password, final JsonObject config) {

        try {
            final String path = config.getObject(getClass().getName()).getString("path");
            final File file = new File(path);
            final List<String> lines = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
            final StringBuilder allContent = new StringBuilder();
            for (final String line : lines) {
                allContent.append(line).append("\n");
            }
            //C:\skarb\vertx\logpile\src\
            final JsonArray allusers = new JsonArray(allContent.toString());
            for (int i = 0; i < allusers.size(); i++) {
                final JsonObject jsonObject = (JsonObject) allusers.get(i);
                if (username.equals(jsonObject.getString(USERNAME)) &&
                        password.equals(jsonObject.getString(PASSWORD))) {
                    return true;
                }
            }

        } catch (Exception e) {
            logger.error("Error in reading user files.", e);

        }

        return false;
    }

}
