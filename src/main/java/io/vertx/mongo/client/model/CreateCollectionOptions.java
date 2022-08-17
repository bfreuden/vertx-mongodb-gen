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

  /**
   *  Sets the maximum number of documents allowed in a capped collection.
   *
   *  @param maxDocuments the maximum number of documents allowed in capped collection
   *  @return this
   */
  public CreateCollectionOptions maxDocuments(Long maxDocuments) {
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
  public CreateCollectionOptions capped(Boolean capped) {
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
  public CreateCollectionOptions sizeInBytes(Long sizeInBytes) {
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
  public CreateCollectionOptions storageEngineOptions(JsonObject storageEngineOptions) {
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
  public CreateCollectionOptions indexOptionDefaults(IndexOptionDefaults indexOptionDefaults) {
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
  public CreateCollectionOptions validationOptions(ValidationOptions validationOptions) {
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
  public CreateCollectionOptions collation(Collation collation) {
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
