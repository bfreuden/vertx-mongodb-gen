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
