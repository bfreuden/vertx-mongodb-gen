package io.vertx.mongo.client.model;

import io.vertx.codegen.annotations.DataObject;
import java.lang.Boolean;

/**
 *  The options to apply to a bulk write.
 *
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class BulkWriteOptions {
  /**
   * true if the writes should be ordered
   */
  private Boolean ordered;

  /**
   * If true, allows the write to opt-out of document level validation.
   */
  private Boolean bypassDocumentValidation;

  /**
   *  If true, then when a write fails, return without performing the remaining
   *  writes. If false, then when a write fails, continue with the remaining writes, if any.
   *  Defaults to true.
   *
   *  @param ordered true if the writes should be ordered
   *  @return this
   */
  public BulkWriteOptions ordered(Boolean ordered) {
    return this;
  }

  /**
   *  If true, then when a write fails, return without performing the remaining
   *  writes. If false, then when a write fails, continue with the remaining writes, if any.
   *  Defaults to true.
   *
   *  @return true if the writes are ordered
   */
  public Boolean isOrdered() {
    return ordered;
  }

  /**
   *  Sets the bypass document level validation flag.
   *
   *  @param bypassDocumentValidation If true, allows the write to opt-out of document level validation.
   *  @return this
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public BulkWriteOptions bypassDocumentValidation(Boolean bypassDocumentValidation) {
    return this;
  }

  /**
   *  Gets the the bypass document level validation flag
   *
   *  @return the bypass document level validation flag
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public Boolean isBypassDocumentValidation() {
    return bypassDocumentValidation;
  }

  /**
   * @hidden
   */
  public com.mongodb.client.model.BulkWriteOptions toDriverClass() {
    com.mongodb.client.model.BulkWriteOptions result = new com.mongodb.client.model.BulkWriteOptions();
    if (this.ordered != null) {
      result.ordered(this.ordered);
    }
    if (this.bypassDocumentValidation != null) {
      result.bypassDocumentValidation(this.bypassDocumentValidation);
    }
    return result;
  }
}
