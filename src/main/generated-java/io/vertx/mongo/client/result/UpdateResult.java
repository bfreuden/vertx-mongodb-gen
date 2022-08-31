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

import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Exception;
import java.lang.Object;

public class UpdateResult {
  private boolean acknowledged;

  private long matchedCount;

  private long modifiedCount;

  private Object upsertedId;

  private Exception acknowledgedException;

  private Exception matchedCountException;

  private Exception modifiedCountException;

  private Exception upsertedIdException;

  private UpdateResult() {
  }

  /**
   *  Returns true if the write was acknowledged.
   *
   *  @return true if the write was acknowledged
   */
  public boolean isAcknowledged() {
    if (acknowledgedException != null)  {
      throw new RuntimeException(acknowledgedException);
    }
    return acknowledged;
  }

  /**
   *  Gets the number of documents matched by the query.
   *
   *  @return the number of documents matched
   */
  public long getMatchedCount() {
    if (matchedCountException != null)  {
      throw new RuntimeException(matchedCountException);
    }
    return matchedCount;
  }

  /**
   *  Gets the number of documents modified by the update.
   *
   *  @return the number of documents modified
   */
  public long getModifiedCount() {
    if (modifiedCountException != null)  {
      throw new RuntimeException(modifiedCountException);
    }
    return modifiedCount;
  }

  /**
   *  If the replace resulted in an inserted document, gets the _id of the inserted document, otherwise null.
   *
   *  @return if the replace resulted in an inserted document, the _id of the inserted document, otherwise null
   */
  public Object getUpsertedId() {
    if (upsertedIdException != null)  {
      throw new RuntimeException(upsertedIdException);
    }
    return upsertedId;
  }

  /**
   * @param from from
   * @return mongo object
   * @hidden
   */
  public static UpdateResult fromDriverClass(MongoClientContext clientContext,
      com.mongodb.client.result.UpdateResult from) {
    requireNonNull(from, "from is null");
    UpdateResult result = new UpdateResult();
    try {
      result.acknowledged = from.wasAcknowledged();
    } catch (Exception ex) {
      result.acknowledgedException = ex;
    }
    try {
      result.matchedCount = from.getMatchedCount();
    } catch (Exception ex) {
      result.matchedCountException = ex;
    }
    try {
      result.modifiedCount = from.getModifiedCount();
    } catch (Exception ex) {
      result.modifiedCountException = ex;
    }
    try {
      result.upsertedId = clientContext.getConversionUtils().toObject(from.getUpsertedId());
    } catch (Exception ex) {
      result.upsertedIdException = ex;
    }
    return result;
  }
}
