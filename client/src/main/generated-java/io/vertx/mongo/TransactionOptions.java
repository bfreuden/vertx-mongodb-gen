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
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.serializers.TransactionOptionsSerializer;
import java.lang.Long;

@DataObject
public class TransactionOptions {
  private TransactionOptionsSerializer serializer = new TransactionOptionsSerializer();

  public TransactionOptions() {
  }

  public TransactionOptions(JsonObject json) {
    serializer = new TransactionOptionsSerializer(json);
  }

  public JsonObject toJson() {
    return serializer.toJson();
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.TransactionOptions toDriverClass(MongoClientContext clientContext) {
    return this.serializer.toDriverClass(clientContext);
  }

  /**
   *  Sets the read concern.
   *
   *  @param readConcern the read concern
   *  @return this
   */
  public TransactionOptions setReadConcern(ReadConcern readConcern) {
    this.serializer.__setReadConcern(readConcern);
    return this;
  }

  /**
   *  Gets the read concern.
   *
   *  @return the read concern
   */
  public ReadConcern getReadConcern() {
    return this.serializer.__getReadConcern();
  }

  /**
   *  Sets the write concern.
   *
   *  @param writeConcern the write concern, which must be acknowledged
   *  @return this
   */
  public TransactionOptions setWriteConcern(WriteConcern writeConcern) {
    this.serializer.__setWriteConcern(writeConcern);
    return this;
  }

  /**
   *  Gets the write concern.
   *
   *  @return the write concern
   */
  public WriteConcern getWriteConcern() {
    return this.serializer.__getWriteConcern();
  }

  /**
   *  Sets the read preference.
   *
   *  @param readPreference the read preference, which currently must be primary. This restriction may be relaxed in future versions.
   *  @return this
   */
  public TransactionOptions setReadPreference(ReadPreference readPreference) {
    this.serializer.__setReadPreference(readPreference);
    return this;
  }

  /**
   *  Gets the read preference.
   *
   *  @return the write concern
   */
  public ReadPreference getReadPreference() {
    return this.serializer.__getReadPreference();
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
    this.serializer.setMaxCommitTime(maxCommitTime);
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
    return this.serializer.getMaxCommitTime();
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(MongoClientContext clientContext,
      com.mongodb.TransactionOptions.Builder builder) {
    this.serializer.initializeDriverBuilderClass(clientContext, builder);
  }
}
