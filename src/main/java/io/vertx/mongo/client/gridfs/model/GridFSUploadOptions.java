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
package io.vertx.mongo.client.gridfs.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Integer;

/**
 *  GridFS upload options
 *
 *  Customizable options used when uploading files into GridFS
 *
 *  @since 3.1
 */
@DataObject(
    generateConverter = true
)
public class GridFSUploadOptions {
  /**
   * the number of bytes per chunk for the uploaded file
   */
  private Integer chunkSizeBytes;

  /**
   * the metadata to be stored
   */
  private JsonObject metadata;

  /**
   *  Sets the chunk size in bytes.
   *
   *  @param chunkSizeBytes the number of bytes per chunk for the uploaded file
   *  @return this
   */
  public GridFSUploadOptions chunkSizeBytes(Integer chunkSizeBytes) {
    return this;
  }

  /**
   *  The number of bytes per chunk of this file.
   *
   *  <p>If no value has been set then, the chunkSizeBytes from the GridFSBucket will be used.</p>
   *
   *  @return number of bytes per chunk if set or null
   */
  public Integer getChunkSizeBytes() {
    return chunkSizeBytes;
  }

  /**
   *  Sets metadata to stored alongside the filename in the files collection
   *
   *  @param metadata the metadata to be stored
   *  @return this
   */
  public GridFSUploadOptions metadata(JsonObject metadata) {
    return this;
  }

  /**
   *  Returns any user provided data for the 'metadata' field of the files collection document.
   *
   *  @return the user provided metadata for the file if set or null
   */
  public JsonObject getMetadata() {
    return metadata;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.gridfs.model.GridFSUploadOptions toDriverClass() {
    com.mongodb.client.gridfs.model.GridFSUploadOptions result = new com.mongodb.client.gridfs.model.GridFSUploadOptions();
    if (this.chunkSizeBytes != null) {
      result.chunkSizeBytes(this.chunkSizeBytes);
    }
    if (this.metadata != null) {
      result.metadata(ConversionUtilsImpl.INSTANCE.toDocument(this.metadata));
    }
    return result;
  }
}
