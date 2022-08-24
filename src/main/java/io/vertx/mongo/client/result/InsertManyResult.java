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
import io.vertx.mongo.impl.CollectionsConversionUtils;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Integer;
import java.lang.Object;
import java.util.Map;

@DataObject(
    generateConverter = true
)
public class InsertManyResult {
  private boolean acknowledged;

  private Map<Integer, Object> insertedIds;

  private InsertManyResult() {
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
   *  An unmodifiable map of the index of the inserted document to the id of the inserted document.
   *
   *  <p>Note: Inserting RawBsonDocuments does not generate an _id value and it's corresponding value will be null.</p>
   *
   *  @return  A map of the index of the inserted document to the id of the inserted document.
   */
  public Map<Integer, Object> getInsertedIds() {
    return insertedIds;
  }

  /**
   * @return mongo object
   * @hidden
   */
  public static InsertManyResult fromDriverClass(com.mongodb.client.result.InsertManyResult from) {
    requireNonNull(from, "from is null");
    InsertManyResult result = new InsertManyResult();
    result.acknowledged = from.wasAcknowledged();
    result.insertedIds = CollectionsConversionUtils.mapValues(from.getInsertedIds(), ConversionUtilsImpl.INSTANCE::toObject);
    return result;
  }
}
