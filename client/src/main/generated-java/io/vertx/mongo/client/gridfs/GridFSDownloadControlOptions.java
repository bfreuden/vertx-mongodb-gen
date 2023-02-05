//
//  Copyright 2022 The Vert.x Community.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//
package io.vertx.mongo.client.gridfs;

import com.mongodb.reactivestreams.client.gridfs.GridFSDownloadPublisher;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.MongoClientContext;
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
public class GridFSDownloadControlOptions {
  /**
   * the preferred buffer size in bytes to use per {@code ByteBuffer} in the {@code Publisher}, defaults to chunk
   */
  private Integer bufferSizeBytes;

  public GridFSDownloadControlOptions() {
  }

  public GridFSDownloadControlOptions(JsonObject json) {
    GridFSDownloadControlOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    GridFSDownloadControlOptionsConverter.toJson(this, result);
    return result;
  }

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
  public GridFSDownloadControlOptions setBufferSizeBytes(Integer bufferSizeBytes) {
    this.bufferSizeBytes = bufferSizeBytes;
    return this;
  }

  public Integer getBufferSizeBytes() {
    return bufferSizeBytes;
  }

  /**
   * @param publisher MongoDB driver publisher
   * @hidden
   */
  public void initializePublisher(MongoClientContext clientContext,
      GridFSDownloadPublisher publisher) {
    if (this.bufferSizeBytes != null) {
      publisher.bufferSizeBytes(this.bufferSizeBytes);
    }
  }
}
