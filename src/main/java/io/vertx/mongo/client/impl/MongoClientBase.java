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
import io.vertx.mongo.client.ClientConfig;
import io.vertx.mongo.client.MongoClient;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.codec.json.JsonObjectCodec;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.io.IOException;
import java.util.Objects;

public abstract class MongoClientBase implements MongoClient {

    private final static CodecRegistry commonCodecRegistry = CodecRegistries.fromCodecs(new StringCodec(), new IntegerCodec(),
            new BooleanCodec(), new DoubleCodec(), new LongCodec(), new BsonDocumentCodec());
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
        this.clientContext = new MongoClientContext(this.vertx, creatingContext, config);
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
                if (config.getMongoSettings() != null) {
                    mongo = MongoClients.create(config.getMongoSettings());
                } else {
                    MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder();
                    if (config.getMongoConnectionString() != null) {
                        settingsBuilder.applyConnectionString(config.getMongoConnectionString());
                    } else if (config.getConnectionString() != null) {
                        settingsBuilder.applyConnectionString(new ConnectionString(config.getConnectionString()));
                    }
                    settingsBuilder.codecRegistry(
                            CodecRegistries.fromRegistries(
                                    commonCodecRegistry,
                                    CodecRegistries.fromCodecs(
                                            new JsonObjectCodec(new JsonObject().put("useObjectId", config.isUseObjectIds()))
                                    )
                            )
                    );
                    mongo = MongoClients.create(settingsBuilder.build());
                }
            }
            return mongo;
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
