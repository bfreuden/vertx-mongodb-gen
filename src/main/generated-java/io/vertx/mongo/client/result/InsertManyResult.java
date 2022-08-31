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

import io.vertx.mongo.impl.CollectionsConversionUtils;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.util.Map;

public class InsertManyResult {
  private boolean acknowledged;

  private Map<Integer, Object> insertedIds;

  private Exception acknowledgedException;

  private Exception insertedIdsException;

  private InsertManyResult() {
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
   *  An unmodifiable map of the index of the inserted document to the id of the inserted document.
   *
   *  <p>Note: Inserting RawBsonDocuments does not generate an _id value and it's corresponding value will be null.</p>
   *
   *  @return  A map of the index of the inserted document to the id of the inserted document.
   */
  public Map<Integer, Object> getInsertedIds() {
    if (insertedIdsException != null)  {
      throw new RuntimeException(insertedIdsException);
    }
    return insertedIds;
  }

  /**
   * @param from from
   * @return mongo object
   * @hidden
   */
  public static InsertManyResult fromDriverClass(MongoClientContext clientContext,
      com.mongodb.client.result.InsertManyResult from) {
    requireNonNull(from, "from is null");
    InsertManyResult result = new InsertManyResult();
    try {
      result.acknowledged = from.wasAcknowledged();
    } catch (Exception ex) {
      result.acknowledgedException = ex;
    }
    try {
      result.insertedIds = CollectionsConversionUtils.mapValues(from.getInsertedIds(), clientContext.getConversionUtils()::toObject);
    } catch (Exception ex) {
      result.insertedIdsException = ex;
    }
    return result;
  }
}
