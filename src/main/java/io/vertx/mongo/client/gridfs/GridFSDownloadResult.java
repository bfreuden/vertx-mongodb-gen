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
import io.vertx.core.file.OpenOptions;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.gridfs.model.GridFSFile;

import static io.vertx.mongo.impl.Utils.setHandler;

public interface GridFSDownloadResult extends MongoResult<Buffer> {

    void getGridFSFile(Handler<AsyncResult<GridFSFile>> handler);

    Future<GridFSFile> getGridFSFile();

    void saveToFile(String filename, Handler<AsyncResult<Void>> resultHandler);

    public Future<Void> saveToFile(String filename);


    public void saveToFile(String filename, int batchSize, Handler<AsyncResult<Void>> resultHandler);

    public Future<Void> saveToFile(String filename, int batchSize);


}
