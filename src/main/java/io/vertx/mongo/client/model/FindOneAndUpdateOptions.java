package io.vertx.mongo.client.model;

import com.mongodb.client.model.ReturnDocument;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *  The options to apply to an operation that atomically finds a document and updates it.
 *
 *  @since 3.0
 *  @mongodb.driver.manual reference/command/findAndModify/
 */
@DataObject(
    generateConverter = true
)
public class FindOneAndUpdateOptions {
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
   * set whether to return the document before it was updated / inserted or after
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
   *  Sets a document describing the fields to return for all matching documents.
   *
   *  @param projection the project document, which may be null.
   *  @return this
   *  @mongodb.driver.manual tutorial/project-fields-from-query-results Projection
   */
  public FindOneAndUpdateOptions projection(JsonObject projection) {
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
  public FindOneAndUpdateOptions sort(JsonObject sort) {
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
  public FindOneAndUpdateOptions upsert(Boolean upsert) {
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
   *  Set whether to return the document before it was updated / inserted or after
   *
   *  @param returnDocument set whether to return the document before it was updated / inserted or after
   *  @return this
   */
  public FindOneAndUpdateOptions returnDocument(ReturnDocument returnDocument) {
    return this;
  }

  /**
   *  Gets the {@link ReturnDocument} value indicating whether to return the document before it was updated / inserted or after
   *
   *  @return {@link ReturnDocument#BEFORE} if returning the document before it was updated or inserted otherwise
   *  returns {@link ReturnDocument#AFTER}
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
  public FindOneAndUpdateOptions maxTime(Long maxTime) {
    return this;
  }

  /**
   *  Gets the maximum execution time for the find one and update operation.
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
  public FindOneAndUpdateOptions bypassDocumentValidation(Boolean bypassDocumentValidation) {
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
  public FindOneAndUpdateOptions collation(Collation collation) {
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
  public FindOneAndUpdateOptions arrayFilters(List arrayFilters) {
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
  public FindOneAndUpdateOptions hint(JsonObject hint) {
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
  public FindOneAndUpdateOptions hintString(String hint) {
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

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.FindOneAndUpdateOptions toDriverClass() {
    com.mongodb.client.model.FindOneAndUpdateOptions result = new com.mongodb.client.model.FindOneAndUpdateOptions();
    if (this.projection != null) {
      result.projection(ConversionUtilsImpl.INSTANCE.toBson(this.projection));
    }
    if (this.sort != null) {
      result.sort(ConversionUtilsImpl.INSTANCE.toBson(this.sort));
    }
    if (this.upsert != null) {
      result.upsert(this.upsert);
    }
    if (this.returnDocument != null) {
      result.returnDocument(this.returnDocument);
    }
    if (this.maxTime != null) {
      result.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.bypassDocumentValidation != null) {
      result.bypassDocumentValidation(this.bypassDocumentValidation);
    }
    if (this.collation != null) {
      result.collation(this.collation.toDriverClass());
    }
    if (this.arrayFilters != null) {
      result.arrayFilters(this.arrayFilters);
    }
    if (this.hint != null) {
      result.hint(ConversionUtilsImpl.INSTANCE.toBson(this.hint));
    }
    if (this.hintString != null) {
      result.hintString(this.hintString);
    }
    return result;
  }
}
