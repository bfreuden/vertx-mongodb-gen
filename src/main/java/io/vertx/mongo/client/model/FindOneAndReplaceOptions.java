package io.vertx.mongo.client.model;

import com.mongodb.client.model.ReturnDocument;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.String;

/**
 *  The options to apply to an operation that atomically finds a document and replaces it.
 *
 *  @mongodb.driver.manual reference/command/findAndModify/
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class FindOneAndReplaceOptions {
  /**
   * the project document, which may be null.
   */
  private JsonObject projection;

  /**
   * the sort criteria, which may be null.
   */
  private JsonObject sort;

  /**
   * true if a new document should be inserted if there are no matches to the query filter
   */
  private Boolean upsert;

  /**
   * set whether to return the document before it was replaced or after
   */
  private ReturnDocument returnDocument;

  /**
   *  the max time
   */
  private Long maxTime;

  /**
   * If true, allows the write to opt-out of document level validation.
   */
  private Boolean bypassDocumentValidation;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the hint
   */
  private JsonObject hint;

  /**
   * the name of the index which should be used for the operation
   */
  private String hintString;

  /**
   *  Sets a document describing the fields to return for all matching documents.
   *
   *  @param projection the project document, which may be null.
   *  @return this
   *  @mongodb.driver.manual tutorial/project-fields-from-query-results Projection
   */
  public FindOneAndReplaceOptions projection(JsonObject projection) {
    return this;
  }

  /**
   *  Gets a document describing the fields to return for all matching documents.
   *
   *  @return the project document, which may be null
   *  @mongodb.driver.manual tutorial/project-fields-from-query-results Projection
   */
  public JsonObject getProjection() {
    return projection;
  }

  /**
   *  Sets the sort criteria to apply to the query.
   *
   *  @param sort the sort criteria, which may be null.
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.sort/ Sort
   */
  public FindOneAndReplaceOptions sort(JsonObject sort) {
    return this;
  }

  /**
   *  Gets the sort criteria to apply to the query. The default is null, which means that the documents will be returned in an undefined
   *  order.
   *
   *  @return a document describing the sort criteria
   *  @mongodb.driver.manual reference/method/cursor.sort/ Sort
   */
  public JsonObject getSort() {
    return sort;
  }

  /**
   *  Set to true if a new document should be inserted if there are no matches to the query filter.
   *
   *  @param upsert true if a new document should be inserted if there are no matches to the query filter
   *  @return this
   */
  public FindOneAndReplaceOptions upsert(Boolean upsert) {
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
   *  Set whether to return the document before it was replaced or after
   *
   *  @param returnDocument set whether to return the document before it was replaced or after
   *  @return this
   */
  public FindOneAndReplaceOptions returnDocument(ReturnDocument returnDocument) {
    return this;
  }

  /**
   *  Gets the {@link ReturnDocument} value indicating whether to return the document before it was replaced or after
   *
   *  @return {@link ReturnDocument#BEFORE} if returning the document before it was replaced otherwise return {@link ReturnDocument#AFTER}
   */
  public ReturnDocument getReturnDocument() {
    return returnDocument;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public FindOneAndReplaceOptions maxTime(Long maxTime) {
    return this;
  }

  /**
   *  Gets the maximum execution time for the find one and replace operation.
   *
   *  @return the max time
   */
  public Long getMaxTime() {
    return maxTime;
  }

  /**
   *  Sets the bypass document level validation flag.
   *
   *  @param bypassDocumentValidation If true, allows the write to opt-out of document level validation.
   *  @return this
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public FindOneAndReplaceOptions bypassDocumentValidation(Boolean bypassDocumentValidation) {
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
  public FindOneAndReplaceOptions collation(Collation collation) {
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
   *  Sets the hint for which index to use. A null value means no hint is set.
   *
   *  @param hint the hint
   *  @return this
   *  @since 4.1
   */
  public FindOneAndReplaceOptions hint(JsonObject hint) {
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
   *  @param hint the name of the index which should be used for the operation
   *  @return this
   *  @since 4.1
   */
  public FindOneAndReplaceOptions hintString(String hint) {
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
