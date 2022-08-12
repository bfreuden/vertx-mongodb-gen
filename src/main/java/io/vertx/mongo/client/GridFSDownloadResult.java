package io.vertx.mongo.client;

import com.mongodb.client.gridfs.model.GridFSFile;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.nio.ByteBuffer;

public interface GridFSDownloadResult extends MongoResult<ByteBuffer> {

    void getGridFSFile(Handler<AsyncResult<GridFSFile>> handler);

    Future<GridFSFile> getGridFSFile();

}
