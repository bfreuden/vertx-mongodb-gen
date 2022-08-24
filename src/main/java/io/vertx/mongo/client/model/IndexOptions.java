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
import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.TimeUnit;

/**
 *  The options to apply to the creation of an index.
 *
 *  @mongodb.driver.manual reference/command/createIndexes Index options
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class IndexOptions {
  /**
   * true if should create the index in the background
   */
  private Boolean background;

  /**
   * if the index should be unique
   */
  private Boolean unique;

  /**
   * of the index
   */
  private String name;

  /**
   * if true, the index only references documents with the specified field
   */
  private Boolean sparse;

  /**
   * the time to live for documents in the collection
   */
  private Long expireAfter;

  /**
   * the index version number
   */
  private Integer version;

  /**
   * the weighting object
   */
  private JsonObject weights;

  /**
   * the language for the text index.
   */
  private String defaultLanguage;

  /**
   * the name of the field that contains the language string.
   */
  private String languageOverride;

  /**
   * the text index version number.
   */
  private Integer textVersion;

  /**
   * the 2dsphere index version number.
   */
  private Integer sphereVersion;

  /**
   * the number of precision of the stored geohash value
   */
  private Integer bits;

  /**
   * the lower inclusive boundary for the longitude and latitude values
   */
  private Double min;

  /**
   * the upper inclusive boundary for the longitude and latitude values
   */
  private Double max;

  /**
   * the specified the number of units within which to group the location values for geoHaystack Indexes
   */
  private Double bucketSize;

  /**
   * the storage engine options
   */
  private JsonObject storageEngine;

  /**
   * the filter expression for the documents to be included in the index
   */
  private JsonObject partialFilterExpression;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the wildcard projection
   */
  private JsonObject wildcardProjection;

  /**
   * true if the index should be hidden
   */
  private Boolean hidden;

  /**
   *  Should the index should be created in the background
   *
   *  @param background true if should create the index in the background
   *  @return this
   */
  public IndexOptions background(Boolean background) {
    this.background = background;
    return this;
  }

  /**
   *  Create the index in the background
   *
   *  @return true if should create the index in the background
   */
  public Boolean isBackground() {
    return background;
  }

  /**
   *  Should the index should be unique.
   *
   *  @param unique if the index should be unique
   *  @return this
   */
  public IndexOptions unique(Boolean unique) {
    this.unique = unique;
    return this;
  }

  /**
   *  Gets if the index should be unique.
   *
   *  @return true if the index should be unique
   */
  public Boolean isUnique() {
    return unique;
  }

  /**
   *  Sets the name of the index.
   *
   *  @param name of the index
   *  @return this
   */
  public IndexOptions name(String name) {
    this.name = name;
    return this;
  }

  /**
   *  Gets the name of the index.
   *
   *  @return the name of the index
   */
  public String getName() {
    return name;
  }

  /**
   *  Should the index only references documents with the specified field
   *
   *  @param sparse if true, the index only references documents with the specified field
   *  @return this
   */
  public IndexOptions sparse(Boolean sparse) {
    this.sparse = sparse;
    return this;
  }

  /**
   *  If true, the index only references documents with the specified field
   *
   *  @return if the index should only reference documents with the specified field
   */
  public Boolean isSparse() {
    return sparse;
  }

  /**
   *  Sets the time to live for documents in the collection
   *
   *  @param expireAfter the time to live for documents in the collection (in milliseconds)
   *  @return this
   *  @mongodb.driver.manual tutorial/expire-data TTL
   */
  public IndexOptions expireAfter(Long expireAfter) {
    this.expireAfter = expireAfter;
    return this;
  }

  /**
   *  Gets the time to live for documents in the collection
   *
   *  @return the time to live for documents in the collection
   *  @mongodb.driver.manual tutorial/expire-data TTL
   */
  public Long getExpireAfter() {
    return expireAfter;
  }

  /**
   *  Sets the index version number.
   *
   *  @param version the index version number
   *  @return this
   */
  public IndexOptions version(Integer version) {
    this.version = version;
    return this;
  }

  /**
   *  Gets the index version number.
   *
   *  @return the index version number
   */
  public Integer getVersion() {
    return version;
  }

  /**
   *  Sets the weighting object for use with a text index.
   *
   *  <p>An document that represents field and weight pairs. The weight is an integer ranging from 1 to 99,999 and denotes the significance
   *  of the field relative to the other indexed fields in terms of the score.</p>
   *
   *  @param weights the weighting object
   *  @return this
   *  @mongodb.driver.manual tutorial/control-results-of-text-search Control Search Results with Weights
   */
  public IndexOptions weights(JsonObject weights) {
    this.weights = weights;
    return this;
  }

  /**
   *  Gets the weighting object for use with a text index
   *
   *  <p>A document that represents field and weight pairs. The weight is an integer ranging from 1 to 99,999 and denotes the significance
   *  of the field relative to the other indexed fields in terms of the score.</p>
   *
   *  @return the weighting object
   *  @mongodb.driver.manual tutorial/control-results-of-text-search Control Search Results with Weights
   */
  public JsonObject getWeights() {
    return weights;
  }

  /**
   *  Sets the language for the text index.
   *
   *  <p>The language that determines the list of stop words and the rules for the stemmer and tokenizer.</p>
   *
   *  @param defaultLanguage the language for the text index.
   *  @return this
   *  @mongodb.driver.manual reference/text-search-languages Text Search languages
   */
  public IndexOptions defaultLanguage(String defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
    return this;
  }

  /**
   *  Gets the language for a text index.
   *
   *  <p>The language that determines the list of stop words and the rules for the stemmer and tokenizer.</p>
   *
   *  @return the language for a text index.
   *  @mongodb.driver.manual reference/text-search-languages Text Search languages
   */
  public String getDefaultLanguage() {
    return defaultLanguage;
  }

  /**
   *  Sets the name of the field that contains the language string.
   *
   *  <p>For text indexes, the name of the field, in the collection's documents, that contains the override language for the document.</p>
   *
   *  @param languageOverride the name of the field that contains the language string.
   *  @return this
   *  @mongodb.driver.manual tutorial/specify-language-for-text-index/#specify-language-field-text-index-example Language override
   */
  public IndexOptions languageOverride(String languageOverride) {
    this.languageOverride = languageOverride;
    return this;
  }

  /**
   *  Gets the name of the field that contains the language string.
   *
   *  <p>For text indexes, the name of the field, in the collection's documents, that contains the override language for the document.</p>
   *
   *  @return the name of the field that contains the language string.
   *  @mongodb.driver.manual tutorial/specify-language-for-text-index/#specify-language-field-text-index-example Language override
   */
  public String getLanguageOverride() {
    return languageOverride;
  }

  /**
   *  Set the text index version number.
   *
   *  @param textVersion the text index version number.
   *  @return this
   */
  public IndexOptions textVersion(Integer textVersion) {
    this.textVersion = textVersion;
    return this;
  }

  /**
   *  The text index version number.
   *
   *  @return the text index version number.
   */
  public Integer getTextVersion() {
    return textVersion;
  }

  /**
   *  Sets the 2dsphere index version number.
   *
   *  @param sphereVersion the 2dsphere index version number.
   *  @return this
   */
  public IndexOptions sphereVersion(Integer sphereVersion) {
    this.sphereVersion = sphereVersion;
    return this;
  }

  /**
   *  Gets the 2dsphere index version number.
   *
   *  @return the 2dsphere index version number
   */
  public Integer getSphereVersion() {
    return sphereVersion;
  }

  /**
   *  Sets the number of precision of the stored geohash value of the location data in 2d indexes.
   *
   *  @param bits the number of precision of the stored geohash value
   *  @return this
   */
  public IndexOptions bits(Integer bits) {
    this.bits = bits;
    return this;
  }

  /**
   *  Gets the number of precision of the stored geohash value of the location data in 2d indexes.
   *
   *  @return the number of precision of the stored geohash value
   */
  public Integer getBits() {
    return bits;
  }

  /**
   *  Sets the lower inclusive boundary for the longitude and latitude values for 2d indexes..
   *
   *  @param min the lower inclusive boundary for the longitude and latitude values
   *  @return this
   */
  public IndexOptions min(Double min) {
    this.min = min;
    return this;
  }

  /**
   *  Gets the lower inclusive boundary for the longitude and latitude values for 2d indexes..
   *
   *  @return the lower inclusive boundary for the longitude and latitude values.
   */
  public Double getMin() {
    return min;
  }

  /**
   *  Sets the upper inclusive boundary for the longitude and latitude values for 2d indexes..
   *
   *  @param max the upper inclusive boundary for the longitude and latitude values
   *  @return this
   */
  public IndexOptions max(Double max) {
    this.max = max;
    return this;
  }

  /**
   *  Gets the upper inclusive boundary for the longitude and latitude values for 2d indexes..
   *
   *  @return the upper inclusive boundary for the longitude and latitude values.
   */
  public Double getMax() {
    return max;
  }

  /**
   *  Sets the specified the number of units within which to group the location values for geoHaystack Indexes
   *
   *  @param bucketSize the specified the number of units within which to group the location values for geoHaystack Indexes
   *  @return this
   *  @mongodb.driver.manual core/geohaystack/ geoHaystack Indexes
   */
  public IndexOptions bucketSize(Double bucketSize) {
    this.bucketSize = bucketSize;
    return this;
  }

  /**
   *  Gets the specified the number of units within which to group the location values for geoHaystack Indexes
   *
   *  @return the specified the number of units within which to group the location values for geoHaystack Indexes
   *  @mongodb.driver.manual core/geohaystack/ geoHaystack Indexes
   */
  public Double getBucketSize() {
    return bucketSize;
  }

  /**
   *  Sets the storage engine options document for this index.
   *
   *  @param storageEngine the storage engine options
   *  @return this
   *  @mongodb.server.release 3.0
   */
  public IndexOptions storageEngine(JsonObject storageEngine) {
    this.storageEngine = storageEngine;
    return this;
  }

  /**
   *  Gets the storage engine options document for this index.
   *
   *  @return the storage engine options
   *  @mongodb.server.release 3.0
   */
  public JsonObject getStorageEngine() {
    return storageEngine;
  }

  /**
   *  Sets the filter expression for the documents to be included in the index
   *
   *  @param partialFilterExpression the filter expression for the documents to be included in the index
   *  @return this
   *  @mongodb.server.release 3.2
   *  @since 3.2
   */
  public IndexOptions partialFilterExpression(JsonObject partialFilterExpression) {
    this.partialFilterExpression = partialFilterExpression;
    return this;
  }

  /**
   *  Get the filter expression for the documents to be included in the index or null if not set
   *
   *  @return the filter expression for the documents to be included in the index or null if not set
   *  @mongodb.server.release 3.2
   *  @since 3.2
   */
  public JsonObject getPartialFilterExpression() {
    return partialFilterExpression;
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
  public IndexOptions collation(Collation collation) {
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
   *  Sets the wildcard projection of a wildcard index
   *
   *  @param wildcardProjection the wildcard projection
   *  @return this
   *  @mongodb.server.release 4.2
   *  @since 3.10
   */
  public IndexOptions wildcardProjection(JsonObject wildcardProjection) {
    this.wildcardProjection = wildcardProjection;
    return this;
  }

  /**
   *  Gets the wildcard projection of a wildcard index
   *
   *  @return the wildcard projection
   *  @mongodb.server.release 4.2
   *  @since 3.10
   */
  public JsonObject getWildcardProjection() {
    return wildcardProjection;
  }

  /**
   *  Should the index not be used by the query planner when executing operations.
   *
   *  @param hidden true if the index should be hidden
   *  @return this
   *  @mongodb.server.release 4.4
   *  @since 4.1
   */
  public IndexOptions hidden(Boolean hidden) {
    this.hidden = hidden;
    return this;
  }

  /**
   *  Gets whether the index should not be used by the query planner when executing operations.
   *
   *  @return true if the index should not be used by the query planner when executing operations.
   *  @mongodb.server.release 4.4
   *  @since 4.1
   */
  public Boolean isHidden() {
    return hidden;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.IndexOptions toDriverClass() {
    com.mongodb.client.model.IndexOptions result = new com.mongodb.client.model.IndexOptions();
    if (this.background != null) {
      result.background(this.background);
    }
    if (this.unique != null) {
      result.unique(this.unique);
    }
    if (this.name != null) {
      result.name(this.name);
    }
    if (this.sparse != null) {
      result.sparse(this.sparse);
    }
    if (this.expireAfter != null) {
      result.expireAfter(this.expireAfter, TimeUnit.MILLISECONDS);
    }
    if (this.version != null) {
      result.version(this.version);
    }
    if (this.weights != null) {
      result.weights(ConversionUtilsImpl.INSTANCE.toBson(this.weights));
    }
    if (this.defaultLanguage != null) {
      result.defaultLanguage(this.defaultLanguage);
    }
    if (this.languageOverride != null) {
      result.languageOverride(this.languageOverride);
    }
    if (this.textVersion != null) {
      result.textVersion(this.textVersion);
    }
    if (this.sphereVersion != null) {
      result.sphereVersion(this.sphereVersion);
    }
    if (this.bits != null) {
      result.bits(this.bits);
    }
    if (this.min != null) {
      result.min(this.min);
    }
    if (this.max != null) {
      result.max(this.max);
    }
    if (this.bucketSize != null) {
      result.bucketSize(this.bucketSize);
    }
    if (this.storageEngine != null) {
      result.storageEngine(ConversionUtilsImpl.INSTANCE.toBson(this.storageEngine));
    }
    if (this.partialFilterExpression != null) {
      result.partialFilterExpression(ConversionUtilsImpl.INSTANCE.toBson(this.partialFilterExpression));
    }
    if (this.collation != null) {
      result.collation(this.collation.toDriverClass());
    }
    if (this.wildcardProjection != null) {
      result.wildcardProjection(ConversionUtilsImpl.INSTANCE.toBson(this.wildcardProjection));
    }
    if (this.hidden != null) {
      result.hidden(this.hidden);
    }
    return result;
  }
}
