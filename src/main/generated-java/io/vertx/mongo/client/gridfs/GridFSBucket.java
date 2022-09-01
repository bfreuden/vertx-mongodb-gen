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
package io.vertx.mongo.client.gridfs;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.gridfs.GridFSBuckets;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.ObjectId;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.client.gridfs.impl.GridFSBucketImpl;
import io.vertx.mongo.client.gridfs.model.GridFSDownloadOptions;
import io.vertx.mongo.client.gridfs.model.GridFSFile;
import io.vertx.mongo.client.gridfs.model.GridFSUploadOptions;
import io.vertx.mongo.client.impl.MongoDatabaseImpl;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;

/**
 *  Represents a GridFS Bucket
 *  @since 1.3
 */
public interface GridFSBucket {
  static GridFSBucket create(MongoDatabase database) {
    return new GridFSBucketImpl(((MongoDatabaseImpl)database).getClientContext(), GridFSBuckets.create(database.toDriverClass(((MongoDatabaseImpl)database).getClientContext())));
  }

  static GridFSBucket create(MongoDatabase database, String bucketName) {
    return new GridFSBucketImpl(((MongoDatabaseImpl)database).getClientContext(), GridFSBuckets.create(database.toDriverClass(((MongoDatabaseImpl)database).getClientContext()), bucketName));
  }

  /**
   *  The bucket name.
   *
   *  @return the bucket name
   */
  String getBucketName();

  /**
   *  Sets the chunk size in bytes. Defaults to 255.
   *
   *  @return the chunk size in bytes.
   */
  int getChunkSizeBytes();

  /**
   *  Get the write concern for the GridFSBucket.
   *
   *  @return the {@link com.mongodb.WriteConcern}
   */
  WriteConcern getWriteConcern();

  /**
   *  Get the read preference for the GridFSBucket.
   *
   *  @return the {@link com.mongodb.ReadPreference}
   */
  ReadPreference getReadPreference();

  /**
   *  Get the read concern for the GridFSBucket.
   *
   *  @return the {@link com.mongodb.ReadConcern}
   *  @mongodb.server.release 3.2
   *  @mongodb.driver.manual reference/readConcern/ Read Concern
   */
  ReadConcern getReadConcern();

  /**
   *  Create a new GridFSBucket instance with a new chunk size in bytes.
   *
   *  @param chunkSizeBytes the new chunk size in bytes.
   *  @return a new GridFSBucket instance with the different chunk size in bytes
   */
  GridFSBucket withChunkSizeBytes(int chunkSizeBytes);

  /**
   *  Create a new GridFSBucket instance with a different read preference.
   *
   *  @param readPreference the new {@link ReadPreference} for the database
   *  @return a new GridFSBucket instance with the different readPreference
   */
  GridFSBucket withReadPreference(ReadPreference readPreference);

  /**
   *  Create a new GridFSBucket instance with a different write concern.
   *
   *  @param writeConcern the new {@link WriteConcern} for the database
   *  @return a new GridFSBucket instance with the different writeConcern
   */
  GridFSBucket withWriteConcern(WriteConcern writeConcern);

  /**
   *  Create a new MongoDatabase instance with a different read concern.
   *
   *  @param readConcern the new {@link ReadConcern} for the database
   *  @return a new GridFSBucket instance with the different ReadConcern
   *  @mongodb.server.release 3.2
   *  @mongodb.driver.manual reference/readConcern/ Read Concern
   */
  GridFSBucket withReadConcern(ReadConcern readConcern);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param filename the filename
   *  @param source   the stream providing the file data
   *  @return a future with a single element, the ObjectId of the uploaded file.
   *  @since 1.13
   */
  Future<ObjectId> uploadStream(String filename, ReadStream<Buffer> source);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param filename the filename
   *  @param source   the stream providing the file data
   *  @param resultHandler an async result with a single element, the ObjectId of the uploaded file.
   *  @since 1.13
   */
  void uploadStream(String filename, ReadStream<Buffer> source,
      Handler<AsyncResult<ObjectId>> resultHandler);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param filename the filename providing the file data
   *  
   *  @return a future with a single element, the ObjectId of the uploaded file.
   *  @since 1.13
   */
  Future<ObjectId> uploadFile(String filename);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param filename the filename providing the file data
   *  
   *  @param resultHandler an async result with a single element, the ObjectId of the uploaded file.
   *  @since 1.13
   */
  void uploadFile(String filename, Handler<AsyncResult<ObjectId>> resultHandler);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param filename the filename
   *  @param source   the stream providing the file data
   *  @param options  the GridFSUploadOptions
   *  @return a future with a single element, the ObjectId of the uploaded file.
   *  @since 1.13
   */
  Future<ObjectId> uploadStream(String filename, ReadStream<Buffer> source,
      GridFSUploadOptions options);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param filename the filename
   *  @param source   the stream providing the file data
   *  @param options  the GridFSUploadOptions
   *  @param resultHandler an async result with a single element, the ObjectId of the uploaded file.
   *  @since 1.13
   */
  void uploadStream(String filename, ReadStream<Buffer> source, GridFSUploadOptions options,
      Handler<AsyncResult<ObjectId>> resultHandler);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param filename the filename providing the file data
   *  
   *  @param options  the GridFSUploadOptions
   *  @return a future with a single element, the ObjectId of the uploaded file.
   *  @since 1.13
   */
  Future<ObjectId> uploadFile(String filename, GridFSUploadOptions options);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param filename the filename providing the file data
   *  
   *  @param options  the GridFSUploadOptions
   *  @param resultHandler an async result with a single element, the ObjectId of the uploaded file.
   *  @since 1.13
   */
  void uploadFile(String filename, GridFSUploadOptions options,
      Handler<AsyncResult<ObjectId>> resultHandler);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filename the filename
   *  @param source   the stream providing the file data
   *  @return a future with a single element, the ObjectId of the uploaded file.
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  Future<ObjectId> uploadStream(ClientSession clientSession, String filename,
      ReadStream<Buffer> source);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filename the filename
   *  @param source   the stream providing the file data
   *  @param resultHandler an async result with a single element, the ObjectId of the uploaded file.
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  void uploadStream(ClientSession clientSession, String filename, ReadStream<Buffer> source,
      Handler<AsyncResult<ObjectId>> resultHandler);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filename the filename providing the file data
   *  
   *  @return a future with a single element, the ObjectId of the uploaded file.
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  Future<ObjectId> uploadFile(ClientSession clientSession, String filename);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filename the filename providing the file data
   *  
   *  @param resultHandler an async result with a single element, the ObjectId of the uploaded file.
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  void uploadFile(ClientSession clientSession, String filename,
      Handler<AsyncResult<ObjectId>> resultHandler);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filename the filename
   *  @param source   the stream providing the file data
   *  @param options  the GridFSUploadOptions
   *  @return a future with a single element, the ObjectId of the uploaded file.
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  Future<ObjectId> uploadStream(ClientSession clientSession, String filename,
      ReadStream<Buffer> source, GridFSUploadOptions options);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filename the filename
   *  @param source   the stream providing the file data
   *  @param options  the GridFSUploadOptions
   *  @param resultHandler an async result with a single element, the ObjectId of the uploaded file.
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  void uploadStream(ClientSession clientSession, String filename, ReadStream<Buffer> source,
      GridFSUploadOptions options, Handler<AsyncResult<ObjectId>> resultHandler);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filename the filename providing the file data
   *  
   *  @param options  the GridFSUploadOptions
   *  @return a future with a single element, the ObjectId of the uploaded file.
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  Future<ObjectId> uploadFile(ClientSession clientSession, String filename,
      GridFSUploadOptions options);

  /**
   *  Uploads the contents of the given {@code Publisher} to a GridFS bucket.
   *  <p>
   *  Reads the contents of the user file from the {@code source} and uploads it as chunks in the chunks collection. After all the
   *  chunks have been uploaded, it creates a files collection document for {@code filename} in the files collection.
   *  </p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filename the filename providing the file data
   *  
   *  @param options  the GridFSUploadOptions
   *  @param resultHandler an async result with a single element, the ObjectId of the uploaded file.
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  void uploadFile(ClientSession clientSession, String filename, GridFSUploadOptions options,
      Handler<AsyncResult<ObjectId>> resultHandler);

  /**
   *  Downloads the contents of the stored file specified by {@code id} into the {@code Publisher}.
   *  @param id          the ObjectId of the file to be written to the destination Publisher
   *  @return a result with a single element, representing the amount of data written
   *  @since 1.13
   */
  GridFSDownloadResult downloadByObjectId(ObjectId id);

  /**
   *  Downloads the contents of the stored file specified by {@code id} into the {@code Publisher}.
   *  @param id          the ObjectId of the file to be written to the destination Publisher
   *  @param controlOptions options
   *  @return a result with a single element, representing the amount of data written
   *  @since 1.13
   */
  GridFSDownloadResult downloadByObjectId(ObjectId id, GridFSDownloadControlOptions controlOptions);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} into the {@code Publisher}.
   *  @param filename    the name of the file to be downloaded
   *  @return a result with a single element, representing the amount of data written
   *  @since 1.13
   */
  GridFSDownloadResult downloadByFilename(String filename);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} into the {@code Publisher}.
   *  @param filename    the name of the file to be downloaded
   *  @param controlOptions options
   *  @return a result with a single element, representing the amount of data written
   *  @since 1.13
   */
  GridFSDownloadResult downloadByFilename(String filename,
      GridFSDownloadControlOptions controlOptions);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} and by the revision in {@code options} into the
   *  {@code Publisher}.
   *  @param filename    the name of the file to be downloaded
   *  @param options     the download options
   *  @return a result with a single element, representing the amount of data written
   *  @since 1.13
   */
  GridFSDownloadResult downloadByFilename(String filename, GridFSDownloadOptions options);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} and by the revision in {@code options} into the
   *  {@code Publisher}.
   *  @param filename    the name of the file to be downloaded
   *  @param options     the download options
   *  @param controlOptions options
   *  @return a result with a single element, representing the amount of data written
   *  @since 1.13
   */
  GridFSDownloadResult downloadByFilename(String filename, GridFSDownloadOptions options,
      GridFSDownloadControlOptions controlOptions);

  /**
   *  Downloads the contents of the stored file specified by {@code id} into the {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param id          the ObjectId of the file to be written to the destination Publisher
   *  @return a result with a single element, representing the amount of data written
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  GridFSDownloadResult downloadByObjectId(ClientSession clientSession, ObjectId id);

  /**
   *  Downloads the contents of the stored file specified by {@code id} into the {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param id          the ObjectId of the file to be written to the destination Publisher
   *  @param controlOptions options
   *  @return a result with a single element, representing the amount of data written
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  GridFSDownloadResult downloadByObjectId(ClientSession clientSession, ObjectId id,
      GridFSDownloadControlOptions controlOptions);

  /**
   *  Downloads the contents of the latest version of the stored file specified by {@code filename} into the {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param filename    the name of the file to be downloaded
   *  @return a result with a single element, representing the amount of data written
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  GridFSDownloadResult downloadByFilename(ClientSession clientSession, String filename);

  /**
   *  Downloads the contents of the latest version of the stored file specified by {@code filename} into the {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param filename    the name of the file to be downloaded
   *  @param controlOptions options
   *  @return a result with a single element, representing the amount of data written
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  GridFSDownloadResult downloadByFilename(ClientSession clientSession, String filename,
      GridFSDownloadControlOptions controlOptions);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} and by the revision in {@code options} into the
   *  {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param filename    the name of the file to be downloaded
   *  @param options     the download options
   *  @return a result with a single element, representing the amount of data written
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  GridFSDownloadResult downloadByFilename(ClientSession clientSession, String filename,
      GridFSDownloadOptions options);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} and by the revision in {@code options} into the
   *  {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param filename    the name of the file to be downloaded
   *  @param options     the download options
   *  @param controlOptions options
   *  @return a result with a single element, representing the amount of data written
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  GridFSDownloadResult downloadByFilename(ClientSession clientSession, String filename,
      GridFSDownloadOptions options, GridFSDownloadControlOptions controlOptions);

  /**
   *  Finds all documents in the files collection.
   *  @return the GridFS find result interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   */
  MongoResult<GridFSFile> find();

  /**
   *  Finds all documents in the files collection.
   *  @param options options
   *  @return the GridFS find result interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   */
  MongoResult<GridFSFile> find(GridFSFindOptions options);

  /**
   *  Finds all documents in the collection that match the filter.
   *  <p>
   *  Below is an example of filtering against the filename and some nested metadata that can also be stored along with the file data:
   *  <pre>
   *   {@code
   *       Filters.and(Filters.eq("filename", "mongodb.png"), Filters.eq("metadata.contentType", "image/png"));
   *   }
   *   </pre>
   *  @param filter the query filter
   *  @return the GridFS find result interface
   *  @see com.mongodb.client.model.Filters
   */
  MongoResult<GridFSFile> find(JsonObject filter);

  /**
   *  Finds all documents in the collection that match the filter.
   *  <p>
   *  Below is an example of filtering against the filename and some nested metadata that can also be stored along with the file data:
   *  <pre>
   *   {@code
   *       Filters.and(Filters.eq("filename", "mongodb.png"), Filters.eq("metadata.contentType", "image/png"));
   *   }
   *   </pre>
   *  @param filter the query filter
   *  @param options options
   *  @return the GridFS find result interface
   *  @see com.mongodb.client.model.Filters
   */
  MongoResult<GridFSFile> find(JsonObject filter, GridFSFindOptions options);

  /**
   *  Finds all documents in the files collection.
   *  @param clientSession the client session with which to associate this operation
   *  @return the GridFS find result interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<GridFSFile> find(ClientSession clientSession);

  /**
   *  Finds all documents in the files collection.
   *  @param clientSession the client session with which to associate this operation
   *  @param options options
   *  @return the GridFS find result interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<GridFSFile> find(ClientSession clientSession, GridFSFindOptions options);

  /**
   *  Finds all documents in the collection that match the filter.
   *  <p>
   *  Below is an example of filtering against the filename and some nested metadata that can also be stored along with the file data:
   *  <pre>
   *   {@code
   *       Filters.and(Filters.eq("filename", "mongodb.png"), Filters.eq("metadata.contentType", "image/png"));
   *   }
   *   </pre>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter
   *  @return the GridFS find result interface
   *  @see com.mongodb.client.model.Filters
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<GridFSFile> find(ClientSession clientSession, JsonObject filter);

  /**
   *  Finds all documents in the collection that match the filter.
   *  <p>
   *  Below is an example of filtering against the filename and some nested metadata that can also be stored along with the file data:
   *  <pre>
   *   {@code
   *       Filters.and(Filters.eq("filename", "mongodb.png"), Filters.eq("metadata.contentType", "image/png"));
   *   }
   *   </pre>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter
   *  @param options options
   *  @return the GridFS find result interface
   *  @see com.mongodb.client.model.Filters
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<GridFSFile> find(ClientSession clientSession, JsonObject filter,
      GridFSFindOptions options);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param id       the ObjectId of the file to be deleted
   *  @return a future with a single element, representing that the file has been deleted
   */
  Future<Void> delete(ObjectId id);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param id       the ObjectId of the file to be deleted
   *  @param resultHandler an async result with a single element, representing that the file has been deleted
   */
  void delete(ObjectId id, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param id       the ObjectId of the file to be deleted
   *  @return a future with a single element, representing that the file has been deleted
   */
  Future<Void> delete(Object id);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param id       the ObjectId of the file to be deleted
   *  @param resultHandler an async result with a single element, representing that the file has been deleted
   */
  void delete(Object id, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param clientSession the client session with which to associate this operation
   *  @param id       the ObjectId of the file to be deleted
   *  @return a future with a single element, representing that the file has been deleted
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> delete(ClientSession clientSession, ObjectId id);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param clientSession the client session with which to associate this operation
   *  @param id       the ObjectId of the file to be deleted
   *  @param resultHandler an async result with a single element, representing that the file has been deleted
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void delete(ClientSession clientSession, ObjectId id, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param clientSession the client session with which to associate this operation
   *  @param id       the ObjectId of the file to be deleted
   *  @return a future with a single element, representing that the file has been deleted
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> delete(ClientSession clientSession, Object id);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param clientSession the client session with which to associate this operation
   *  @param id       the ObjectId of the file to be deleted
   *  @param resultHandler an async result with a single element, representing that the file has been deleted
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void delete(ClientSession clientSession, Object id, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @return a future with a single element, representing that the file has been renamed
   */
  Future<Void> rename(ObjectId id, String newFilename);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @param resultHandler an async result with a single element, representing that the file has been renamed
   */
  void rename(ObjectId id, String newFilename, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @return a future with a single element, representing that the file has been renamed
   */
  Future<Void> rename(Object id, String newFilename);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @param resultHandler an async result with a single element, representing that the file has been renamed
   */
  void rename(Object id, String newFilename, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param clientSession the client session with which to associate this operation
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @return a future with a single element, representing that the file has been renamed
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> rename(ClientSession clientSession, ObjectId id, String newFilename);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param clientSession the client session with which to associate this operation
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @param resultHandler an async result with a single element, representing that the file has been renamed
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void rename(ClientSession clientSession, ObjectId id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param clientSession the client session with which to associate this operation
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @return a future with a single element, representing that the file has been renamed
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> rename(ClientSession clientSession, Object id, String newFilename);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param clientSession the client session with which to associate this operation
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @param resultHandler an async result with a single element, representing that the file has been renamed
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void rename(ClientSession clientSession, Object id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drops the data associated with this bucket from the database.
   *  @return a future with a single element, representing that the collections have been dropped
   */
  Future<Void> drop();

  /**
   *  Drops the data associated with this bucket from the database.
   *  @param resultHandler an async result with a single element, representing that the collections have been dropped
   */
  void drop(Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drops the data associated with this bucket from the database.
   *  @param clientSession the client session with which to associate this operation
   *  @return a future with a single element, representing that the collections have been dropped
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> drop(ClientSession clientSession);

  /**
   *  Drops the data associated with this bucket from the database.
   *  @param clientSession the client session with which to associate this operation
   *  @param resultHandler an async result with a single element, representing that the collections have been dropped
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void drop(ClientSession clientSession, Handler<AsyncResult<Void>> resultHandler);

  /**
   * @return mongo object
   * @hidden
   */
  com.mongodb.reactivestreams.client.gridfs.GridFSBucket toDriverClass(
      MongoClientContext clientContext);
}
