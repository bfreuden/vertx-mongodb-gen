package io.vertx.mongo.client.gridfs.impl;

import com.mongodb.reactivestreams.client.gridfs.GridFSDownloadPublisher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.OpenOptions;
import io.vertx.mongo.client.gridfs.GridFSDownloadResult;
import io.vertx.mongo.impl.MappingPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.MongoResultImpl2;
import org.reactivestreams.Publisher;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static io.vertx.mongo.impl.Utils.setHandler;

public abstract class GridFSDownloadResultBase extends MongoResultImpl<Buffer> implements GridFSDownloadResult {

    protected final MongoClientContext clientContext;

    public GridFSDownloadResultBase(MongoClientContext clientContext,
                                    Publisher<Buffer> wrapped) {
        super(clientContext, wrapped);
        this.clientContext = clientContext;
    }

    public GridFSDownloadResultBase(MongoClientContext clientContext, GridFSDownloadPublisher wrapped,
                                    Function<Buffer, Buffer> mapper) {
        super(clientContext, new MappingPublisher<>(wrapped, clientContext.getMapper()::toBuffer));
        this.clientContext = clientContext;
    }

    @Override
    public void saveToFile(String filename, Handler<AsyncResult<Void>> resultHandler) {
        Future<Void> __future = this.saveToFile(filename);
        setHandler(__future, resultHandler);
    }

    @Override
    public Future<Void> saveToFile(String filename) {
        OpenOptions options = new OpenOptions().setWrite(true);
        return clientContext.getVertx().fileSystem()
                .open(filename, options)
                .flatMap(file -> stream(1).pipeTo(file));
    }


    @Override
    public void saveToFile(String filename, int batchSize, Handler<AsyncResult<Void>> resultHandler) {
        Future<Void> __future = this.saveToFile(filename, batchSize);
        setHandler(__future, resultHandler);
    }

    @Override
    public Future<Void> saveToFile(String filename, int batchSize) {
        OpenOptions options = new OpenOptions().setWrite(true);
        return clientContext.getVertx().fileSystem()
                .open(filename, options)
                .flatMap(file -> stream(batchSize).pipeTo(file));
    }

}
