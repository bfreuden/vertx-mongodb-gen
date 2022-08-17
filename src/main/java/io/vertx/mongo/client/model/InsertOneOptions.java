package io.vertx.mongo.client.model;

import io.vertx.codegen.annotations.DataObject;
import java.lang.Boolean;

/**
 *  The options to apply to an operation that inserts a single document into a collection.
 *
 *  @since 3.2
 *  @mongodb.server.release 3.2
 *  @mongodb.driver.manual tutorial/insert-documents/ Insert Tutorial
 *  @mongodb.driver.manual reference/command/insert/ Insert Command
 */
@DataObject(
    generateConverter = true
)
public class InsertOneOptions {
  /**
   * If true, allows the write to opt-out of document level validation.
   */
  private Boolean bypassDocumentValidation;

  /**
   *  Sets the bypass document level validation flag.
   *
   *  @param bypassDocumentValidation If true, allows the write to opt-out of document level validation.
   *  @return this
   */
  public InsertOneOptions bypassDocumentValidation(Boolean bypassDocumentValidation) {
    return this;
  }

  /**
   *  Gets the the bypass document level validation flag
   *
   *  @return the bypass document level validation flag
   */
  public Boolean isBypassDocumentValidation() {
    return bypassDocumentValidation;
  }

  /**
   * @hidden
   */
  public com.mongodb.client.model.InsertOneOptions toDriverClass() {
    com.mongodb.client.model.InsertOneOptions result = new com.mongodb.client.model.InsertOneOptions();
    if (this.bypassDocumentValidation != null) {
      result.bypassDocumentValidation(this.bypassDocumentValidation);
    }
    return result;
  }
}
