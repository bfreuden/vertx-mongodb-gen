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
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Boolean;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  Options for creating a collection
 *
 *  @mongodb.driver.manual reference/command/create/ Create Collection
 *  @mongodb.driver.manual core/timeseries-collections/ Time-series collections
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
   * the expire-after duration.  After conversion to seconds using
   */
  private Long expireAfter;

  /**
   * the time-series options
   */
  private TimeSeriesOptions timeSeriesOptions;

  /**
   * the clustered index options
   */
  private ClusteredIndexOptions clusteredIndexOptions;

  /**
   * the change stream pre- and post- images options
   */
  private ChangeStreamPreAndPostImagesOptions changeStreamPreAndPostImagesOptions;

  /**
   * the encrypted fields document
   */
  private JsonObject encryptedFields;

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
   *  Sets the expire-after option.
   *
   *  <p>
   *  A duration indicating after how long old time-series data should be deleted. Partial seconds are ignored.
   *  </p>
   *  <p>
   *  Currently applies only to time-series collections, so if this value is set then so must the time-series options
   *  </p>
   *  @param expireAfter the expire-after duration.  After conversion to seconds using (in milliseconds)
   *  {@link TimeUnit#convert(long, java.util.concurrent.TimeUnit)}, the value must be &gt;= 0.  A value of 0 indicates no expiration.
   *  @return this
   *  @since 4.3
   *  @see #timeSeriesOptions(TimeSeriesOptions)
   *  @mongodb.driver.manual core/timeseries-collections/ Time-series collections
   */
  public CreateCollectionOptions setExpireAfter(Long expireAfter) {
    this.expireAfter = expireAfter;
    return this;
  }

  /**
   *  Returns the expire-after option.  The default value is 0, which indicates no expiration.
   *
   *  @return the expire-after option, which may be null.
   *  @since 4.3
   *  @mongodb.driver.manual core/timeseries-collections/ Time-series collections
   */
  public Long getExpireAfter() {
    return expireAfter;
  }

  /**
   *  Sets the time-series collection options.
   *
   *  @param timeSeriesOptions the time-series options
   *  @return this
   *  @since 4.3
   *  @mongodb.driver.manual core/timeseries-collections/ Time-series collections
   */
  public CreateCollectionOptions setTimeSeriesOptions(TimeSeriesOptions timeSeriesOptions) {
    this.timeSeriesOptions = timeSeriesOptions;
    return this;
  }

  /**
   *  Gets the time series collection options.
   *
   *  @return the options for a time-series collection
   *  @since 4.3
   *  @mongodb.driver.manual core/timeseries-collections/ Time-series collections
   */
  public TimeSeriesOptions getTimeSeriesOptions() {
    return timeSeriesOptions;
  }

  /**
   *  Sets the clustered index collection options.
   *
   *  @param clusteredIndexOptions the clustered index options
   *  @return this
   *  @since 4.7
   */
  public CreateCollectionOptions setClusteredIndexOptions(
      ClusteredIndexOptions clusteredIndexOptions) {
    this.clusteredIndexOptions = clusteredIndexOptions;
    return this;
  }

  /**
   *  Gets the clustered index collection options.
   *
   *  @return the options for a clustered index
   *  @since 4.7
   */
  public ClusteredIndexOptions getClusteredIndexOptions() {
    return clusteredIndexOptions;
  }

  /**
   *  Sets the change stream pre- and post- images options.
   *
   *  @param changeStreamPreAndPostImagesOptions the change stream pre- and post- images options
   *  @return this
   *  @since 4.7
   */
  public CreateCollectionOptions setChangeStreamPreAndPostImagesOptions(
      ChangeStreamPreAndPostImagesOptions changeStreamPreAndPostImagesOptions) {
    this.changeStreamPreAndPostImagesOptions = changeStreamPreAndPostImagesOptions;
    return this;
  }

  /**
   *  Gets change stream pre- and post- images options.
   *
   *  @return the options for change stream pre- and post- images
   *  @since 4.7
   */
  public ChangeStreamPreAndPostImagesOptions getChangeStreamPreAndPostImagesOptions() {
    return changeStreamPreAndPostImagesOptions;
  }

  /**
   *  Sets the encrypted fields
   *
   *  <p>Explicitly set encrypted fields.</p>
   *  <p>Note: If not set the driver will lookup the namespace in {@link AutoEncryptionSettings#getEncryptedFieldsMap()}</p>
   *  @param encryptedFields the encrypted fields document
   *  @return this
   *  @since 4.7
   *  @mongodb.driver.manual core/security-client-side-encryption/ Client side encryption
   *  @mongodb.server.release 6.0
   */
  public CreateCollectionOptions setEncryptedFields(JsonObject encryptedFields) {
    this.encryptedFields = encryptedFields;
    return this;
  }

  /**
   *  Gets any explicitly set encrypted fields.
   *
   *  <p>Note: If not set the driver will lookup the namespace in {@link AutoEncryptionSettings#getEncryptedFieldsMap()}</p>
   *  @return the encrypted fields document
   *  @since 4.7
   *  @mongodb.server.release 6.0
   */
  public JsonObject getEncryptedFields() {
    return encryptedFields;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.CreateCollectionOptions toDriverClass(
      MongoClientContext clientContext) {
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
      result.storageEngineOptions(clientContext.getMapper().toBson(this.storageEngineOptions));
    }
    if (this.indexOptionDefaults != null) {
      result.indexOptionDefaults(this.indexOptionDefaults.toDriverClass(clientContext));
    }
    if (this.validationOptions != null) {
      result.validationOptions(this.validationOptions.toDriverClass(clientContext));
    }
    if (this.collation != null) {
      result.collation(this.collation.toDriverClass(clientContext));
    }
    if (this.expireAfter != null) {
      result.expireAfter(this.expireAfter, TimeUnit.MILLISECONDS);
    }
    if (this.timeSeriesOptions != null) {
      result.timeSeriesOptions(this.timeSeriesOptions.toDriverClass(clientContext));
    }
    if (this.clusteredIndexOptions != null) {
      result.clusteredIndexOptions(this.clusteredIndexOptions.toDriverClass(clientContext));
    }
    if (this.changeStreamPreAndPostImagesOptions != null) {
      result.changeStreamPreAndPostImagesOptions(this.changeStreamPreAndPostImagesOptions.toDriverClass(clientContext));
    }
    if (this.encryptedFields != null) {
      result.encryptedFields(clientContext.getMapper().toBson(this.encryptedFields));
    }
    return result;
  }
}
