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
package io.vertx.mongo;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ReadConcernSerializer;
import io.vertx.mongo.impl.ReadPreferenceSerializer;
import io.vertx.mongo.impl.WriteConcernSerializer;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  Options to apply to transactions. The default values for the options depend on context.  For options specified per-transaction, the
 *  default values come from the default transaction options.  For the default transaction options themselves, the default values come from
 *  the MongoClient on which the session was started.
 *
 *  @see com.mongodb.session.ClientSession
 *  @see ClientSessionOptions
 *  @since 3.8
 *  @mongodb.server.release 4.0
 */
@DataObject(
    generateConverter = true
)
public class TransactionOptions {
  /**
   * the read concern
   */
  private ReadConcernSerializer readConcern;

  /**
   * the write concern, which must be acknowledged
   */
  private WriteConcernSerializer writeConcern;

  /**
   * the read preference, which currently must be primary. This restriction may be relaxed in future versions.
   */
  private ReadPreferenceSerializer readPreference;

  /**
   * the max commit time, which must be either null or greater than zero, in the given time unit
   */
  private Long maxCommitTime;

  public TransactionOptions() {
  }

  public TransactionOptions(JsonObject json) {
    TransactionOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    TransactionOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.TransactionOptions toDriverClass() {
    com.mongodb.TransactionOptions.Builder builder = com.mongodb.TransactionOptions.builder();
    initializeDriverBuilderClass(builder);
    return builder.build();
  }

  /**
   *  Sets the read concern.
   *
   *  @param readConcern the read concern
   *  @return this
   */
  @GenIgnore
  public TransactionOptions setReadConcern(ReadConcern readConcern) {
    if (readConcern == null) {
      this.readConcern = null;
    } else if (this.readConcern == null) {
      this.readConcern = new ReadConcernSerializer(readConcern);
    } else {
      this.readConcern.setValue(readConcern);
    }
    return this;
  }

  /**
   *  Sets the read concern.
   *
   *  @param readConcern the read concern
   *  @return this
   *
   * @hidden
   */
  public TransactionOptions setReadConcern(ReadConcernSerializer readConcern) {
    this.readConcern = readConcern;
    return this;
  }

  /**
   *  Gets the read concern.
   *
   *  @return the read concern
   */
  @GenIgnore
  public ReadConcern getMongoReadConcern() {
    if (this.readConcern == null) {
      return null;
    } else {
      return readConcern.getValue();
    }
  }

  /**
   *  Gets the read concern.
   *
   *  @return the read concern
   *
   * @hidden
   */
  public ReadConcernSerializer getReadConcern() {
    return readConcern;
  }

  /**
   *  Sets the write concern.
   *
   *  @param writeConcern the write concern, which must be acknowledged
   *  @return this
   */
  @GenIgnore
  public TransactionOptions setWriteConcern(WriteConcern writeConcern) {
    if (writeConcern == null) {
      this.writeConcern = null;
    } else if (this.writeConcern == null) {
      this.writeConcern = new WriteConcernSerializer(writeConcern);
    } else {
      this.writeConcern.setValue(writeConcern);
    }
    return this;
  }

  /**
   *  Sets the write concern.
   *
   *  @param writeConcern the write concern, which must be acknowledged
   *  @return this
   *
   * @hidden
   */
  public TransactionOptions setWriteConcern(WriteConcernSerializer writeConcern) {
    this.writeConcern = writeConcern;
    return this;
  }

  /**
   *  Gets the write concern.
   *
   *  @return the write concern
   */
  @GenIgnore
  public WriteConcern getMongoWriteConcern() {
    if (this.writeConcern == null) {
      return null;
    } else {
      return writeConcern.getValue();
    }
  }

  /**
   *  Gets the write concern.
   *
   *  @return the write concern
   *
   * @hidden
   */
  public WriteConcernSerializer getWriteConcern() {
    return writeConcern;
  }

  /**
   *  Sets the read preference.
   *
   *  @param readPreference the read preference, which currently must be primary. This restriction may be relaxed in future versions.
   *  @return this
   */
  @GenIgnore
  public TransactionOptions setReadPreference(ReadPreference readPreference) {
    if (readPreference == null) {
      this.readPreference = null;
    } else if (this.readPreference == null) {
      this.readPreference = new ReadPreferenceSerializer(readPreference);
    } else {
      this.readPreference.setValue(readPreference);
    }
    return this;
  }

  /**
   *  Sets the read preference.
   *
   *  @param readPreference the read preference, which currently must be primary. This restriction may be relaxed in future versions.
   *  @return this
   *
   * @hidden
   */
  public TransactionOptions setReadPreference(ReadPreferenceSerializer readPreference) {
    this.readPreference = readPreference;
    return this;
  }

  /**
   *  Gets the read preference.
   *
   *  @return the write concern
   */
  @GenIgnore
  public ReadPreference getMongoReadPreference() {
    if (this.readPreference == null) {
      return null;
    } else {
      return readPreference.getValue();
    }
  }

  /**
   *  Gets the read preference.
   *
   *  @return the write concern
   *
   * @hidden
   */
  public ReadPreferenceSerializer getReadPreference() {
    return readPreference;
  }

  /**
   *  Sets the maximum execution time on the server for the commitTransaction operation.
   *
   *  @param maxCommitTime the max commit time, which must be either null or greater than zero, in the given time unit (in milliseconds)
   *  @return this
   *  @mongodb.server.release 4.2
   *  @since 3.11
   */
  public TransactionOptions setMaxCommitTime(Long maxCommitTime) {
    this.maxCommitTime = maxCommitTime;
    return this;
  }

  /**
   *  Gets the maximum amount of time to allow a single commitTransaction command to execute.  The default is null, which places no
   *  limit on the execution time.
   *
   *  @return the maximum execution time (in milliseconds)
   *  @mongodb.server.release 4.2
   *  @since 3.11
   */
  public Long getMaxCommitTime() {
    return maxCommitTime;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(com.mongodb.TransactionOptions.Builder builder) {
    if (this.readConcern != null && this.readConcern.getValue() != null) {
      builder.readConcern(this.readConcern.getValue());
    }
    if (this.writeConcern != null && this.writeConcern.getValue() != null) {
      builder.writeConcern(this.writeConcern.getValue());
    }
    if (this.readPreference != null && this.readPreference.getValue() != null) {
      builder.readPreference(this.readPreference.getValue());
    }
    if (this.maxCommitTime != null) {
      builder.maxCommitTime(this.maxCommitTime, TimeUnit.MILLISECONDS);
    }
  }
}
