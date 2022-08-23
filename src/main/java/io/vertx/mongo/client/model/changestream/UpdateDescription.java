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
import java.lang.String;
import java.util.List;

@DataObject(
    generateConverter = true
)
public class UpdateDescription {
  private List<String> removedFields;

  private JsonObject updatedFields;

  /**
   *  Returns the removedFields
   *
   *  @return the removedFields
   */
  public List<String> getRemovedFields() {
    return removedFields;
  }

  /**
   *  Returns the updatedFields
   *
   *  @return the updatedFields
   */
  public JsonObject getUpdatedFields() {
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
    return result;
  }
}
