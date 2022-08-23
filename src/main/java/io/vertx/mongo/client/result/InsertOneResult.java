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
public abstract class InsertOneResult {
  private boolean acknowledged;

  private Object insertedId;

  /**
   * @hidden
   */
  public InsertOneResult(com.mongodb.client.result.InsertOneResult from) {
    requireNonNull(from, "from is null");
    this.acknowledged = from.wasAcknowledged();
    this.insertedId = ConversionUtilsImpl.INSTANCE.toObject(from.getInsertedId());
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
   *  If the _id of the inserted document if available, otherwise null
   *
   *  <p>Note: Inserting RawBsonDocuments does not generate an _id value.</p>
   *
   *  @return if _id of the inserted document if available, otherwise null
   */
  public Object getInsertedId() {
    return insertedId;
  }
}
