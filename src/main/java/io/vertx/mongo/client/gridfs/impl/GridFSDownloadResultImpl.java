package io.vertx.mongo.client.gridfs.impl;

import com.mongodb.reactivestreams.client.gridfs.GridFSDownloadPublisher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.OpenOptions;
import io.vertx.mongo.client.gridfs.GridFSDownloadResult;
import io.vertx.mongo.client.gridfs.model.GridFSFile;
import io.vertx.mongo.impl.*;

import static io.vertx.mongo.impl.Utils.setHandler;

public class GridFSDownloadResultImpl extends MongoResultImpl<Buffer> implements GridFSDownloadResult {

    private final GridFSDownloadPublisher gridFSPublisher;

    public GridFSDownloadResultImpl(MongoClientContext clientContext, GridFSDownloadPublisher gridFSPublisher) {
        super(clientContext, new MappingPublisher<>(gridFSPublisher, ConversionUtilsImpl.INSTANCE::toBuffer));
        this.gridFSPublisher =  gridFSPublisher;
    }

    @Override
    public void getGridFSFile(Handler<AsyncResult<GridFSFile>> resultHandler) {
        Future<GridFSFile> __future = this.getGridFSFile();
        setHandler(__future, resultHandler);
    }

    @Override
    public Future<GridFSFile> getGridFSFile() {
        Promise<com.mongodb.client.gridfs.model.GridFSFile> __promise = clientContext.getVertx().promise();
        gridFSPublisher.getGridFSFile().subscribe(new SingleResultSubscriber<>(clientContext, __promise));
        return __promise.future().map(_item -> GridFSFile.fromDriverClass(clientContext, _item));
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
