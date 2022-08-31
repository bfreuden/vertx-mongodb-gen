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

import java.lang.Exception;

public class DeleteResult {
  private boolean acknowledged;

  private long deletedCount;

  private Exception acknowledgedException;

  private Exception deletedCountException;

  private DeleteResult() {
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
   *  Gets the number of documents deleted.
   *
   *  @return the number of documents deleted
   */
  public long getDeletedCount() {
    if (deletedCountException != null)  {
      throw new RuntimeException(deletedCountException);
    }
    return deletedCount;
  }

  /**
   * @param from from
   * @return mongo object
   * @hidden
   */
  public static DeleteResult fromDriverClass(com.mongodb.client.result.DeleteResult from) {
    requireNonNull(from, "from is null");
    DeleteResult result = new DeleteResult();
    try {
      result.acknowledged = from.wasAcknowledged();
    } catch (Exception ex) {
      result.acknowledgedException = ex;
    }
    try {
      result.deletedCount = from.getDeletedCount();
    } catch (Exception ex) {
      result.deletedCountException = ex;
    }
    return result;
  }
}
