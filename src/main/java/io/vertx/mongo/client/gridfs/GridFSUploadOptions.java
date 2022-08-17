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
   * @param publisher MongoDB driver publisher
   * @param <TDocument> document class
   * @hidden
   */
  public <TDocument> void initializePublisher(GridFSUploadPublisher<TDocument> publisher) {
  }
}
