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
package io.vertx.mongo.serializers;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ReadConcernSerializer;
import io.vertx.mongo.impl.ReadPreferenceSerializer;
import io.vertx.mongo.impl.WriteConcernSerializer;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

@DataObject(
    generateConverter = true
)
public class TransactionOptionsSerializer {
  private ReadConcern readConcern;

  private WriteConcern writeConcern;

  private ReadPreference readPreference;

  private Long maxCommitTime;

  public TransactionOptionsSerializer() {
  }

  public TransactionOptionsSerializer(JsonObject json) {
    TransactionOptionsSerializerConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    TransactionOptionsSerializerConverter.toJson(this, result);
    return result;
  }

  public TransactionOptions toDriverClass() {
    TransactionOptions.Builder builder = TransactionOptions.builder();
    initializeDriverBuilderClass(builder);
    return builder.build();
  }

  public TransactionOptionsSerializer __setReadConcern(ReadConcern readConcern) {
    this.readConcern = readConcern;
    return this;
  }

  public TransactionOptionsSerializer setReadConcern(ReadConcernSerializer readConcern) {
    this.readConcern = readConcern == null ? null : readConcern.getValue();
    return this;
  }

  public ReadConcern __getReadConcern() {
    return readConcern;
  }

  public ReadConcernSerializer getReadConcern() {
    return this.readConcern == null ? null : new ReadConcernSerializer(this.readConcern);
  }

  public TransactionOptionsSerializer __setWriteConcern(WriteConcern writeConcern) {
    this.writeConcern = writeConcern;
    return this;
  }

  public TransactionOptionsSerializer setWriteConcern(WriteConcernSerializer writeConcern) {
    this.writeConcern = writeConcern == null ? null : writeConcern.getValue();
    return this;
  }

  public WriteConcern __getWriteConcern() {
    return writeConcern;
  }

  public WriteConcernSerializer getWriteConcern() {
    return this.writeConcern == null ? null : new WriteConcernSerializer(this.writeConcern);
  }

  public TransactionOptionsSerializer __setReadPreference(ReadPreference readPreference) {
    this.readPreference = readPreference;
    return this;
  }

  public TransactionOptionsSerializer setReadPreference(ReadPreferenceSerializer readPreference) {
    this.readPreference = readPreference == null ? null : readPreference.getValue();
    return this;
  }

  public ReadPreference __getReadPreference() {
    return readPreference;
  }

  public ReadPreferenceSerializer getReadPreference() {
    return this.readPreference == null ? null : new ReadPreferenceSerializer(this.readPreference);
  }

  public TransactionOptionsSerializer setMaxCommitTime(Long maxCommitTime) {
    this.maxCommitTime = maxCommitTime;
    return this;
  }

  public Long getMaxCommitTime() {
    return maxCommitTime;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(TransactionOptions.Builder builder) {
    if (this.readConcern != null) {
      builder.readConcern(this.readConcern);
    }
    if (this.writeConcern != null) {
      builder.writeConcern(this.writeConcern);
    }
    if (this.readPreference != null) {
      builder.readPreference(this.readPreference);
    }
    if (this.maxCommitTime != null) {
      builder.maxCommitTime(this.maxCommitTime, TimeUnit.MILLISECONDS);
    }
  }
}
