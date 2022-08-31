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

import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Exception;
import java.lang.Object;

public class InsertOneResult {
  private boolean acknowledged;

  private Object insertedId;

  private Exception acknowledgedException;

  private Exception insertedIdException;

  private InsertOneResult() {
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
   *  If the _id of the inserted document if available, otherwise null
   *
   *  <p>Note: Inserting RawBsonDocuments does not generate an _id value.</p>
   *
   *  @return if _id of the inserted document if available, otherwise null
   */
  public Object getInsertedId() {
    if (insertedIdException != null)  {
      throw new RuntimeException(insertedIdException);
    }
    return insertedId;
  }

  /**
   * @param from from
   * @return mongo object
   * @hidden
   */
  public static InsertOneResult fromDriverClass(com.mongodb.client.result.InsertOneResult from) {
    requireNonNull(from, "from is null");
    InsertOneResult result = new InsertOneResult();
    try {
      result.acknowledged = from.wasAcknowledged();
    } catch (Exception ex) {
      result.acknowledgedException = ex;
    }
    try {
      result.insertedId = ConversionUtilsImpl.INSTANCE.toObject(from.getInsertedId());
    } catch (Exception ex) {
      result.insertedIdException = ex;
    }
    return result;
  }
}
