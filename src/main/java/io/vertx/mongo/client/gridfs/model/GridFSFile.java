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

import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import org.bson.BsonValue;
import org.bson.types.ObjectId;

import java.lang.Exception;
import java.lang.Object;
import java.lang.String;
import java.util.Date;

public class GridFSFile {
  private String objectId;

  private Object id;

  private String filename;

  private long length;

  private int chunkSize;

  private Date uploadDate;

  private JsonObject metadata;

  private Exception objectIdException;

  private Exception idException;

  private Exception filenameException;

  private Exception lengthException;

  private Exception chunkSizeException;

  private Exception uploadDateException;

  private Exception metadataException;

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
    if (objectIdException != null)  {
      throw new RuntimeException(objectIdException);
    }
    return objectId;
  }

  /**
   *  The {@link BsonValue} id for this file.
   *
   *  @return the id for this file
   */
  public Object getId() {
    if (idException != null)  {
      throw new RuntimeException(idException);
    }
    return id;
  }

  /**
   *  The filename
   *
   *  @return the filename
   */
  public String getFilename() {
    if (filenameException != null)  {
      throw new RuntimeException(filenameException);
    }
    return filename;
  }

  /**
   *  The length, in bytes of this file
   *
   *  @return the length, in bytes of this file
   */
  public long getLength() {
    if (lengthException != null)  {
      throw new RuntimeException(lengthException);
    }
    return length;
  }

  /**
   *  The size, in bytes, of each data chunk of this file
   *
   *  @return the size, in bytes, of each data chunk of this file
   */
  public int getChunkSize() {
    if (chunkSizeException != null)  {
      throw new RuntimeException(chunkSizeException);
    }
    return chunkSize;
  }

  /**
   *  The date and time this file was added to GridFS
   *
   *  @return the date and time this file was added to GridFS
   */
  public Date getUploadDate() {
    if (uploadDateException != null)  {
      throw new RuntimeException(uploadDateException);
    }
    return uploadDate;
  }

  /**
   *  Any additional metadata stored along with the file
   *
   *  @return the metadata document or null
   */
  public JsonObject getMetadata() {
    if (metadataException != null)  {
      throw new RuntimeException(metadataException);
    }
    return metadata;
  }

  /**
   * @return mongo object
   * @hidden
   */
  public static GridFSFile fromDriverClass(com.mongodb.client.gridfs.model.GridFSFile from) {
    requireNonNull(from, "from is null");
    GridFSFile result = new GridFSFile();
    try {
      result.objectId = ConversionUtilsImpl.INSTANCE.toString(from.getObjectId());
    } catch (Exception ex) {
      result.objectIdException = ex;
    }
    try {
      result.id = ConversionUtilsImpl.INSTANCE.toObject(from.getId());
    } catch (Exception ex) {
      result.idException = ex;
    }
    try {
      result.filename = from.getFilename();
    } catch (Exception ex) {
      result.filenameException = ex;
    }
    try {
      result.length = from.getLength();
    } catch (Exception ex) {
      result.lengthException = ex;
    }
    try {
      result.chunkSize = from.getChunkSize();
    } catch (Exception ex) {
      result.chunkSizeException = ex;
    }
    try {
      result.uploadDate = from.getUploadDate();
    } catch (Exception ex) {
      result.uploadDateException = ex;
    }
    try {
      result.metadata = ConversionUtilsImpl.INSTANCE.toJsonObject(from.getMetadata());
    } catch (Exception ex) {
      result.metadataException = ex;
    }
    return result;
  }
}
