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
package io.vertx.mongo.client.model;

import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.MongoClientContext;

/**
 *  Validation options for documents being inserted or updated in a collection
 *
 *  @since 3.2
 *  @mongodb.server.release 3.2
 *  @mongodb.driver.manual reference/method/db.createCollection/ Create Collection
 */
@DataObject(
    generateConverter = true
)
public class ValidationOptions {
  /**
   * the validation rules
   */
  private JsonObject validator;

  /**
   * the validation level
   */
  private ValidationLevel validationLevel;

  /**
   * the validation action
   */
  private ValidationAction validationAction;

  public ValidationOptions() {
  }

  public ValidationOptions(JsonObject json) {
    ValidationOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    ValidationOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Sets the validation rules for all
   *
   *  @param validator the validation rules
   *  @return this
   */
  public ValidationOptions setValidator(JsonObject validator) {
    this.validator = validator;
    return this;
  }

  /**
   *  Gets the validation rules if set or null.
   *
   *  @return the validation rules if set or null
   */
  public JsonObject getValidator() {
    return validator;
  }

  /**
   *  Sets the validation level that determines how strictly MongoDB applies the validation rules to existing documents during an insert
   *  or update.
   *
   *  @param validationLevel the validation level
   *  @return this
   */
  public ValidationOptions setValidationLevel(ValidationLevel validationLevel) {
    this.validationLevel = validationLevel;
    return this;
  }

  /**
   *  Gets the {@link ValidationLevel} that determines how strictly MongoDB applies the validation rules to existing documents during an
   *  insert or update.
   *
   *  @return the ValidationLevel.
   */
  public ValidationLevel getValidationLevel() {
    return validationLevel;
  }

  /**
   *  Sets the {@link ValidationAction} that determines whether to error on invalid documents or just warn about the violations but allow
   *  invalid documents.
   *
   *  @param validationAction the validation action
   *  @return this
   */
  public ValidationOptions setValidationAction(ValidationAction validationAction) {
    this.validationAction = validationAction;
    return this;
  }

  /**
   *  Gets the {@link ValidationAction}.
   *
   *  @return the ValidationAction.
   */
  public ValidationAction getValidationAction() {
    return validationAction;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.ValidationOptions toDriverClass(
      MongoClientContext clientContext) {
    com.mongodb.client.model.ValidationOptions result = new com.mongodb.client.model.ValidationOptions();
    if (this.validator != null) {
      result.validator(clientContext.getMapper().toBson(this.validator));
    }
    if (this.validationLevel != null) {
      result.validationLevel(this.validationLevel);
    }
    if (this.validationAction != null) {
      result.validationAction(this.validationAction);
    }
    return result;
  }
}
