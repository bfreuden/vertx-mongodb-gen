package io.vertx.ext.mongo;

import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoClientSettings;
import io.vertx.mongo.connection.ClusterSettings;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MongoClientSettingsTest {

    @Test
    public void basicTest() {
        // build a real object including com.mongodb.ReadPreference and com.mongodb.ServerAddress
        // mongo driver objects that we don't want to convert into a data object
        // (because some of them have a convenient toBson method)
        MongoClientSettings settings = new MongoClientSettings()
                .setReadPreference(ReadPreference.nearest())
                .setClusterSettings(new ClusterSettings()
                        .setHosts(Collections.singletonList(new ServerAddress("localhost", 27018))));
        // serialize to json for further usage
        JsonObject jsonSettings = settings.toJson();
        System.out.println(jsonSettings);
        // de-serialize from json
        MongoClientSettings settings2 = new MongoClientSettings(jsonSettings);
        // check that no value is lost
        assertNotNull(settings2.getClusterSettings());
        assertNotNull(settings2.getClusterSettings().getHosts());
        assertEquals(1, settings2.getClusterSettings().getHosts().size());
        assertEquals("localhost:27018", settings2.getClusterSettings().getHosts().get(0).toString());
        assertEquals(ReadPreference.nearest(), settings2.getReadPreference());
    }
}
