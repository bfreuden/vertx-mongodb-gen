package io.vertx.mongo.client.gridfs;

import com.mongodb.reactivestreams.client.gridfs.GridFSUploadPublisher;
import io.vertx.codegen.annotations.DataObject;

/**
 *  A GridFS {@code Options} for uploading data into GridFS
 *
 *  <p>Provides the {@code id} for the file to be uploaded. Cancelling the subscription to this publisher will cause any uploaded data
 *  to be cleaned up and removed.</p>
 *
 *  @since 1.13
 */
@DataObject(
    generateConverter = true
)
public class GridFSUploadOptions {
  /**
   * @hidden
   */
  public <TDocument> void initializePublisher(GridFSUploadPublisher<TDocument> publisher) {
  }
}
