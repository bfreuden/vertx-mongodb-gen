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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Exception;
import java.lang.String;
import java.util.List;

@DataObject(
    generateConverter = true
)
public class UpdateDescription {
  private List<String> removedFields;

  private JsonObject updatedFields;

  private Exception removedFieldsException;

  private Exception updatedFieldsException;

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
   *  Returns the updatedFields
   *
   *  @return the updatedFields
   */
  public JsonObject getUpdatedFields() {
    if (updatedFieldsException != null)  {
      throw new RuntimeException(updatedFieldsException);
    }
    return updatedFields;
  }

  /**
   * @return mongo object
   * @hidden
   */
  public static UpdateDescription fromDriverClass(
      com.mongodb.client.model.changestream.UpdateDescription from) {
    requireNonNull(from, "from is null");
    UpdateDescription result = new UpdateDescription();
    try {
      result.removedFields = from.getRemovedFields();
    } catch (Exception ex) {
      result.removedFieldsException = ex;
    }
    try {
      result.updatedFields = ConversionUtilsImpl.INSTANCE.toJsonObject(from.getUpdatedFields());
    } catch (Exception ex) {
      result.updatedFieldsException = ex;
    }
    return result;
  }
}
