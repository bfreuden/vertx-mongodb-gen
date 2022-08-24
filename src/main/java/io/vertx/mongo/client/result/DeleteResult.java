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

@DataObject(
    generateConverter = true
)
public class DeleteResult {
  private boolean acknowledged;

  private long deletedCount;

  private DeleteResult() {
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
   *  Gets the number of documents deleted.
   *
   *  @return the number of documents deleted
   */
  public long getDeletedCount() {
    return deletedCount;
  }

  /**
   * @return mongo object
   * @hidden
   */
  public static DeleteResult fromDriverClass(com.mongodb.client.result.DeleteResult from) {
    requireNonNull(from, "from is null");
    DeleteResult result = new DeleteResult();
    result.acknowledged = from.wasAcknowledged();
    result.deletedCount = from.getDeletedCount();
    return result;
  }
}
