package io.vertx.mongo.client.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.lang.Boolean;
import java.lang.String;
import java.util.List;

/**
 *  The options to apply when updating documents.
 *
 *  @since 3.0
 *  @mongodb.driver.manual tutorial/modify-documents/ Updates
 *  @mongodb.driver.manual reference/operator/update/ Update Operators
 *  @mongodb.driver.manual reference/command/update/ Update Command
 */
@DataObject(
    generateConverter = true
)
public class UpdateOptions {
  /**
   * true if a new document should be inserted if there are no matches to the query filter
   */
  private Boolean upsert;

  /**
   * If true, allows the write to opt-out of document level validation.
   */
  private Boolean bypassDocumentValidation;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the array filters, which may be null
   */
  private List arrayFilters;

  /**
   * the hint
   */
  private JsonObject hint;

  /**
   * the name of the index which should be used for the operation
   */
  private String hintString;

  /**
   *  Set to true if a new document should be inserted if there are no matches to the query filter.
   *
   *  @param upsert true if a new document should be inserted if there are no matches to the query filter
   *  @return this
   */
  public UpdateOptions upsert(Boolean upsert) {
    return this;
  }

  /**
   *  Returns true if a new document should be inserted if there are no matches to the query filter.  The default is false.
   *
   *  @return true if a new document should be inserted if there are no matches to the query filter
   */
  public Boolean isUpsert() {
    return upsert;
  }

  /**
   *  Sets the bypass document level validation flag.
   *
   *  @param bypassDocumentValidation If true, allows the write to opt-out of document level validation.
   *  @return this
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public UpdateOptions bypassDocumentValidation(Boolean bypassDocumentValidation) {
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
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   *  @since 3.4
   *  @mongodb.server.release 3.4
   */
  public UpdateOptions collation(Collation collation) {
    return this;
  }

  /**
   *  Returns the collation options
   *
   *  @return the collation options
   *  @since 3.4
   *  @mongodb.server.release 3.4
   */
  public Collation getCollation() {
    return collation;
  }

  /**
   *  Sets the array filters option
   *
   *  @param arrayFilters the array filters, which may be null
   *  @return this
   *  @since 3.6
   *  @mongodb.server.release 3.6
   */
  public UpdateOptions arrayFilters(List arrayFilters) {
    return this;
  }

  /**
   *  Returns the array filters option
   *
   *  @return the array filters, which may be null
   *  @since 3.6
   *  @mongodb.server.release 3.6
   */
  public List getArrayFilters() {
    return arrayFilters;
  }

  /**
   *  Sets the hint for which index to use. A null value means no hint is set.
   *
   *  @param hint the hint
   *  @return this
   *  @since 4.1
   */
  public UpdateOptions hint(JsonObject hint) {
    return this;
  }

  /**
   *  Returns the hint for which index to use. The default is not to set a hint.
   *
   *  @return the hint
   *  @since 4.1
   */
  public JsonObject getHint() {
    return hint;
  }

  /**
   *  Sets the hint to apply.
   *
   *  @param hintString the name of the index which should be used for the operation
   *  @return this
   *  @since 4.1
   */
  public UpdateOptions hintString(String hintString) {
    return this;
  }

  /**
   *  Gets the hint string to apply.
   *
   *  @return the hint string, which should be the name of an existing index
   *  @since 4.1
   */
  public String getHintString() {
    return hintString;
  }
}
