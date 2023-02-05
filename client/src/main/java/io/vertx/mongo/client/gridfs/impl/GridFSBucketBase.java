package io.vertx.mongo.client.gridfs.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.client.gridfs.GridFSBucket;

public abstract class GridFSBucketBase implements GridFSBucket {

    protected Future<ReadStream<Buffer>> openFile(Vertx vertx, String filename) {
        OpenOptions openOptions = new OpenOptions().setRead(true);
        return vertx.fileSystem().open(filename, openOptions).map(it -> it);
    }

}
