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

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.gridfs.model.GridFSFile;

/**
 *  A GridFS Publisher for downloading data from GridFS
 *  <p>Provides the {@code GridFSFile} for the file to being downloaded as well as a way to control the batchsize.</p>
 *  @since 1.13
 */
public interface GridFSDownloadResult extends MongoResult<Buffer> {
  /**
   *  Gets the corresponding {@link GridFSFile} for the file being downloaded
   *  @return a future with a single element, the corresponding GridFSFile for the file being downloaded
   */
  Future<GridFSFile> getGridFSFile();

  /**
   *  Gets the corresponding {@link GridFSFile} for the file being downloaded
   *  @param resultHandler an async result with a single element, the corresponding GridFSFile for the file being downloaded
   */
  void getGridFSFile(Handler<AsyncResult<GridFSFile>> resultHandler);

  void saveToFile(String filename, Handler<AsyncResult<Void>> resultHandler);

  public Future<Void> saveToFile(String filename);


  public void saveToFile(String filename, int batchSize, Handler<AsyncResult<Void>> resultHandler);

  public Future<Void> saveToFile(String filename, int batchSize);


}
