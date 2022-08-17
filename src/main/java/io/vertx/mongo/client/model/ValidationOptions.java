package io.vertx.mongo.client.model;

import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;

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

  /**
   *  Sets the validation rules for all
   *
   *  @param validator the validation rules
   *  @return this
   */
  public ValidationOptions validator(JsonObject validator) {
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
  public ValidationOptions validationLevel(ValidationLevel validationLevel) {
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
  public ValidationOptions validationAction(ValidationAction validationAction) {
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
  public com.mongodb.client.model.ValidationOptions toDriverClass() {
    com.mongodb.client.model.ValidationOptions result = new com.mongodb.client.model.ValidationOptions();
    if (this.validator != null) {
      result.validator(ConversionUtilsImpl.INSTANCE.toBson(this.validator));
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
