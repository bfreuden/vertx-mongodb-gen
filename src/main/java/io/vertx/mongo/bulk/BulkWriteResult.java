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
package io.vertx.mongo.bulk;

import static java.util.Objects.requireNonNull;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.mongo.impl.CollectionsConversionUtils;
import java.util.List;

@DataObject(
    generateConverter = true
)
public class BulkWriteResult {
  private boolean acknowledged;

  private int insertedCount;

  private int matchedCount;

  private int deletedCount;

  private int modifiedCount;

  private List<BulkWriteInsert> inserts;

  private List<BulkWriteUpsert> upserts;

  private BulkWriteResult() {
  }

  /**
   *  Returns true if the write was acknowledged.
   *
   *  @return true if the write was acknowledged
   *  @see com.mongodb.WriteConcern#UNACKNOWLEDGED
   */
  public boolean isAcknowledged() {
    return acknowledged;
  }

  /**
   *  Returns the number of documents inserted by the write operation.
   *
   *  @return the number of documents inserted by the write operation
   *  @throws java.lang.UnsupportedOperationException if the write was unacknowledged.
   *  @see com.mongodb.WriteConcern#UNACKNOWLEDGED
   */
  public int getInsertedCount() {
    return insertedCount;
  }

  /**
   *  Returns the number of documents matched by updates or replacements in the write operation.  This will include documents that matched
   *  the query but where the modification didn't result in any actual change to the document; for example, if you set the value of some
   *  field, and the field already has that value, that will still count as an update.
   *
   *  @return the number of documents matched by updates in the write operation
   *  @throws java.lang.UnsupportedOperationException if the write was unacknowledged.
   *  @see com.mongodb.WriteConcern#UNACKNOWLEDGED
   */
  public int getMatchedCount() {
    return matchedCount;
  }

  /**
   *  Returns the number of documents deleted by the write operation.
   *
   *  @return the number of documents deleted by the write operation
   *  @throws java.lang.UnsupportedOperationException if the write was unacknowledged.
   *  @see com.mongodb.WriteConcern#UNACKNOWLEDGED
   */
  public int getDeletedCount() {
    return deletedCount;
  }

  /**
   *  Returns the number of documents modified by the write operation.  This only applies to updates or replacements, and will only count
   *  documents that were actually changed; for example, if you set the value of some field , and the field already has that value, that
   *  will not count as a modification.
   *
   *  @return the number of documents modified by the write operation
   *  @see com.mongodb.WriteConcern#UNACKNOWLEDGED
   */
  public int getModifiedCount() {
    return modifiedCount;
  }

  /**
   *  Gets an unmodifiable list of inserted items, or the empty list if there were none.
   *
   *  @return a list of inserted items, or the empty list if there were none.
   *  @throws java.lang.UnsupportedOperationException if the write was unacknowledged.
   *  @see com.mongodb.WriteConcern#UNACKNOWLEDGED
   *  @since 4.0
   */
  public List<BulkWriteInsert> getInserts() {
    return inserts;
  }

  /**
   *  Gets an unmodifiable list of upserted items, or the empty list if there were none.
   *
   *  @return a list of upserted items, or the empty list if there were none.
   *  @throws java.lang.UnsupportedOperationException if the write was unacknowledged.
   *  @see com.mongodb.WriteConcern#UNACKNOWLEDGED
   */
  public List<BulkWriteUpsert> getUpserts() {
    return upserts;
  }

  /**
   * @return mongo object
   * @hidden
   */
  public static BulkWriteResult fromDriverClass(com.mongodb.bulk.BulkWriteResult from) {
    requireNonNull(from, "from is null");
    BulkWriteResult result = new BulkWriteResult();
    result.acknowledged = from.wasAcknowledged();
    result.insertedCount = from.getInsertedCount();
    result.matchedCount = from.getMatchedCount();
    result.deletedCount = from.getDeletedCount();
    result.modifiedCount = from.getModifiedCount();
    result.inserts = CollectionsConversionUtils.mapItems(from.getInserts(), _bean -> BulkWriteInsert.fromDriverClass(_bean));
    result.upserts = CollectionsConversionUtils.mapItems(from.getUpserts(), _bean -> BulkWriteUpsert.fromDriverClass(_bean));
    return result;
  }
}
