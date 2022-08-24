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
package io.vertx.mongo.client.gridfs.model;

import static java.util.Objects.requireNonNull;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import org.bson.BsonValue;
import org.bson.types.ObjectId;

import java.lang.Object;
import java.lang.String;
import java.util.Date;

@DataObject(
    generateConverter = true
)
public class GridFSFile {
  private String objectId;

  private Object id;

  private String filename;

  private long length;

  private int chunkSize;

  private Date uploadDate;

  private JsonObject metadata;

  private GridFSFile() {
  }

  /**
   *  The {@link ObjectId} for this file.
   *
   *  Throws a MongoGridFSException if the file id is not an ObjectId.
   *
   *  @return the id for this file.
   */
  public String getObjectId() {
    return objectId;
  }

  /**
   *  The {@link BsonValue} id for this file.
   *
   *  @return the id for this file
   */
  public Object getId() {
    return id;
  }

  /**
   *  The filename
   *
   *  @return the filename
   */
  public String getFilename() {
    return filename;
  }

  /**
   *  The length, in bytes of this file
   *
   *  @return the length, in bytes of this file
   */
  public long getLength() {
    return length;
  }

  /**
   *  The size, in bytes, of each data chunk of this file
   *
   *  @return the size, in bytes, of each data chunk of this file
   */
  public int getChunkSize() {
    return chunkSize;
  }

  /**
   *  The date and time this file was added to GridFS
   *
   *  @return the date and time this file was added to GridFS
   */
  public Date getUploadDate() {
    return uploadDate;
  }

  /**
   *  Any additional metadata stored along with the file
   *
   *  @return the metadata document or null
   */
  public JsonObject getMetadata() {
    return metadata;
  }

  /**
   * @return mongo object
   * @hidden
   */
  public static GridFSFile fromDriverClass(com.mongodb.client.gridfs.model.GridFSFile from) {
    requireNonNull(from, "from is null");
    GridFSFile result = new GridFSFile();
    result.objectId = ConversionUtilsImpl.INSTANCE.toString(from.getObjectId());
    result.id = ConversionUtilsImpl.INSTANCE.toObject(from.getId());
    result.filename = from.getFilename();
    result.length = from.getLength();
    result.chunkSize = from.getChunkSize();
    result.uploadDate = from.getUploadDate();
    result.metadata = ConversionUtilsImpl.INSTANCE.toJsonObject(from.getMetadata());
    return result;
  }
}
