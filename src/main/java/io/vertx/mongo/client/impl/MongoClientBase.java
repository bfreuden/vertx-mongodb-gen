package io.vertx.mongo.client.impl;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClients;
import io.vertx.core.*;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.Shareable;
import io.vertx.mongo.AutoEncryptionSettings;
import io.vertx.mongo.MongoClientSettingsInitializer;
import io.vertx.mongo.client.ClientConfig;
import io.vertx.mongo.client.MongoClient;
import io.vertx.mongo.connection.*;
import io.vertx.mongo.impl.ConversionUtils;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.codec.json.JsonObjectCodec;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.io.IOException;
import java.util.Objects;

public abstract class MongoClientBase implements MongoClient {

    private final static CodecRegistry commonCodecRegistry = CodecRegistries.fromCodecs(new StringCodec(), new IntegerCodec(),
            new BooleanCodec(), new DoubleCodec(), new LongCodec(), new BsonDocumentCodec(), new BsonObjectIdCodec());
    private static final String DS_LOCAL_MAP_NAME = "__vertx.MongoClientBase.datasources";
    private final VertxInternal vertx;
    private final ContextInternal creatingContext;
    private final MongoHolder holder;
    protected final MongoClientContext clientContext;
    protected final com.mongodb.reactivestreams.client.MongoClient wrapped;

    protected MongoClientBase(Vertx vertx, ClientConfig config, String dataSourceName) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(config);
        Objects.requireNonNull(dataSourceName);
        this.vertx = (VertxInternal) vertx;
        this.creatingContext = this.vertx.getOrCreateContext();
        this.holder = lookupHolder(dataSourceName);
        this.wrapped = holder.mongo(config);
        this.clientContext = new MongoClientContext(this.vertx, creatingContext, this.holder.codecRegistry, this.holder.config);
        creatingContext.addCloseHook(this);
    }

    @Override
    public void close(Promise<Void> completionHandler) {
        holder.close();
        completionHandler.complete();
    }

    @Override
    public Future<Void> close() {
        holder.close();
        creatingContext.removeCloseHook(this);
        return vertx.getOrCreateContext().succeededFuture();
    }

    private void removeFromMap(LocalMap<String, MongoHolder> map, String dataSourceName) {
        synchronized (vertx) {
            map.remove(dataSourceName);
            if (map.isEmpty()) {
                map.close();
            }
        }
    }

    private MongoHolder lookupHolder(String datasourceName) {
        synchronized (vertx) {
            LocalMap<String, MongoHolder> map = vertx.sharedData().getLocalMap(DS_LOCAL_MAP_NAME);
            MongoHolder theHolder = map.get(datasourceName);
            if (theHolder == null) {
                theHolder = new MongoHolder(() -> removeFromMap(map, datasourceName));
                map.put(datasourceName, theHolder);
            } else {
                theHolder.incRefCount();
            }
            return theHolder;
        }
    }

    @Override
    public void close(Handler<AsyncResult<Void>> handler) {
        ContextInternal ctx = vertx.getOrCreateContext();
        close(ctx.promise(handler));
    }

    private class MongoHolder implements Shareable {
        com.mongodb.reactivestreams.client.MongoClient mongo;
        Runnable closeRunner;
        int refCount = 1;
        CodecRegistry codecRegistry;
        ClientConfig config;

        MongoHolder(Runnable closeRunner) {
            this.closeRunner = closeRunner;
        }

        synchronized com.mongodb.reactivestreams.client.MongoClient mongo() {
            if (mongo == null) {
                mongo = MongoClients.create();
            }
            return mongo;
        }

        synchronized com.mongodb.reactivestreams.client.MongoClient mongo(ClientConfig config) {
            if (mongo == null) {
                config.initializeMappers();
                this.config = config;
                if (config.getMongoSettings() != null) {
                    mongo = MongoClients.create(config.getMongoSettings());
                    codecRegistry = config.getMongoSettings().getCodecRegistry();
                } else {
                    MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder();
                    settingsBuilder.codecRegistry(
                            CodecRegistries.fromRegistries(
                                    commonCodecRegistry,
                                    CodecRegistries.fromCodecs(
//                                            new JsonObjectCodec(new JsonObject().put("useObjectId", true))
                                            new JsonObjectCodec(new JsonObject().put("useObjectId", config.isUseObjectIds()))
                                    )
                            )
                    );
                    io.vertx.mongo.MongoClientSettings vertxConfig = null;
                    if (config.getMongoConnectionString() != null) {
                        settingsBuilder.applyConnectionString(config.getMongoConnectionString());
                        mergeVertxSettingsIntoMongoSettingsBuilder(config.getPostInitializer(), settingsBuilder, null);
                    } else if (config.getConnectionString() != null) {
                        settingsBuilder.applyConnectionString(new ConnectionString(config.getConnectionString()));
                        mergeVertxSettingsIntoMongoSettingsBuilder(config.getPostInitializer(), settingsBuilder, null);
                    }  else if (config.getSettings() != null) {
                        vertxConfig = config.getSettings();
                    }
                    mergeVertxSettingsIntoMongoSettingsBuilder(config.getPostInitializer(), settingsBuilder, vertxConfig);
                    MongoClientSettings settings = settingsBuilder.build();
                    mongo = MongoClients.create(settings);
                    codecRegistry = settings.getCodecRegistry();
                }
            }
            return mongo;
        }

        private void mergeVertxSettingsIntoMongoSettingsBuilder(MongoClientSettingsInitializer postInitializer, MongoClientSettings.Builder settingsBuilder, io.vertx.mongo.MongoClientSettings vertxConfig) {
            settingsBuilder.applyToClusterSettings(_builder -> {
                ClusterSettings clusterSettings = vertxConfig == null ? null : vertxConfig.getClusterSettings();
                if (clusterSettings != null)
                    clusterSettings.initializeDriverBuilderClass(clientContext, _builder);
                if (postInitializer.getClusterSettingsInitializer() != null)
                    postInitializer.getClusterSettingsInitializer().accept(creatingContext, _builder);
            });
            settingsBuilder.applyToConnectionPoolSettings(_builder -> {
                ConnectionPoolSettings connectionPoolSettings = vertxConfig == null ? null : vertxConfig.getConnectionPoolSettings();
                if (connectionPoolSettings != null)
                    connectionPoolSettings.initializeDriverBuilderClass(clientContext, _builder);
                if (postInitializer.getConnectionPoolSettingsInitializer() != null)
                    postInitializer.getConnectionPoolSettingsInitializer().accept(creatingContext, _builder);
            });
            settingsBuilder.applyToServerSettings(_builder -> {
                ServerSettings serverSettings = vertxConfig == null ? null : vertxConfig.getServerSettings();
                if (serverSettings != null)
                    serverSettings.initializeDriverBuilderClass(clientContext, _builder);
                if (postInitializer.getServerSettingsInitializer() != null)
                    postInitializer.getServerSettingsInitializer().accept(creatingContext, _builder);
            });
            settingsBuilder.applyToSocketSettings(_builder -> {
                SocketSettings socketSettings = vertxConfig == null ? null : vertxConfig.getSocketSettings();
                if (socketSettings != null)
                    socketSettings.initializeDriverBuilderClass(clientContext, _builder);
                if (postInitializer.getSocketSettingsInitializer() != null)
                    postInitializer.getSocketSettingsInitializer().accept(creatingContext, _builder);
            });
            settingsBuilder.applyToSslSettings(_builder -> {
                SslSettings sslSettings = vertxConfig == null ? null : vertxConfig.getSslSettings();
                if (sslSettings != null)
                    sslSettings.initializeDriverBuilderClass(clientContext, _builder);
                if (postInitializer.getSslSettingsInitializer() != null)
                    postInitializer.getSslSettingsInitializer().accept(creatingContext, _builder);
            });
            {
                AutoEncryptionSettings autoEncryptionSettings = vertxConfig == null ? null : vertxConfig.getAutoEncryptionSettings();
                if (autoEncryptionSettings != null || postInitializer.getAutoEncryptionSettingsInitializer() != null) {
                    com.mongodb.AutoEncryptionSettings.Builder _builder = com.mongodb.AutoEncryptionSettings.builder();
                    if (autoEncryptionSettings != null)
                        autoEncryptionSettings.initializeDriverBuilderClass(clientContext, _builder);
                    if (postInitializer.getAutoEncryptionSettingsInitializer() != null)
                        postInitializer.getAutoEncryptionSettingsInitializer().accept(creatingContext, _builder);
                }
            }
            if (postInitializer.getMongoClientSettingsInitializer() != null) {
                postInitializer.getMongoClientSettingsInitializer().accept(creatingContext, settingsBuilder);
            }
        }

        synchronized void incRefCount() {
            refCount++;
        }

        void close() {
            java.io.Closeable client;
            Runnable callback;
            synchronized (this) {
                if (--refCount > 0) {
                    return;
                }
                client = mongo;
                mongo = null;
                callback = closeRunner;
                closeRunner = null;
            }
            if (callback != null) {
                callback.run();
            }
            if (client != null) {
                MongoClientBase.this.vertx.executeBlocking(p -> {
                    try {
                        client.close();
                    } catch (IOException e) {
                        p.fail(e);
                    }
                });
            }
        }
    }

}
