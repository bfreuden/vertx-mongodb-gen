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
package io.vertx.mongo.bulk;

import static java.util.Objects.requireNonNull;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Object;

@DataObject(
    generateConverter = true
)
public class BulkWriteUpsert {
  private int index;

  private Object id;

  private BulkWriteUpsert() {
  }

  /**
   *  Gets the index of the upserted item based on the order it was added to the bulk write operation.
   *
   *  @return the index
   */
  public int getIndex() {
    return index;
  }

  /**
   *  Gets the id of the upserted item.
   *
   *  @return the id
   */
  public Object getId() {
    return id;
  }

  /**
   * @return mongo object
   * @hidden
   */
  public static BulkWriteUpsert fromDriverClass(com.mongodb.bulk.BulkWriteUpsert from) {
    requireNonNull(from, "from is null");
    BulkWriteUpsert result = new BulkWriteUpsert();
    result.index = from.getIndex();
    result.id = ConversionUtilsImpl.INSTANCE.toObject(from.getId());
    return result;
  }
}
