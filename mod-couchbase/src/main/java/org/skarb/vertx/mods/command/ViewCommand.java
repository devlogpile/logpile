package org.skarb.vertx.mods.command;

import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewResponse;
import org.skarb.vertx.mods.CouchBasePersistor;
import org.skarb.vertx.mods.mapper.DocumentMapper;
import org.skarb.vertx.mods.mapper.Mapper;
import org.skarb.vertx.mods.mapper.ResultMapper;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * Access to a couch base view.
 * User: skarb
 * Date: 30/07/13
 */
public class ViewCommand extends Command {


    public ViewCommand(CouchBasePersistor persistor, Message<JsonObject> message) {
        super(persistor, message);
    }


    @Override
    public JsonObject run() throws Exception {
        final String viewName = getMandatoryString("view");
        if (viewName == null) {
            throw new IllegalArgumentException("no view");

        }

        final String documentName = getMandatoryString("documentName");
        if (documentName == null) {
            throw new IllegalArgumentException("no documentName");
        }

        final View view = persistor.client.getView(documentName, viewName);
        boolean includeDocs = message.body().getBoolean("include-docs", true);
        final Query query = new Query();
        query.setIncludeDocs(includeDocs).setLimit(persistor.queryLimit);

        final ViewResponse result = persistor.client.query(view, query);
        persistor.getContainer().logger().debug("doView.result " + result);
        Mapper mapper = new ResultMapper();
        if (includeDocs) {
            mapper = new DocumentMapper();
        }
        final JsonArray results = mapper.map(result);

        // create result
        final JsonObject reply = new JsonObject();
        reply.putNumber("total_rows", result.size());
        reply.putArray("rows", results);
        return reply;
    }
}
