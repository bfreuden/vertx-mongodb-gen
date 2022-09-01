package io.vertx.mongo.client;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoClientSettingsInitializer;
import io.vertx.mongo.impl.ObjectIdInputMapper;
import io.vertx.mongo.impl.ObjectIdOutputMapper;

import java.util.function.Function;

/**
 * Client configuration
 */
@DataObject(
        generateConverter = true
)
public class ClientConfig {

    private String connectionString;
    private ConnectionString mongoConnectionString;
    private MongoClientSettings mongoSettings;
    private boolean useObjectIds = false;
    private io.vertx.mongo.MongoClientSettings settings;
    private final MongoClientSettingsInitializer initializer = new MongoClientSettingsInitializer();
    private Function<JsonObject, JsonObject> inputMapper;
    private Function<JsonObject, JsonObject> outputMapper;

    public ClientConfig() {}

    public ClientConfig(JsonObject config) {
        ClientConfigConverter.fromJson(config, this);
    }

    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        ClientConfigConverter.toJson(this, result);
        return result;
    }

    public static ClientConfig defaultConfig() {
        ClientConfig result = new ClientConfig();
        return result;
    }

    /**
     *
     * @param connectionString
     * @return
     */
    public static ClientConfig fromConnectionString(String connectionString) {
        ClientConfig result = new ClientConfig();
        result.setConnectionString(connectionString);
        return result;
    }

    public static ClientConfig fromConnectionString(ConnectionString connectionString) {
        ClientConfig result = new ClientConfig();
        result.setMongoConnectionString(connectionString);
        return result;
    }

    public static ClientConfig fromSettings(io.vertx.mongo.MongoClientSettings clientSettings) {
        ClientConfig result = new ClientConfig();
        result.setSettings(clientSettings);
        return result;
    }

    public static ClientConfig fromSettings(MongoClientSettings clientSettings) {
        ClientConfig result = new ClientConfig();
        result.setMongoSettings(clientSettings);
        return result;
    }

    public static ClientConfig fromSettings(JsonObject jsonSettings) {
        ClientConfig result = new ClientConfig();
        result.setSettings(new io.vertx.mongo.MongoClientSettings(jsonSettings));
        return result;
    }

    private void assertNotConfigured() {
        if (mongoConnectionString != null)
            throw new IllegalStateException("already configured with a ConnectionString");
        if (mongoSettings != null)
            throw new IllegalStateException("already configured with a native MongoClientSettings");
        if (connectionString != null)
            throw new IllegalStateException("already configured with a connection string");
        if (settings != null)
            throw new IllegalStateException("already configured with a Vert.x MongoClientSettings");
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
    public ClientConfig setMongoSettings(MongoClientSettings settings) {
        assertNotConfigured();
        this.mongoSettings = settings;
        return this;
    }

    public io.vertx.mongo.MongoClientSettings getSettings() {
        return settings;
    }

    public ClientConfig setSettings(io.vertx.mongo.MongoClientSettings settings) {
        assertNotConfigured();
        this.settings = settings;
        return this;
    }

    public ConnectionString getMongoConnectionString() {
        return mongoConnectionString;
    }

    public ClientConfig setMongoConnectionString(ConnectionString connectionString) {
        assertNotConfigured();
        this.mongoConnectionString = connectionString;
        return this;
    }

    public ClientConfig setConnectionString(String connectionString) {
        assertNotConfigured();
        this.connectionString = connectionString;
        return this;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public MongoClientSettingsInitializer getPostInitializer() {
        return this.initializer;
    }

    public Function<JsonObject, JsonObject> getInputMapper() {
        return useObjectIds ? null : new ObjectIdInputMapper();
    }

    public Function<JsonObject, JsonObject> getOutputMapper() {
        return useObjectIds ? null : new ObjectIdOutputMapper();
    }

    /**
     * @hidden
     */
    public void initializeMappers() {
        if (!useObjectIds) {
            this.inputMapper = new ObjectIdInputMapper();
            this.outputMapper = new ObjectIdOutputMapper();
        }
    }
}
