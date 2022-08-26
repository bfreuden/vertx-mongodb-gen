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
import java.lang.Exception;
import java.lang.Object;

@DataObject(
    generateConverter = true
)
public class BulkWriteInsert {
  private int index;

  private Object id;

  private Exception indexException;

  private Exception idException;

  private BulkWriteInsert() {
  }

  /**
   *  Gets the index of the inserted item based on the order it was added to the bulk write operation.
   *
   *  @return the index
   */
  public int getIndex() {
    if (indexException != null)  {
      throw new RuntimeException(indexException);
    }
    return index;
  }

  /**
   *  Gets the id of the inserted item.
   *
   *  @return the id
   */
  public Object getId() {
    if (idException != null)  {
      throw new RuntimeException(idException);
    }
    return id;
  }

  /**
   * @return mongo object
   * @hidden
   */
  public static BulkWriteInsert fromDriverClass(com.mongodb.bulk.BulkWriteInsert from) {
    requireNonNull(from, "from is null");
    BulkWriteInsert result = new BulkWriteInsert();
    try {
      result.index = from.getIndex();
    } catch (Exception ex) {
      result.indexException = ex;
    }
    try {
      result.id = ConversionUtilsImpl.INSTANCE.toObject(from.getId());
    } catch (Exception ex) {
      result.idException = ex;
    }
    return result;
  }
}
