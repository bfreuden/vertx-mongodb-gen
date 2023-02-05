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
package io.vertx.mongo.client.model.changestream;

import static java.util.Objects.requireNonNull;

import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Exception;
import java.lang.String;

public class TruncatedArray {
  private String field;

  private int newSize;

  private Exception fieldException;

  private Exception newSizeException;

  private TruncatedArray() {
  }

  /**
   *  Returns the name of the truncated field.
   *
   *  @return {@code field}.
   */
  public String getField() {
    if (fieldException != null)  {
      throw new RuntimeException(fieldException);
    }
    return field;
  }

  /**
   *  Returns the size of the new {@linkplain #getField() field} value.
   *
   *  @return {@code newSize}.
   */
  public int getNewSize() {
    if (newSizeException != null)  {
      throw new RuntimeException(newSizeException);
    }
    return newSize;
  }

  /**
   * @param from from
   * @return mongo object
   * @hidden
   */
  public static TruncatedArray fromDriverClass(MongoClientContext clientContext,
      com.mongodb.client.model.changestream.TruncatedArray from) {
    requireNonNull(from, "from is null");
    TruncatedArray result = new TruncatedArray();
    try {
      result.field = from.getField();
    } catch (Exception ex) {
      result.fieldException = ex;
    }
    try {
      result.newSize = from.getNewSize();
    } catch (Exception ex) {
      result.newSizeException = ex;
    }
    return result;
  }
}
