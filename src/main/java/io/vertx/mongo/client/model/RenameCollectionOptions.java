package io.vertx.mongo.client.model;

import io.vertx.codegen.annotations.DataObject;
import java.lang.Boolean;

/**
 *  The options to apply when renaming a collection.
 *
 *  @mongodb.driver.manual reference/command/renameCollection renameCollection
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class RenameCollectionOptions {
  /**
   * true if mongod should drop the target of renameCollection prior to renaming the collection.
   */
  private Boolean dropTarget;

  /**
   *  Sets if mongod should drop the target of renameCollection prior to renaming the collection.
   *
   *  @param dropTarget true if mongod should drop the target of renameCollection prior to renaming the collection.
   *  @return this
   */
  public RenameCollectionOptions dropTarget(Boolean dropTarget) {
    return this;
  }

  /**
   *  Gets if mongod should drop the target of renameCollection prior to renaming the collection.
   *
   *  @return true if mongod should drop the target of renameCollection prior to renaming the collection.
   */
  public Boolean isDropTarget() {
    return dropTarget;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.RenameCollectionOptions toDriverClass() {
    com.mongodb.client.model.RenameCollectionOptions result = new com.mongodb.client.model.RenameCollectionOptions();
    if (this.dropTarget != null) {
      result.dropTarget(this.dropTarget);
    }
    return result;
  }
}
