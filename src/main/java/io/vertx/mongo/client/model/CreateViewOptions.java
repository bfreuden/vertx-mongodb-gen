package io.vertx.mongo.client.model;

import io.vertx.codegen.annotations.DataObject;

/**
 *  Options for creating a view
 *
 *  @since 3.4
 *  @mongodb.server.release 3.4
 *  @mongodb.driver.manual reference/command/create Create Command
 */
@DataObject(
    generateConverter = true
)
public class CreateViewOptions {
  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   */
  public CreateViewOptions collation(Collation collation) {
    return this;
  }

  /**
   *  Returns the collation options
   *
   *  @return the collation options
   */
  public Collation getCollation() {
    return collation;
  }

  /**
   * @hidden
   */
  public com.mongodb.client.model.CreateViewOptions toDriverClass() {
    com.mongodb.client.model.CreateViewOptions result = new com.mongodb.client.model.CreateViewOptions();
    if (this.collation != null) {
      result.collation(this.collation.toDriverClass());
    }
    return result;
  }
}
