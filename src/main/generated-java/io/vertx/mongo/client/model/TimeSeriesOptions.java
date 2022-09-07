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

import com.mongodb.client.model.TimeSeriesGranularity;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.String;

/**
 *  Options related to the creation of time-series collections.
 *
 *  @since 4.3
 *  @see CreateCollectionOptions
 *  @mongodb.driver.manual core/timeseries-collections/ Time-series collections
 */
@DataObject(
    generateConverter = true
)
public class TimeSeriesOptions {
  private String timeField;

  /**
   * the name of the meta field
   */
  private String metaField;

  /**
   * the time-series granularity
   */
  private TimeSeriesGranularity granularity;

  public TimeSeriesOptions() {
  }

  public TimeSeriesOptions(JsonObject json) {
    TimeSeriesOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    TimeSeriesOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Gets the name of the field holding the time value.
   *
   *  @return the name of the field holding the time value.
   */
  public String getTimeField() {
    return timeField;
  }

  /**
   *  Sets the name of the meta field.
   *  <p>
   *   The name of the field which contains metadata in each time series document. The metadata in the specified field should be data
   *   that is used to label a unique series of documents. The metadata should rarely, if ever, change.  This field is used to group
   *   related data and may be of any BSON type, except for array. This name may not be the same as the {@code timeField} or "_id".
   *  </p>
   *  @param metaField the name of the meta field
   *  @return this
   *  @see #getMetaField()
   */
  public TimeSeriesOptions setMetaField(String metaField) {
    this.metaField = metaField;
    return this;
  }

  /**
   *  Gets the name of the meta field.
   *
   *  @return the name of the meta field
   *  @see #metaField(String)
   */
  public String getMetaField() {
    return metaField;
  }

  /**
   *  Sets the granularity of the time-series data.
   *  <p>
   *  The default value is {@link TimeSeriesGranularity#SECONDS}.
   *  </p>
   *
   *  @param granularity the time-series granularity
   *  @return this
   *  @see #getGranularity()
   */
  public TimeSeriesOptions setGranularity(TimeSeriesGranularity granularity) {
    this.granularity = granularity;
    return this;
  }

  /**
   *  Gets the granularity of the time-series data.
   *
   *  @return the time-series granularity
   *  @see #granularity(TimeSeriesGranularity)
   */
  public TimeSeriesGranularity getGranularity() {
    return granularity;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.TimeSeriesOptions toDriverClass(
      MongoClientContext clientContext) {
    if (this.timeField == null) {
      throw new IllegalArgumentException("timeField is mandatory");
    }
    com.mongodb.client.model.TimeSeriesOptions result = new com.mongodb.client.model.TimeSeriesOptions(this.timeField);
    if (this.metaField != null) {
      result.metaField(this.metaField);
    }
    if (this.granularity != null) {
      result.granularity(this.granularity);
    }
    return result;
  }
}
