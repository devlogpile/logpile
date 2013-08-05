package org.skarb.vertx.mods.mapper;

import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * Transform the Couchbase datas in {@link org.vertx.java.core.json.JsonElement}.
 * User: skarb
 * Date: 30/07/13
 */
public class DocumentMapper implements Mapper {

    public DocumentMapper(){
        //NOP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray map(final ViewResponse viewResponse){
        JsonArray results = new JsonArray();
        for (final ViewRow row : viewResponse) {
            results.addObject(new JsonObject(row.getDocument().toString()));
        }
        return results;
    }
}
