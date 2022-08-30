package io.vertx.ext.mongo;

import com.mongodb.ServerAddress;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoClientSettings;
import io.vertx.mongo.connection.ClusterSettings;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class MongoClientSettingsTest {

    @Test
    public void basicTest() {
        // build a real object
        MongoClientSettings settings = new MongoClientSettings()
                .setClusterSettings(new ClusterSettings()
                        .setHosts(Collections.singletonList(new ServerAddress("localhost", 27018))));
        // serialize to json for further usage
        JsonObject jsonSettings = settings.toJson();
        System.out.println(jsonSettings);
    }
}