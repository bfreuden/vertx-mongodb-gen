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
import java.lang.Exception;
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

  private Exception acknowledgedException;

  private Exception insertedCountException;

  private Exception matchedCountException;

  private Exception deletedCountException;

  private Exception modifiedCountException;

  private Exception insertsException;

  private Exception upsertsException;

  private BulkWriteResult() {
  }

  /**
   *  Returns true if the write was acknowledged.
   *
   *  @return true if the write was acknowledged
   *  @see com.mongodb.WriteConcern#UNACKNOWLEDGED
   */
  public boolean isAcknowledged() {
    if (acknowledgedException != null)  {
      throw new RuntimeException(acknowledgedException);
    }
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
    if (insertedCountException != null)  {
      throw new RuntimeException(insertedCountException);
    }
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
    if (matchedCountException != null)  {
      throw new RuntimeException(matchedCountException);
    }
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
    if (deletedCountException != null)  {
      throw new RuntimeException(deletedCountException);
    }
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
    if (modifiedCountException != null)  {
      throw new RuntimeException(modifiedCountException);
    }
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
    if (insertsException != null)  {
      throw new RuntimeException(insertsException);
    }
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
    if (upsertsException != null)  {
      throw new RuntimeException(upsertsException);
    }
    return upserts;
  }

  /**
   * @return mongo object
   * @hidden
   */
  public static BulkWriteResult fromDriverClass(com.mongodb.bulk.BulkWriteResult from) {
    requireNonNull(from, "from is null");
    BulkWriteResult result = new BulkWriteResult();
    try {
      result.acknowledged = from.wasAcknowledged();
    } catch (Exception ex) {
      result.acknowledgedException = ex;
    }
    try {
      result.insertedCount = from.getInsertedCount();
    } catch (Exception ex) {
      result.insertedCountException = ex;
    }
    try {
      result.matchedCount = from.getMatchedCount();
    } catch (Exception ex) {
      result.matchedCountException = ex;
    }
    try {
      result.deletedCount = from.getDeletedCount();
    } catch (Exception ex) {
      result.deletedCountException = ex;
    }
    try {
      result.modifiedCount = from.getModifiedCount();
    } catch (Exception ex) {
      result.modifiedCountException = ex;
    }
    try {
      result.inserts = CollectionsConversionUtils.mapItems(from.getInserts(), BulkWriteInsert::fromDriverClass);
    } catch (Exception ex) {
      result.insertsException = ex;
    }
    try {
      result.upserts = CollectionsConversionUtils.mapItems(from.getUpserts(), BulkWriteUpsert::fromDriverClass);
    } catch (Exception ex) {
      result.upsertsException = ex;
    }
    return result;
  }
}
