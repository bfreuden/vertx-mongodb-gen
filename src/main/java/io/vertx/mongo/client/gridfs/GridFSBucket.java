package io.vertx.mongo.client.gridfs;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.MongoResult;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;

/**
 *  Represents a GridFS Bucket
 *  @since 1.3
 */
public interface GridFSBucket {
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
   *  Downloads the contents of the stored file specified by {@code id} into the {@code Publisher}.
   *  @param id          the ObjectId of the file to be written to the destination Publisher
   *  @return a future with a single element, representing the amount of data written
   *  @since 1.13
   */
  Future<Void> downloadByObjectId(JsonObject id);

  /**
   *  Downloads the contents of the stored file specified by {@code id} into the {@code Publisher}.
   *  @param id          the ObjectId of the file to be written to the destination Publisher
   *  @param handler an async result with a single element, representing the amount of data written
   *  @return a reference to <code>this</code>
   *  @since 1.13
   */
  GridFSBucket downloadByObjectId(JsonObject id, Handler<AsyncResult<Void>> handler);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} into the {@code Publisher}.
   *  @param filename    the name of the file to be downloaded
   *  @return a future with a single element, representing the amount of data written
   *  @since 1.13
   */
  Future<Void> downloadByFilename(String filename);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} into the {@code Publisher}.
   *  @param filename    the name of the file to be downloaded
   *  @param handler an async result with a single element, representing the amount of data written
   *  @return a reference to <code>this</code>
   *  @since 1.13
   */
  GridFSBucket downloadByFilename(String filename, Handler<AsyncResult<Void>> handler);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} and by the revision in {@code options} into the
   *  {@code Publisher}.
   *  @param filename    the name of the file to be downloaded
   *  @param options     the download options
   *  @return a future with a single element, representing the amount of data written
   *  @since 1.13
   */
  Future<Void> downloadByFilename(String filename, GridFSDownloadOptions options);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} and by the revision in {@code options} into the
   *  {@code Publisher}.
   *  @param filename    the name of the file to be downloaded
   *  @param options     the download options
   *  @param handler an async result with a single element, representing the amount of data written
   *  @return a reference to <code>this</code>
   *  @since 1.13
   */
  GridFSBucket downloadByFilename(String filename, GridFSDownloadOptions options,
      Handler<AsyncResult<Void>> handler);

  /**
   *  Downloads the contents of the stored file specified by {@code id} into the {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param id          the ObjectId of the file to be written to the destination Publisher
   *  @return a future with a single element, representing the amount of data written
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  Future<Void> downloadByObjectId(ClientSession clientSession, JsonObject id);

  /**
   *  Downloads the contents of the stored file specified by {@code id} into the {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param id          the ObjectId of the file to be written to the destination Publisher
   *  @param handler an async result with a single element, representing the amount of data written
   *  @return a reference to <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  GridFSBucket downloadByObjectId(ClientSession clientSession, JsonObject id,
      Handler<AsyncResult<Void>> handler);

  /**
   *  Downloads the contents of the latest version of the stored file specified by {@code filename} into the {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param filename    the name of the file to be downloaded
   *  @return a future with a single element, representing the amount of data written
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  Future<Void> downloadByFilename(ClientSession clientSession, String filename);

  /**
   *  Downloads the contents of the latest version of the stored file specified by {@code filename} into the {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param filename    the name of the file to be downloaded
   *  @param handler an async result with a single element, representing the amount of data written
   *  @return a reference to <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  GridFSBucket downloadByFilename(ClientSession clientSession, String filename,
      Handler<AsyncResult<Void>> handler);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} and by the revision in {@code options} into the
   *  {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param filename    the name of the file to be downloaded
   *  @param options     the download options
   *  @return a future with a single element, representing the amount of data written
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  Future<Void> downloadByFilename(ClientSession clientSession, String filename,
      GridFSDownloadOptions options);

  /**
   *  Downloads the contents of the stored file specified by {@code filename} and by the revision in {@code options} into the
   *  {@code Publisher}.
   *  @param clientSession the client session with which to associate this operation
   *  @param filename    the name of the file to be downloaded
   *  @param options     the download options
   *  @param handler an async result with a single element, representing the amount of data written
   *  @return a reference to <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.13
   */
  GridFSBucket downloadByFilename(ClientSession clientSession, String filename,
      GridFSDownloadOptions options, Handler<AsyncResult<Void>> handler);

  /**
   *  Finds all documents in the files collection.
   *  @return the GridFS find iterable interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   */
  MongoResult<GridFSFile> find();

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
   *  @return the GridFS find iterable interface
   *  @see com.mongodb.client.model.Filters
   */
  MongoResult<GridFSFile> find(JsonObject filter);

  /**
   *  Finds all documents in the files collection.
   *  @param clientSession the client session with which to associate this operation
   *  @return the GridFS find iterable interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<GridFSFile> find(ClientSession clientSession);

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
   *  @return the GridFS find iterable interface
   *  @see com.mongodb.client.model.Filters
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<GridFSFile> find(ClientSession clientSession, JsonObject filter);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param id       the ObjectId of the file to be deleted
   *  @return a future with a single element, representing that the file has been deleted
   */
  Future<Void> delete(JsonObject id);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param id       the ObjectId of the file to be deleted
   *  @param handler an async result with a single element, representing that the file has been deleted
   *  @return a reference to <code>this</code>
   */
  GridFSBucket delete(JsonObject id, Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param id       the ObjectId of the file to be deleted
   *  @return a future with a single element, representing that the file has been deleted
   */
  Future<Void> delete(Object id);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param id       the ObjectId of the file to be deleted
   *  @param handler an async result with a single element, representing that the file has been deleted
   *  @return a reference to <code>this</code>
   */
  GridFSBucket delete(Object id, Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param clientSession the client session with which to associate this operation
   *  @param id       the ObjectId of the file to be deleted
   *  @return a future with a single element, representing that the file has been deleted
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> delete(ClientSession clientSession, JsonObject id);

  /**
   *  Given a {@code id}, delete this stored file's files collection document and associated chunks from a GridFS bucket.
   *  @param clientSession the client session with which to associate this operation
   *  @param id       the ObjectId of the file to be deleted
   *  @param handler an async result with a single element, representing that the file has been deleted
   *  @return a reference to <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  GridFSBucket delete(ClientSession clientSession, JsonObject id,
      Handler<AsyncResult<Future<Void>>> handler);

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
   *  @param handler an async result with a single element, representing that the file has been deleted
   *  @return a reference to <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  GridFSBucket delete(ClientSession clientSession, Object id,
      Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @return a future with a single element, representing that the file has been renamed
   */
  Future<Void> rename(JsonObject id, String newFilename);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @param handler an async result with a single element, representing that the file has been renamed
   *  @return a reference to <code>this</code>
   */
  GridFSBucket rename(JsonObject id, String newFilename,
      Handler<AsyncResult<Future<Void>>> handler);

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
   *  @param handler an async result with a single element, representing that the file has been renamed
   *  @return a reference to <code>this</code>
   */
  GridFSBucket rename(Object id, String newFilename, Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param clientSession the client session with which to associate this operation
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @return a future with a single element, representing that the file has been renamed
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> rename(ClientSession clientSession, JsonObject id, String newFilename);

  /**
   *  Renames the stored file with the specified {@code id}.
   *  @param clientSession the client session with which to associate this operation
   *  @param id          the id of the file in the files collection to rename
   *  @param newFilename the new filename for the file
   *  @param handler an async result with a single element, representing that the file has been renamed
   *  @return a reference to <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  GridFSBucket rename(ClientSession clientSession, JsonObject id, String newFilename,
      Handler<AsyncResult<Future<Void>>> handler);

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
   *  @param handler an async result with a single element, representing that the file has been renamed
   *  @return a reference to <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  GridFSBucket rename(ClientSession clientSession, Object id, String newFilename,
      Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Drops the data associated with this bucket from the database.
   *  @return a future with a single element, representing that the collections have been dropped
   */
  Future<Void> drop();

  /**
   *  Drops the data associated with this bucket from the database.
   *  @param handler an async result with a single element, representing that the collections have been dropped
   *  @return a reference to <code>this</code>
   */
  GridFSBucket drop(Handler<AsyncResult<Future<Void>>> handler);

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
   *  @param handler an async result with a single element, representing that the collections have been dropped
   *  @return a reference to <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  GridFSBucket drop(ClientSession clientSession, Handler<AsyncResult<Future<Void>>> handler);
}
