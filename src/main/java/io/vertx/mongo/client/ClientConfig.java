package io.vertx.mongo.client;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;

@DataObject(
        generateConverter = true
)
public class ClientConfig {

    private String connectionString;
    private ConnectionString mongoConnectionString;
    private MongoClientSettings mongoSettings;
    private boolean useObjectIds = false;

    private void assertNotConfigured() {
        if (mongoConnectionString != null)
            throw new IllegalStateException("already configured with a ConnectionString");
        if (mongoSettings != null)
            throw new IllegalStateException("already configured with a MongoClientSettings");
        if (connectionString != null)
            throw new IllegalStateException("already configured with a connection string");
    }

    public boolean isUseObjectIds() {
        return useObjectIds;
    }

    public ClientConfig useObjectIds(boolean useObjectIds) {
        this.useObjectIds = useObjectIds;
        return this;
    }
    @GenIgnore
    public MongoClientSettings getMongoSettings() {
        return mongoSettings;
    }

    @GenIgnore
    public ClientConfig mongoSettings(MongoClientSettings settings) {
        assertNotConfigured();
        this.mongoSettings = settings;
        return this;
    }

    @GenIgnore
    public ConnectionString getMongoConnectionString() {
        return mongoConnectionString;
    }

    @GenIgnore
    public ClientConfig mongoConnectionString(ConnectionString connectionString) {
        assertNotConfigured();
        this.mongoConnectionString = connectionString;
        return this;
    }

    public ClientConfig connectionString(String connectionString) {
        assertNotConfigured();
        this.connectionString = connectionString;
        return this;
    }

    public String getConnectionString() {
        return connectionString;
    }

}
