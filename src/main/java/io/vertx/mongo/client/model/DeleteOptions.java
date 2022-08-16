package io.vertx.mongo.client.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.lang.String;

/**
 *  The options to apply when deleting documents.
 *
 *  @since 3.4
 *  @mongodb.driver.manual tutorial/remove-documents/ Remove documents
 *  @mongodb.driver.manual reference/command/delete/ Delete Command
 */
@DataObject(
    generateConverter = true
)
public class DeleteOptions {
  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * a document describing the index which should be used for this operation.
   */
  private JsonObject hint;

  /**
   * the name of the index which should be used for the operation
   */
  private String hintString;

  /**
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   *  @mongodb.server.release 3.4
   */
  public DeleteOptions collation(Collation collation) {
    return this;
  }

  /**
   *  Returns the collation options
   *
   *  @return the collation options
   *  @mongodb.server.release 3.4
   */
  public Collation getCollation() {
    return collation;
  }

  /**
   *  Sets the hint to apply.
   *
   *  @param hint a document describing the index which should be used for this operation.
   *  @return this
   *  @since 4.1
   *  @mongodb.server.release 4.4
   */
  public DeleteOptions hint(JsonObject hint) {
    return this;
  }

  /**
   *  Gets the hint to apply.
   *
   *  @return the hint, which should describe an existing index
   *  @since 4.1
   *  @mongodb.server.release 4.4
   */
  public JsonObject getHint() {
    return hint;
  }

  /**
   *  Sets the hint to apply.
   *
   *  <p>Note: If {@link DeleteOptions#hint(JsonObject)} is set that will be used instead of any hint string.</p>
   *
   *  @param hint the name of the index which should be used for the operation
   *  @return this
   *  @since 4.1
   *  @mongodb.server.release 4.4
   */
  public DeleteOptions hintString(String hint) {
    return this;
  }

  /**
   *  Gets the hint string to apply.
   *
   *  @return the hint string, which should be the name of an existing index
   *  @since 4.1
   *  @mongodb.server.release 4.4
   */
  public String getHintString() {
    return hintString;
  }
}
