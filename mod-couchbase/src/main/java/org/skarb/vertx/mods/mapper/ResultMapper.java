package org.skarb.vertx.mods.mapper;

import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * User: skarb
 * Date: 30/07/13
 */
public class ResultMapper implements Mapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray map(final ViewResponse viewResponse) {
        JsonArray results = new JsonArray();
        for (final ViewRow row : viewResponse) {
            final JsonObject value = new JsonObject();
            value.putString("id", row.getId());
            value.putString("key", row.getKey());
            value.putString("value", row.getValue());
            results.addObject(value);
        }
        return results;
    }
}
