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

import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.CollectionsConversionUtils;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Exception;
import java.lang.String;
import java.util.List;

public class UpdateDescription {
  private List<String> removedFields;

  private JsonObject updatedFields;

  private List<TruncatedArray> truncatedArrays;

  int __ctorIndex;

  private Exception removedFieldsException;

  private Exception updatedFieldsException;

  private Exception truncatedArraysException;

  private UpdateDescription() {
  }

  /**
   *  Returns the removedFields
   *
   *  @return the removedFields
   */
  public List<String> getRemovedFields() {
    if (removedFieldsException != null)  {
      throw new RuntimeException(removedFieldsException);
    }
    return removedFields;
  }

  /**
   *  Returns information about the updated fields excluding the fields reported via {@link #getTruncatedArrays()}.
   *  <p>
   *  Despite {@linkplain org.bson.BsonType#ARRAY array} fields reported via {@link #getTruncatedArrays()} being excluded from the
   *  information returned by this method, changes to fields of the elements of the array values may be reported via this method.
   *  For example, given the original field {@code "arrayField": ["foo", {"a": "bar"}, 1, 2, 3]}
   *  and the updated field {@code "arrayField": ["foo", {"a": "bar", "b": 3}]}, the following is how such a change may be reported:
   *  <table>
   *    <caption>An example showing how the aforementioned change may be reported</caption>
   *    <tr>
   *      <th>Method</th>
   *      <th>Result</th>
   *    </tr>
   *    <tr>
   *      <td>{@link #getUpdatedFields()}</td>
   *      <td>{"arrayField.1.b": 3}</td>
   *    </tr>
   *    <tr>
   *      <td>{@link #getTruncatedArrays()}</td>
   *      <td>{"field": "arrayField", "newSize": 2}</td>
   *    </tr>
   *  </table>
   *
   *  @return {@code updatedFields}.
   *  @see #getTruncatedArrays()
   */
  public JsonObject getUpdatedFields() {
    if (updatedFieldsException != null)  {
      throw new RuntimeException(updatedFieldsException);
    }
    return updatedFields;
  }

  /**
   *  Returns information about the updated fields of the {@linkplain org.bson.BsonType#ARRAY array} type
   *  when the changes are reported as truncations.
   *
   *  @return {@code truncatedArrays}.
   *  There are no guarantees on the mutability of the {@code List} returned.
   *  @see #getUpdatedFields()
   *  @since 4.3
   */
  public List<TruncatedArray> getTruncatedArrays() {
    if (truncatedArraysException != null)  {
      throw new RuntimeException(truncatedArraysException);
    }
    return truncatedArrays;
  }

  /**
   * @param from from
   * @return mongo object
   * @hidden
   */
  public static UpdateDescription fromDriverClass(MongoClientContext clientContext,
      com.mongodb.client.model.changestream.UpdateDescription from) {
    requireNonNull(from, "from is null");
    UpdateDescription result = new UpdateDescription();
    try {
      result.removedFields = from.getRemovedFields();
    } catch (Exception ex) {
      result.removedFieldsException = ex;
    }
    try {
      result.updatedFields = clientContext.getMapper().toJsonObject(from.getUpdatedFields());
    } catch (Exception ex) {
      result.updatedFieldsException = ex;
    }
    try {
      result.truncatedArrays = CollectionsConversionUtils.mapItems(from.getTruncatedArrays(), _item -> TruncatedArray.fromDriverClass(clientContext, _item));
    } catch (Exception ex) {
      result.truncatedArraysException = ex;
    }
    return result;
  }
}
