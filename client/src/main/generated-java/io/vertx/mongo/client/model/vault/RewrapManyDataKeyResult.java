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
package io.vertx.mongo.client.model.vault;

import static java.util.Objects.requireNonNull;

import io.vertx.mongo.bulk.BulkWriteResult;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Exception;

public class RewrapManyDataKeyResult {
  private BulkWriteResult bulkWriteResult;

  int __ctorIndex;

  private Exception bulkWriteResultException;

  private RewrapManyDataKeyResult() {
  }

  /**
   *  @return the bulk write result of the rewrapping data keys or null if there was no bulk operation
   */
  public BulkWriteResult getBulkWriteResult() {
    if (bulkWriteResultException != null)  {
      throw new RuntimeException(bulkWriteResultException);
    }
    return bulkWriteResult;
  }

  /**
   * @param from from
   * @return mongo object
   * @hidden
   */
  public static RewrapManyDataKeyResult fromDriverClass(MongoClientContext clientContext,
      com.mongodb.client.model.vault.RewrapManyDataKeyResult from) {
    requireNonNull(from, "from is null");
    RewrapManyDataKeyResult result = new RewrapManyDataKeyResult();
    try {
      result.bulkWriteResult = BulkWriteResult.fromDriverClass(clientContext, from.getBulkWriteResult());
    } catch (Exception ex) {
      result.bulkWriteResultException = ex;
    }
    return result;
  }
}
