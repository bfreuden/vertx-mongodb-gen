package io.vertx.mongo.client.gridfs;

import io.vertx.codegen.annotations.DataObject;
import java.lang.Integer;

/**
 *  A GridFS Options for downloading data from GridFS
 *
 *  <p>Provides the {@code GridFSFile} for the file to being downloaded as well as a way to control the batchsize.</p>
 *
 *  @since 1.13
 */
@DataObject(
    generateConverter = true
)
public class GridFSDownloadOptions {
  /**
   * the preferred buffer size in bytes to use per {@code ByteBuffer} in the {@code Publisher}, defaults to chunk
   */
  private Integer bufferSizeBytes;

  /**
   *  The preferred number of bytes per {@code ByteBuffer} returned by the {@code Publisher}.
   *
   *  <p>Allows for larger than chunk size ByteBuffers. The actual chunk size of the data stored in MongoDB is the smallest allowable
   *  {@code ByteBuffer} size.</p>
   *
   *  <p>Can be used to control the memory consumption of this {@code Publisher}. The smaller the bufferSizeBytes the lower the memory
   *  consumption and higher latency.</p>
   *
   *  <p>Note: Must be set before the Publisher is subscribed to.</p>
   *
   *  @param bufferSizeBytes the preferred buffer size in bytes to use per {@code ByteBuffer} in the {@code Publisher}, defaults to chunk
   *                         size.
   *  @return this
   */
  public GridFSDownloadOptions bufferSizeBytes(Integer bufferSizeBytes) {
    return this;
  }

  public Integer getBufferSizeBytes() {
    return bufferSizeBytes;
  }
}
