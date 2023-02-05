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

import io.vertx.mongo.MongoResult;
import io.vertx.mongo.ObjectId;
import java.lang.Object;

/**
 *  A GridFS {@code Publisher} for uploading data into GridFS
 *  <p>Provides the {@code id} for the file to be uploaded. Cancelling the subscription to this publisher will cause any uploaded data
 *  to be cleaned up and removed.</p>
 *  @param <T> the result type of the publisher
 *  @since 1.13
 */
public interface GridFSUploadResult<T> extends MongoResult<T> {
  /**
   *  Gets the {@link org.bson.types.ObjectId} for the file to be uploaded
   *
   *  Throws a {@link com.mongodb.MongoGridFSException} if the file id is not an ObjectId.
   *
   *  @return the ObjectId for the file to be uploaded
   */
  ObjectId getObjectId();

  /**
   *  The {@link org.bson.BsonValue} id for this file.
   *
   *  @return the id for this file
   */
  Object getId();
}
