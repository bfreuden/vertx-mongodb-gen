package io.vertx.mongo.client;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.connection.*;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.mongo.MongoClientSettingsInitializer;

import java.util.function.Consumer;

@DataObject(
        generateConverter = true
)
public class ClientConfig implements MongoClientSettingsInitializer {

    private String connectionString;

    private ConnectionString mongoConnectionString;
    private MongoClientSettings mongoSettings;
    private io.vertx.mongo.MongoClientSettings vertxMongoSettings;
    private boolean useObjectIds = false;
    private io.vertx.mongo.MongoClientSettings settings;
    public Consumer<ServerSettings.Builder> serverSettingsInitializer;
    private Consumer<SslSettings.Builder> sslSettingsInitializer;
    private Consumer<MongoClientSettings.Builder> mongoClientSettingsInitializer;
    private Consumer<ConnectionPoolSettings.Builder> connectionPoolSettingsInitializer;
    private Consumer<AutoEncryptionSettings.Builder> autoEncryptionSettingsInitializer;
    private Consumer<SocketSettings.Builder> socketSettingsInitializer;
    private Consumer<ClusterSettings.Builder> clusterSettingsInitializer;

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
    public ClientConfig mongoSettings(MongoClientSettings settings) {
        assertNotConfigured();
        this.mongoSettings = settings;
        return this;
    }

    @GenIgnore
    public io.vertx.mongo.MongoClientSettings getSettings() {
        return settings;
    }

    @GenIgnore
    public ClientConfig settings(io.vertx.mongo.MongoClientSettings settings) {
        assertNotConfigured();
        this.settings = settings;
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

    @Override
    public void initializeWithServerSettings(Consumer<ServerSettings.Builder> builderInitializer) {
        this.serverSettingsInitializer = builderInitializer;
    }

    @Override
    public void initializeWithSslSettings(Consumer<SslSettings.Builder> builderInitializer) {
        this.sslSettingsInitializer = builderInitializer;
    }

    @Override
    public void initializeWithMongoClientSettings(Consumer<MongoClientSettings.Builder> builderInitializer) {
        this.mongoClientSettingsInitializer = builderInitializer;
    }

    @Override
    public void initializeWithConnectionPoolSettings(Consumer<ConnectionPoolSettings.Builder> builderInitializer) {
        this.connectionPoolSettingsInitializer = builderInitializer;
    }

    @Override
    public void initializeWithAutoEncryptionSettings(Consumer<AutoEncryptionSettings.Builder> builderInitializer) {
        this.autoEncryptionSettingsInitializer = builderInitializer;
    }

    @Override
    public void initializeWithSocketSettings(Consumer<SocketSettings.Builder> builderInitializer) {
        this.socketSettingsInitializer = builderInitializer;
    }

    @Override
    public void initializeWithClusterSettings(Consumer<ClusterSettings.Builder> builderInitializer) {
        this.clusterSettingsInitializer = builderInitializer;
    }

    /**
     * @return initializer
     * @hidden
     */
    public Consumer<ServerSettings.Builder> getServerSettingsInitializer() {
        return serverSettingsInitializer;
    }

    /**
     * @return initializer
     * @hidden
     */
    public Consumer<SslSettings.Builder> getSslSettingsInitializer() {
        return sslSettingsInitializer;
    }

    /**
     * @return initializer
     * @hidden
     */
    public Consumer<MongoClientSettings.Builder> getMongoClientSettingsInitializer() {
        return mongoClientSettingsInitializer;
    }

    /**
     * @return initializer
     * @hidden
     */
    public Consumer<ConnectionPoolSettings.Builder> getConnectionPoolSettingsInitializer() {
        return connectionPoolSettingsInitializer;
    }

    /**
     * @return initializer
     * @hidden
     */
    public Consumer<AutoEncryptionSettings.Builder> getAutoEncryptionSettingsInitializer() {
        return autoEncryptionSettingsInitializer;
    }

    /**
     * @return initializer
     * @hidden
     */
    public Consumer<SocketSettings.Builder> getSocketSettingsInitializer() {
        return socketSettingsInitializer;
    }

    /**
     * @return initializer
     * @hidden
     */
    public Consumer<ClusterSettings.Builder> getClusterSettingsInitializer() {
        return clusterSettingsInitializer;
    }


}
