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
package io.vertx.mongo.client.result;

import static java.util.Objects.requireNonNull;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Object;

@DataObject(
    generateConverter = true
)
public abstract class UpdateResult {
  private boolean acknowledged;

  private long matchedCount;

  private long modifiedCount;

  private Object upsertedId;

  /**
   * @hidden
   */
  public UpdateResult(com.mongodb.client.result.UpdateResult from) {
    requireNonNull(from, "from is null");
    this.acknowledged = from.wasAcknowledged();
    this.matchedCount = from.getMatchedCount();
    this.modifiedCount = from.getModifiedCount();
    this.upsertedId = ConversionUtilsImpl.INSTANCE.toObject(from.getUpsertedId());
  }

  /**
   *  Returns true if the write was acknowledged.
   *
   *  @return true if the write was acknowledged
   */
  public boolean isAcknowledged() {
    return acknowledged;
  }

  /**
   *  Gets the number of documents matched by the query.
   *
   *  @return the number of documents matched
   */
  public long getMatchedCount() {
    return matchedCount;
  }

  /**
   *  Gets the number of documents modified by the update.
   *
   *  @return the number of documents modified
   */
  public long getModifiedCount() {
    return modifiedCount;
  }

  /**
   *  If the replace resulted in an inserted document, gets the _id of the inserted document, otherwise null.
   *
   *  @return if the replace resulted in an inserted document, the _id of the inserted document, otherwise null
   */
  public Object getUpsertedId() {
    return upsertedId;
  }
}
