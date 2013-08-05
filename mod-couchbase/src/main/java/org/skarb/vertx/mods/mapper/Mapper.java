package org.skarb.vertx.mods.mapper;

import com.couchbase.client.protocol.views.ViewResponse;
import org.vertx.java.core.json.JsonArray;

/**
 * User: skarb
 * Date: 30/07/13
 */
public interface Mapper {
    /**
     * Map the result to the JsonArray.
     * @param viewResponse
     * @return
     */
    JsonArray map(final ViewResponse viewResponse);
}
