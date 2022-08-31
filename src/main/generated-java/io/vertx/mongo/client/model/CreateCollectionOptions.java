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
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Boolean;
import java.lang.Long;

/**
 *  Options for creating a collection
 *
 *  @mongodb.driver.manual reference/method/db.createCollection/ Create Collection
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class CreateCollectionOptions {
  /**
   * the maximum number of documents allowed in capped collection
   */
  private Long maxDocuments;

  /**
   * whether the collection is capped
   */
  private Boolean capped;

  /**
   * the maximum size of a capped collection.
   */
  private Long sizeInBytes;

  /**
   * the storage engine options
   */
  private JsonObject storageEngineOptions;

  /**
   * the index option defaults
   */
  private IndexOptionDefaults indexOptionDefaults;

  /**
   * the validation options
   */
  private ValidationOptions validationOptions;

  /**
   * the collation options to use
   */
  private Collation collation;

  public CreateCollectionOptions() {
  }

  public CreateCollectionOptions(JsonObject json) {
    CreateCollectionOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    CreateCollectionOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Sets the maximum number of documents allowed in a capped collection.
   *
   *  @param maxDocuments the maximum number of documents allowed in capped collection
   *  @return this
   */
  public CreateCollectionOptions setMaxDocuments(Long maxDocuments) {
    this.maxDocuments = maxDocuments;
    return this;
  }

  /**
   *  Gets the maximum number of documents allowed in a capped collection.
   *
   *  @return max number of documents in a capped collection
   */
  public Long getMaxDocuments() {
    return maxDocuments;
  }

  /**
   *  sets whether the collection is capped.
   *
   *  @param capped whether the collection is capped
   *  @return this
   */
  public CreateCollectionOptions setCapped(Boolean capped) {
    this.capped = capped;
    return this;
  }

  /**
   *  Gets whether the collection is capped.
   *
   *  @return whether the collection is capped
   */
  public Boolean isCapped() {
    return capped;
  }

  /**
   *  Gets the maximum size of in bytes of a capped collection.
   *
   *  @param sizeInBytes the maximum size of a capped collection.
   *  @return this
   */
  public CreateCollectionOptions setSizeInBytes(Long sizeInBytes) {
    this.sizeInBytes = sizeInBytes;
    return this;
  }

  /**
   *  Gets the maximum size in bytes of a capped collection.
   *
   *  @return the maximum size of a capped collection.
   */
  public Long getSizeInBytes() {
    return sizeInBytes;
  }

  /**
   *  Sets the storage engine options document defaults for the collection
   *
   *  @param storageEngineOptions the storage engine options
   *  @return this
   *  @mongodb.server.release 3.0
   */
  public CreateCollectionOptions setStorageEngineOptions(JsonObject storageEngineOptions) {
    this.storageEngineOptions = storageEngineOptions;
    return this;
  }

  /**
   *  Gets the storage engine options document for the collection.
   *
   *  @return the storage engine options
   *  @mongodb.server.release 3.0
   */
  public JsonObject getStorageEngineOptions() {
    return storageEngineOptions;
  }

  /**
   *  Sets the index option defaults for the collection.
   *
   *  @param indexOptionDefaults the index option defaults
   *  @return this
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public CreateCollectionOptions setIndexOptionDefaults(IndexOptionDefaults indexOptionDefaults) {
    this.indexOptionDefaults = indexOptionDefaults;
    return this;
  }

  /**
   *  Gets the index option defaults for the collection.
   *
   *  @return the index option defaults
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public IndexOptionDefaults getIndexOptionDefaults() {
    return indexOptionDefaults;
  }

  /**
   *  Sets the validation options for documents being inserted or updated in a collection
   *
   *  @param validationOptions the validation options
   *  @return this
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public CreateCollectionOptions setValidationOptions(ValidationOptions validationOptions) {
    this.validationOptions = validationOptions;
    return this;
  }

  /**
   *  Gets the validation options for documents being inserted or updated in a collection
   *
   *  @return the validation options
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public ValidationOptions getValidationOptions() {
    return validationOptions;
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
  public CreateCollectionOptions setCollation(Collation collation) {
    this.collation = collation;
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
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.CreateCollectionOptions toDriverClass() {
    com.mongodb.client.model.CreateCollectionOptions result = new com.mongodb.client.model.CreateCollectionOptions();
    if (this.maxDocuments != null) {
      result.maxDocuments(this.maxDocuments);
    }
    if (this.capped != null) {
      result.capped(this.capped);
    }
    if (this.sizeInBytes != null) {
      result.sizeInBytes(this.sizeInBytes);
    }
    if (this.storageEngineOptions != null) {
      result.storageEngineOptions(ConversionUtilsImpl.INSTANCE.toBson(this.storageEngineOptions));
    }
    if (this.indexOptionDefaults != null) {
      result.indexOptionDefaults(this.indexOptionDefaults.toDriverClass());
    }
    if (this.validationOptions != null) {
      result.validationOptions(this.validationOptions.toDriverClass());
    }
    if (this.collation != null) {
      result.collation(this.collation.toDriverClass());
    }
    return result;
  }
}
