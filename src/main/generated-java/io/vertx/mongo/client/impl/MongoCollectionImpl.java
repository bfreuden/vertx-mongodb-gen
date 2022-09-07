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
package io.vertx.mongo.client.impl;

import static io.vertx.mongo.impl.Utils.setHandler;
import static java.util.Objects.requireNonNull;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.AggregatePublisher;
import com.mongodb.reactivestreams.client.ChangeStreamPublisher;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.ListIndexesPublisher;
import com.mongodb.reactivestreams.client.MapReducePublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoCollectionResult;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.bulk.BulkWriteResult;
import io.vertx.mongo.client.AggregateOptions;
import io.vertx.mongo.client.ChangeStreamOptions;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.FindOptions;
import io.vertx.mongo.client.ListIndexesOptions;
import io.vertx.mongo.client.MapReduceOptions;
import io.vertx.mongo.client.model.BulkWriteOptions;
import io.vertx.mongo.client.model.CountOptions;
import io.vertx.mongo.client.model.CreateIndexOptions;
import io.vertx.mongo.client.model.DeleteOptions;
import io.vertx.mongo.client.model.DropCollectionOptions;
import io.vertx.mongo.client.model.DropIndexOptions;
import io.vertx.mongo.client.model.EstimatedDocumentCountOptions;
import io.vertx.mongo.client.model.FindOneAndDeleteOptions;
import io.vertx.mongo.client.model.FindOneAndReplaceOptions;
import io.vertx.mongo.client.model.FindOneAndUpdateOptions;
import io.vertx.mongo.client.model.IndexModel;
import io.vertx.mongo.client.model.IndexOptions;
import io.vertx.mongo.client.model.InsertManyOptions;
import io.vertx.mongo.client.model.InsertOneOptions;
import io.vertx.mongo.client.model.RenameCollectionOptions;
import io.vertx.mongo.client.model.ReplaceOptions;
import io.vertx.mongo.client.model.UpdateOptions;
import io.vertx.mongo.client.model.WriteModel;
import io.vertx.mongo.client.model.changestream.ChangeStreamDocument;
import io.vertx.mongo.client.result.DeleteResult;
import io.vertx.mongo.client.result.InsertManyResult;
import io.vertx.mongo.client.result.InsertOneResult;
import io.vertx.mongo.client.result.UpdateResult;
import io.vertx.mongo.impl.CollectionsConversionUtils;
import io.vertx.mongo.impl.MappingPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoCollectionResultImpl;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.util.List;
import java.util.function.Function;
import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;

public class MongoCollectionImpl<TDocument> extends MongoCollectionBase<TDocument> {
  protected final MongoClientContext clientContext;

  protected final MongoCollection<TDocument> wrapped;

  protected final Function<TDocument, TDocument> inputMapper;

  protected final Function<TDocument, TDocument> outputMapper;

  protected final Function<TDocument, BsonValue> idProvider;

  protected final Class<TDocument> clazz;

  public MongoCollectionImpl(MongoClientContext clientContext, MongoCollection<TDocument> wrapped,
      Class<TDocument> clazz) {
    this.clientContext = clientContext;
    this.wrapped = wrapped;
    this.clazz = clazz;
    this.inputMapper = clientContext.getConfig().getInputDocumentMapper(clazz);
    this.outputMapper = clientContext.getConfig().getOutputDocumentMapper(clazz);
    this.idProvider = clientContext.getConfig().getDocumentIdProvider(clazz);
  }

  @Override
  public MongoNamespace getNamespace() {
    return wrapped.getNamespace();
  }

  @Override
  public Class<TDocument> getDocumentClass() {
    return wrapped.getDocumentClass();
  }

  @Override
  public CodecRegistry getCodecRegistry() {
    return wrapped.getCodecRegistry();
  }

  @Override
  public ReadPreference getReadPreference() {
    return wrapped.getReadPreference();
  }

  @Override
  public WriteConcern getWriteConcern() {
    return wrapped.getWriteConcern();
  }

  @Override
  public ReadConcern getReadConcern() {
    return wrapped.getReadConcern();
  }

  @Override
  public <NewTDocument> io.vertx.mongo.client.MongoCollection<NewTDocument> withDocumentClass(
      Class<NewTDocument> clazz) {
    requireNonNull(clazz, "clazz is null");
    MongoCollection<NewTDocument> __result = wrapped.withDocumentClass(clazz);
    return new MongoCollectionImpl<>(clientContext, __result, clazz);
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> withCodecRegistry(
      CodecRegistry codecRegistry) {
    requireNonNull(codecRegistry, "codecRegistry is null");
    MongoCollection<TDocument> __result = wrapped.withCodecRegistry(codecRegistry);
    return new MongoCollectionImpl<>(clientContext, __result, clazz);
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> withReadPreference(
      ReadPreference readPreference) {
    requireNonNull(readPreference, "readPreference is null");
    MongoCollection<TDocument> __result = wrapped.withReadPreference(readPreference);
    return new MongoCollectionImpl<>(clientContext, __result, clazz);
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> withWriteConcern(
      WriteConcern writeConcern) {
    requireNonNull(writeConcern, "writeConcern is null");
    MongoCollection<TDocument> __result = wrapped.withWriteConcern(writeConcern);
    return new MongoCollectionImpl<>(clientContext, __result, clazz);
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> withReadConcern(ReadConcern readConcern) {
    requireNonNull(readConcern, "readConcern is null");
    MongoCollection<TDocument> __result = wrapped.withReadConcern(readConcern);
    return new MongoCollectionImpl<>(clientContext, __result, clazz);
  }

  @Override
  public Future<Long> estimatedDocumentCount() {
    Publisher<Long> __publisher = wrapped.estimatedDocumentCount();
    Promise<Long> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void estimatedDocumentCount(Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> __future = this.estimatedDocumentCount();
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Long> estimatedDocumentCount(EstimatedDocumentCountOptions options) {
    requireNonNull(options, "options is null");
    com.mongodb.client.model.EstimatedDocumentCountOptions __options = options.toDriverClass(clientContext);
    Publisher<Long> __publisher = wrapped.estimatedDocumentCount(__options);
    Promise<Long> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void estimatedDocumentCount(EstimatedDocumentCountOptions options,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> __future = this.estimatedDocumentCount(options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Long> countDocuments() {
    Publisher<Long> __publisher = wrapped.countDocuments();
    Promise<Long> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void countDocuments(Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> __future = this.countDocuments();
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Long> countDocuments(JsonObject filter) {
    requireNonNull(filter, "filter is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Publisher<Long> __publisher = wrapped.countDocuments(__filter);
    Promise<Long> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void countDocuments(JsonObject filter, Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> __future = this.countDocuments(filter);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Long> countDocuments(JsonObject filter, CountOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    com.mongodb.client.model.CountOptions __options = options.toDriverClass(clientContext);
    Publisher<Long> __publisher = wrapped.countDocuments(__filter, __options);
    Promise<Long> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void countDocuments(JsonObject filter, CountOptions options,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> __future = this.countDocuments(filter, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Long> countDocuments(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Publisher<Long> __publisher = wrapped.countDocuments(__clientSession);
    Promise<Long> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void countDocuments(ClientSession clientSession,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> __future = this.countDocuments(clientSession);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Long> countDocuments(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    Publisher<Long> __publisher = wrapped.countDocuments(__clientSession, __filter);
    Promise<Long> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void countDocuments(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> __future = this.countDocuments(clientSession, filter);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Long> countDocuments(ClientSession clientSession, JsonObject filter,
      CountOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    com.mongodb.client.model.CountOptions __options = options.toDriverClass(clientContext);
    Publisher<Long> __publisher = wrapped.countDocuments(__clientSession, __filter, __options);
    Promise<Long> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void countDocuments(ClientSession clientSession, JsonObject filter, CountOptions options,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> __future = this.countDocuments(clientSession, filter, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public MongoResult<TDocument> find() {
    FindPublisher<TDocument> __publisher = wrapped.find();
    return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first);
  }

  @Override
  public MongoResult<TDocument> find(FindOptions options) {
    FindPublisher<TDocument> __publisher = wrapped.find();
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first);
    }
  }

  @Override
  public MongoResult<TDocument> find(JsonObject filter) {
    requireNonNull(filter, "filter is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    FindPublisher<TDocument> __publisher = wrapped.find(__filter);
    return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first);
  }

  @Override
  public MongoResult<TDocument> find(JsonObject filter, FindOptions options) {
    requireNonNull(filter, "filter is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    FindPublisher<TDocument> __publisher = wrapped.find(__filter);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first);
    }
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    FindPublisher<TDocument> __publisher = wrapped.find(__clientSession);
    return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first);
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession, FindOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    FindPublisher<TDocument> __publisher = wrapped.find(__clientSession);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first);
    }
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    FindPublisher<TDocument> __publisher = wrapped.find(__clientSession, __filter);
    return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first);
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession, JsonObject filter,
      FindOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    FindPublisher<TDocument> __publisher = wrapped.find(__clientSession, __filter);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __publisher, outputMapper, __publisher::first);
    }
  }

  @Override
  public MongoCollectionResult<TDocument> aggregate(JsonArray pipeline) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    AggregatePublisher<TDocument> __publisher = wrapped.aggregate(__pipeline);
    return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first);
  }

  @Override
  public MongoCollectionResult<TDocument> aggregate(JsonArray pipeline, AggregateOptions options) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    AggregatePublisher<TDocument> __publisher = wrapped.aggregate(__pipeline);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first, __batchSize);
    } else {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first);
    }
  }

  @Override
  public MongoCollectionResult<TDocument> aggregate(ClientSession clientSession,
      JsonArray pipeline) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    AggregatePublisher<TDocument> __publisher = wrapped.aggregate(__clientSession, __pipeline);
    return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first);
  }

  @Override
  public MongoCollectionResult<TDocument> aggregate(ClientSession clientSession, JsonArray pipeline,
      AggregateOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    AggregatePublisher<TDocument> __publisher = wrapped.aggregate(__clientSession, __pipeline);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first, __batchSize);
    } else {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first);
    }
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch() {
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(ChangeStreamOptions options) {
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(JsonArray pipeline) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__pipeline, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(JsonArray pipeline,
      ChangeStreamOptions options) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__pipeline, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession,
      ChangeStreamOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession,
      JsonArray pipeline) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, __pipeline, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession,
      JsonArray pipeline, ChangeStreamOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, __pipeline, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoCollectionResult<TDocument> mapReduce(String mapFunction, String reduceFunction) {
    requireNonNull(mapFunction, "mapFunction is null");
    requireNonNull(reduceFunction, "reduceFunction is null");
    MapReducePublisher<TDocument> __publisher = wrapped.mapReduce(mapFunction, reduceFunction);
    return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first);
  }

  @Override
  public MongoCollectionResult<TDocument> mapReduce(String mapFunction, String reduceFunction,
      MapReduceOptions options) {
    requireNonNull(mapFunction, "mapFunction is null");
    requireNonNull(reduceFunction, "reduceFunction is null");
    MapReducePublisher<TDocument> __publisher = wrapped.mapReduce(mapFunction, reduceFunction);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first, __batchSize);
    } else {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first);
    }
  }

  @Override
  public MongoCollectionResult<TDocument> mapReduce(ClientSession clientSession, String mapFunction,
      String reduceFunction) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(mapFunction, "mapFunction is null");
    requireNonNull(reduceFunction, "reduceFunction is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    MapReducePublisher<TDocument> __publisher = wrapped.mapReduce(__clientSession, mapFunction, reduceFunction);
    return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first);
  }

  @Override
  public MongoCollectionResult<TDocument> mapReduce(ClientSession clientSession, String mapFunction,
      String reduceFunction, MapReduceOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(mapFunction, "mapFunction is null");
    requireNonNull(reduceFunction, "reduceFunction is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    MapReducePublisher<TDocument> __publisher = wrapped.mapReduce(__clientSession, mapFunction, reduceFunction);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first, __batchSize);
    } else {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, outputMapper, __publisher::first);
    }
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(
      List<? extends WriteModel<? extends TDocument>> requests) {
    requireNonNull(requests, "requests is null");
    List<? extends com.mongodb.client.model.WriteModel<? extends TDocument>> __requests = CollectionsConversionUtils.mapItems(((List<? extends WriteModel<TDocument>>)requests), _item -> _item.toDriverClass(clientContext, inputMapper));
    Publisher<com.mongodb.bulk.BulkWriteResult> __publisher = wrapped.bulkWrite(__requests);
    Promise<com.mongodb.bulk.BulkWriteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> BulkWriteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void bulkWrite(List<? extends WriteModel<? extends TDocument>> requests,
      Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> __future = this.bulkWrite(requests);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(List<? extends WriteModel<? extends TDocument>> requests,
      BulkWriteOptions options) {
    requireNonNull(requests, "requests is null");
    requireNonNull(options, "options is null");
    List<? extends com.mongodb.client.model.WriteModel<? extends TDocument>> __requests = CollectionsConversionUtils.mapItems(((List<? extends WriteModel<TDocument>>)requests), _item -> _item.toDriverClass(clientContext, inputMapper));
    com.mongodb.client.model.BulkWriteOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.bulk.BulkWriteResult> __publisher = wrapped.bulkWrite(__requests, __options);
    Promise<com.mongodb.bulk.BulkWriteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> BulkWriteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void bulkWrite(List<? extends WriteModel<? extends TDocument>> requests,
      BulkWriteOptions options, Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> __future = this.bulkWrite(requests, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(ClientSession clientSession,
      List<? extends WriteModel<? extends TDocument>> requests) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(requests, "requests is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    List<? extends com.mongodb.client.model.WriteModel<? extends TDocument>> __requests = CollectionsConversionUtils.mapItems(((List<? extends WriteModel<TDocument>>)requests), _item -> _item.toDriverClass(clientContext, inputMapper));
    Publisher<com.mongodb.bulk.BulkWriteResult> __publisher = wrapped.bulkWrite(__clientSession, __requests);
    Promise<com.mongodb.bulk.BulkWriteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> BulkWriteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void bulkWrite(ClientSession clientSession,
      List<? extends WriteModel<? extends TDocument>> requests,
      Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> __future = this.bulkWrite(clientSession, requests);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(ClientSession clientSession,
      List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(requests, "requests is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    List<? extends com.mongodb.client.model.WriteModel<? extends TDocument>> __requests = CollectionsConversionUtils.mapItems(((List<? extends WriteModel<TDocument>>)requests), _item -> _item.toDriverClass(clientContext, inputMapper));
    com.mongodb.client.model.BulkWriteOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.bulk.BulkWriteResult> __publisher = wrapped.bulkWrite(__clientSession, __requests, __options);
    Promise<com.mongodb.bulk.BulkWriteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> BulkWriteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void bulkWrite(ClientSession clientSession,
      List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options,
      Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> __future = this.bulkWrite(clientSession, requests, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<InsertOneResult> insertOne(TDocument document) {
    requireNonNull(document, "document is null");
    document = mapDoc(document, inputMapper);
    Publisher<com.mongodb.client.result.InsertOneResult> __publisher = wrapped.insertOne(document);
    Promise<com.mongodb.client.result.InsertOneResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> InsertOneResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void insertOne(TDocument document, Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> __future = this.insertOne(document);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<InsertOneResult> insertOne(TDocument document, InsertOneOptions options) {
    requireNonNull(document, "document is null");
    requireNonNull(options, "options is null");
    document = mapDoc(document, inputMapper);
    com.mongodb.client.model.InsertOneOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.InsertOneResult> __publisher = wrapped.insertOne(document, __options);
    Promise<com.mongodb.client.result.InsertOneResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> InsertOneResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void insertOne(TDocument document, InsertOneOptions options,
      Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> __future = this.insertOne(document, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<InsertOneResult> insertOne(ClientSession clientSession, TDocument document) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(document, "document is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    document = mapDoc(document, inputMapper);
    Publisher<com.mongodb.client.result.InsertOneResult> __publisher = wrapped.insertOne(__clientSession, document);
    Promise<com.mongodb.client.result.InsertOneResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> InsertOneResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void insertOne(ClientSession clientSession, TDocument document,
      Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> __future = this.insertOne(clientSession, document);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<InsertOneResult> insertOne(ClientSession clientSession, TDocument document,
      InsertOneOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(document, "document is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    document = mapDoc(document, inputMapper);
    com.mongodb.client.model.InsertOneOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.InsertOneResult> __publisher = wrapped.insertOne(__clientSession, document, __options);
    Promise<com.mongodb.client.result.InsertOneResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> InsertOneResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void insertOne(ClientSession clientSession, TDocument document, InsertOneOptions options,
      Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> __future = this.insertOne(clientSession, document, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<InsertManyResult> insertMany(List<? extends TDocument> documents) {
    requireNonNull(documents, "documents is null");
    documents = mapDocList(documents, inputMapper);
    Publisher<com.mongodb.client.result.InsertManyResult> __publisher = wrapped.insertMany(documents);
    Promise<com.mongodb.client.result.InsertManyResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> InsertManyResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void insertMany(List<? extends TDocument> documents,
      Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> __future = this.insertMany(documents);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<InsertManyResult> insertMany(List<? extends TDocument> documents,
      InsertManyOptions options) {
    requireNonNull(documents, "documents is null");
    requireNonNull(options, "options is null");
    documents = mapDocList(documents, inputMapper);
    com.mongodb.client.model.InsertManyOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.InsertManyResult> __publisher = wrapped.insertMany(documents, __options);
    Promise<com.mongodb.client.result.InsertManyResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> InsertManyResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void insertMany(List<? extends TDocument> documents, InsertManyOptions options,
      Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> __future = this.insertMany(documents, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<InsertManyResult> insertMany(ClientSession clientSession,
      List<? extends TDocument> documents) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(documents, "documents is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    documents = mapDocList(documents, inputMapper);
    Publisher<com.mongodb.client.result.InsertManyResult> __publisher = wrapped.insertMany(__clientSession, documents);
    Promise<com.mongodb.client.result.InsertManyResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> InsertManyResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void insertMany(ClientSession clientSession, List<? extends TDocument> documents,
      Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> __future = this.insertMany(clientSession, documents);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<InsertManyResult> insertMany(ClientSession clientSession,
      List<? extends TDocument> documents, InsertManyOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(documents, "documents is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    documents = mapDocList(documents, inputMapper);
    com.mongodb.client.model.InsertManyOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.InsertManyResult> __publisher = wrapped.insertMany(__clientSession, documents, __options);
    Promise<com.mongodb.client.result.InsertManyResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> InsertManyResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void insertMany(ClientSession clientSession, List<? extends TDocument> documents,
      InsertManyOptions options, Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> __future = this.insertMany(clientSession, documents, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<DeleteResult> deleteOne(JsonObject filter) {
    requireNonNull(filter, "filter is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Publisher<com.mongodb.client.result.DeleteResult> __publisher = wrapped.deleteOne(__filter);
    Promise<com.mongodb.client.result.DeleteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> DeleteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void deleteOne(JsonObject filter, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> __future = this.deleteOne(filter);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<DeleteResult> deleteOne(JsonObject filter, DeleteOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    com.mongodb.client.model.DeleteOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.DeleteResult> __publisher = wrapped.deleteOne(__filter, __options);
    Promise<com.mongodb.client.result.DeleteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> DeleteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void deleteOne(JsonObject filter, DeleteOptions options,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> __future = this.deleteOne(filter, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<DeleteResult> deleteOne(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    Publisher<com.mongodb.client.result.DeleteResult> __publisher = wrapped.deleteOne(__clientSession, __filter);
    Promise<com.mongodb.client.result.DeleteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> DeleteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void deleteOne(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> __future = this.deleteOne(clientSession, filter);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<DeleteResult> deleteOne(ClientSession clientSession, JsonObject filter,
      DeleteOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    com.mongodb.client.model.DeleteOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.DeleteResult> __publisher = wrapped.deleteOne(__clientSession, __filter, __options);
    Promise<com.mongodb.client.result.DeleteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> DeleteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void deleteOne(ClientSession clientSession, JsonObject filter, DeleteOptions options,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> __future = this.deleteOne(clientSession, filter, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<DeleteResult> deleteMany(JsonObject filter) {
    requireNonNull(filter, "filter is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Publisher<com.mongodb.client.result.DeleteResult> __publisher = wrapped.deleteMany(__filter);
    Promise<com.mongodb.client.result.DeleteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> DeleteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void deleteMany(JsonObject filter, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> __future = this.deleteMany(filter);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<DeleteResult> deleteMany(JsonObject filter, DeleteOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    com.mongodb.client.model.DeleteOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.DeleteResult> __publisher = wrapped.deleteMany(__filter, __options);
    Promise<com.mongodb.client.result.DeleteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> DeleteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void deleteMany(JsonObject filter, DeleteOptions options,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> __future = this.deleteMany(filter, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<DeleteResult> deleteMany(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    Publisher<com.mongodb.client.result.DeleteResult> __publisher = wrapped.deleteMany(__clientSession, __filter);
    Promise<com.mongodb.client.result.DeleteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> DeleteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void deleteMany(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> __future = this.deleteMany(clientSession, filter);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<DeleteResult> deleteMany(ClientSession clientSession, JsonObject filter,
      DeleteOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    com.mongodb.client.model.DeleteOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.DeleteResult> __publisher = wrapped.deleteMany(__clientSession, __filter, __options);
    Promise<com.mongodb.client.result.DeleteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> DeleteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void deleteMany(ClientSession clientSession, JsonObject filter, DeleteOptions options,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> __future = this.deleteMany(clientSession, filter, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> replaceOne(JsonObject filter, TDocument replacement) {
    requireNonNull(filter, "filter is null");
    requireNonNull(replacement, "replacement is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    replacement = mapDoc(replacement, inputMapper);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.replaceOne(__filter, replacement);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void replaceOne(JsonObject filter, TDocument replacement,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.replaceOne(filter, replacement);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> replaceOne(TDocument replacement) {
    requireNonNull(replacement, "replacement is null");
    Bson __filter = Filters.eq(idProvider.apply(replacement));
    replacement = mapDoc(replacement, inputMapper);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.replaceOne(__filter, replacement);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void replaceOne(TDocument replacement, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.replaceOne(replacement);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> replaceOne(JsonObject filter, TDocument replacement,
      ReplaceOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(replacement, "replacement is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    replacement = mapDoc(replacement, inputMapper);
    com.mongodb.client.model.ReplaceOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.replaceOne(__filter, replacement, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void replaceOne(JsonObject filter, TDocument replacement, ReplaceOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.replaceOne(filter, replacement, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> replaceOne(TDocument replacement, ReplaceOptions options) {
    requireNonNull(replacement, "replacement is null");
    requireNonNull(options, "options is null");
    Bson __filter = Filters.eq(idProvider.apply(replacement));
    replacement = mapDoc(replacement, inputMapper);
    com.mongodb.client.model.ReplaceOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.replaceOne(__filter, replacement, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void replaceOne(TDocument replacement, ReplaceOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.replaceOne(replacement, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(replacement, "replacement is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    replacement = mapDoc(replacement, inputMapper);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.replaceOne(__clientSession, __filter, replacement);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void replaceOne(ClientSession clientSession, JsonObject filter, TDocument replacement,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.replaceOne(clientSession, filter, replacement);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> replaceOne(ClientSession clientSession, TDocument replacement) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(replacement, "replacement is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = Filters.eq(idProvider.apply(replacement));
    replacement = mapDoc(replacement, inputMapper);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.replaceOne(__clientSession, __filter, replacement);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void replaceOne(ClientSession clientSession, TDocument replacement,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.replaceOne(clientSession, replacement);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement, ReplaceOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(replacement, "replacement is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    replacement = mapDoc(replacement, inputMapper);
    com.mongodb.client.model.ReplaceOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.replaceOne(__clientSession, __filter, replacement, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void replaceOne(ClientSession clientSession, JsonObject filter, TDocument replacement,
      ReplaceOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.replaceOne(clientSession, filter, replacement, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> replaceOne(ClientSession clientSession, TDocument replacement,
      ReplaceOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(replacement, "replacement is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = Filters.eq(idProvider.apply(replacement));
    replacement = mapDoc(replacement, inputMapper);
    com.mongodb.client.model.ReplaceOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.replaceOne(__clientSession, __filter, replacement, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void replaceOne(ClientSession clientSession, TDocument replacement, ReplaceOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.replaceOne(clientSession, replacement, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, JsonObject update) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateOne(__filter, __update);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateOne(JsonObject filter, JsonObject update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateOne(filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, JsonObject update,
      UpdateOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateOne(__filter, __update, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateOne(JsonObject filter, JsonObject update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateOne(filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateOne(__clientSession, __filter, __update);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateOne(ClientSession clientSession, JsonObject filter, JsonObject update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateOne(clientSession, filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateOne(__clientSession, __filter, __update, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateOne(ClientSession clientSession, JsonObject filter, JsonObject update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateOne(clientSession, filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, JsonArray update) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateOne(__filter, __update);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateOne(JsonObject filter, JsonArray update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateOne(filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, JsonArray update,
      UpdateOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateOne(__filter, __update, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateOne(JsonObject filter, JsonArray update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateOne(filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      JsonArray update) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateOne(__clientSession, __filter, __update);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateOne(ClientSession clientSession, JsonObject filter, JsonArray update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateOne(clientSession, filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      JsonArray update, UpdateOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateOne(__clientSession, __filter, __update, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateOne(ClientSession clientSession, JsonObject filter, JsonArray update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateOne(clientSession, filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, JsonObject update) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateMany(__filter, __update);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateMany(JsonObject filter, JsonObject update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateMany(filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, JsonObject update,
      UpdateOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateMany(__filter, __update, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateMany(JsonObject filter, JsonObject update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateMany(filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateMany(__clientSession, __filter, __update);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateMany(ClientSession clientSession, JsonObject filter, JsonObject update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateMany(clientSession, filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateMany(__clientSession, __filter, __update, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateMany(ClientSession clientSession, JsonObject filter, JsonObject update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateMany(clientSession, filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, JsonArray update) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateMany(__filter, __update);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateMany(JsonObject filter, JsonArray update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateMany(filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, JsonArray update,
      UpdateOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateMany(__filter, __update, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateMany(JsonObject filter, JsonArray update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateMany(filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      JsonArray update) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateMany(__clientSession, __filter, __update);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateMany(ClientSession clientSession, JsonObject filter, JsonArray update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateMany(clientSession, filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      JsonArray update, UpdateOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.result.UpdateResult> __publisher = wrapped.updateMany(__clientSession, __filter, __update, __options);
    Promise<com.mongodb.client.result.UpdateResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> UpdateResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void updateMany(ClientSession clientSession, JsonObject filter, JsonArray update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> __future = this.updateMany(clientSession, filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndDelete(JsonObject filter) {
    requireNonNull(filter, "filter is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Publisher<TDocument> __publisher = wrapped.findOneAndDelete(__filter);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndDelete(JsonObject filter, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndDelete(filter);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndDelete(JsonObject filter, FindOneAndDeleteOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    com.mongodb.client.model.FindOneAndDeleteOptions __options = options.toDriverClass(clientContext);
    Publisher<TDocument> __publisher = wrapped.findOneAndDelete(__filter, __options);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndDelete(JsonObject filter, FindOneAndDeleteOptions options,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndDelete(filter, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    Publisher<TDocument> __publisher = wrapped.findOneAndDelete(__clientSession, __filter);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndDelete(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndDelete(clientSession, filter);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter,
      FindOneAndDeleteOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    com.mongodb.client.model.FindOneAndDeleteOptions __options = options.toDriverClass(clientContext);
    Publisher<TDocument> __publisher = wrapped.findOneAndDelete(__clientSession, __filter, __options);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndDelete(ClientSession clientSession, JsonObject filter,
      FindOneAndDeleteOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndDelete(clientSession, filter, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement) {
    requireNonNull(filter, "filter is null");
    requireNonNull(replacement, "replacement is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    replacement = mapDoc(replacement, inputMapper);
    Publisher<TDocument> __publisher = wrapped.findOneAndReplace(__filter, replacement);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndReplace(JsonObject filter, TDocument replacement,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndReplace(filter, replacement);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement,
      FindOneAndReplaceOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(replacement, "replacement is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    replacement = mapDoc(replacement, inputMapper);
    com.mongodb.client.model.FindOneAndReplaceOptions __options = options.toDriverClass(clientContext);
    Publisher<TDocument> __publisher = wrapped.findOneAndReplace(__filter, replacement, __options);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndReplace(JsonObject filter, TDocument replacement,
      FindOneAndReplaceOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndReplace(filter, replacement, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(replacement, "replacement is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    replacement = mapDoc(replacement, inputMapper);
    Publisher<TDocument> __publisher = wrapped.findOneAndReplace(__clientSession, __filter, replacement);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndReplace(clientSession, filter, replacement);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement, FindOneAndReplaceOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(replacement, "replacement is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    replacement = mapDoc(replacement, inputMapper);
    com.mongodb.client.model.FindOneAndReplaceOptions __options = options.toDriverClass(clientContext);
    Publisher<TDocument> __publisher = wrapped.findOneAndReplace(__clientSession, __filter, replacement, __options);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement, FindOneAndReplaceOptions options,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndReplace(clientSession, filter, replacement, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    Publisher<TDocument> __publisher = wrapped.findOneAndUpdate(__filter, __update);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndUpdate(JsonObject filter, JsonObject update,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndUpdate(filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update,
      FindOneAndUpdateOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    com.mongodb.client.model.FindOneAndUpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<TDocument> __publisher = wrapped.findOneAndUpdate(__filter, __update, __options);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndUpdate(JsonObject filter, JsonObject update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndUpdate(filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    Publisher<TDocument> __publisher = wrapped.findOneAndUpdate(__clientSession, __filter, __update);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndUpdate(ClientSession clientSession, JsonObject filter, JsonObject update,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndUpdate(clientSession, filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update, FindOneAndUpdateOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    Bson __update = clientContext.getMapper().toBson(update);
    com.mongodb.client.model.FindOneAndUpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<TDocument> __publisher = wrapped.findOneAndUpdate(__clientSession, __filter, __update, __options);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndUpdate(ClientSession clientSession, JsonObject filter, JsonObject update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndUpdate(clientSession, filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, JsonArray update) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    Publisher<TDocument> __publisher = wrapped.findOneAndUpdate(__filter, __update);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndUpdate(JsonObject filter, JsonArray update,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndUpdate(filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, JsonArray update,
      FindOneAndUpdateOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    com.mongodb.client.model.FindOneAndUpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<TDocument> __publisher = wrapped.findOneAndUpdate(__filter, __update, __options);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndUpdate(JsonObject filter, JsonArray update, FindOneAndUpdateOptions options,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndUpdate(filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonArray update) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    Publisher<TDocument> __publisher = wrapped.findOneAndUpdate(__clientSession, __filter, __update);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndUpdate(ClientSession clientSession, JsonObject filter, JsonArray update,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndUpdate(clientSession, filter, update);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonArray update, FindOneAndUpdateOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    requireNonNull(update, "update is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __filter = clientContext.getMapper().toBson(filter);
    List<? extends Bson> __update = clientContext.getMapper().toBsonList(update);
    com.mongodb.client.model.FindOneAndUpdateOptions __options = options.toDriverClass(clientContext);
    Publisher<TDocument> __publisher = wrapped.findOneAndUpdate(__clientSession, __filter, __update, __options);
    Promise<TDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (outputMapper == null) {
      return __promise.future();
    } else {
      return __promise.future().map(outputMapper);
    }
  }

  @Override
  public void findOneAndUpdate(ClientSession clientSession, JsonObject filter, JsonArray update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> __future = this.findOneAndUpdate(clientSession, filter, update, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> drop() {
    Publisher<Void> __publisher = wrapped.drop();
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void drop(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.drop();
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> drop(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.drop(__clientSession);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void drop(ClientSession clientSession, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.drop(clientSession);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> drop(DropCollectionOptions dropCollectionOptions) {
    requireNonNull(dropCollectionOptions, "dropCollectionOptions is null");
    com.mongodb.client.model.DropCollectionOptions __dropCollectionOptions = dropCollectionOptions.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.drop(__dropCollectionOptions);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void drop(DropCollectionOptions dropCollectionOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.drop(dropCollectionOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> drop(ClientSession clientSession,
      DropCollectionOptions dropCollectionOptions) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(dropCollectionOptions, "dropCollectionOptions is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    com.mongodb.client.model.DropCollectionOptions __dropCollectionOptions = dropCollectionOptions.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.drop(__clientSession, __dropCollectionOptions);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void drop(ClientSession clientSession, DropCollectionOptions dropCollectionOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.drop(clientSession, dropCollectionOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<String> createIndex(JsonObject key) {
    requireNonNull(key, "key is null");
    Bson __key = clientContext.getMapper().toBson(key);
    Publisher<String> __publisher = wrapped.createIndex(__key);
    Promise<String> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void createIndex(JsonObject key, Handler<AsyncResult<String>> resultHandler) {
    Future<String> __future = this.createIndex(key);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<String> createIndex(JsonObject key, IndexOptions options) {
    requireNonNull(key, "key is null");
    requireNonNull(options, "options is null");
    Bson __key = clientContext.getMapper().toBson(key);
    com.mongodb.client.model.IndexOptions __options = options.toDriverClass(clientContext);
    Publisher<String> __publisher = wrapped.createIndex(__key, __options);
    Promise<String> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void createIndex(JsonObject key, IndexOptions options,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> __future = this.createIndex(key, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<String> createIndex(ClientSession clientSession, JsonObject key) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(key, "key is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __key = clientContext.getMapper().toBson(key);
    Publisher<String> __publisher = wrapped.createIndex(__clientSession, __key);
    Promise<String> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void createIndex(ClientSession clientSession, JsonObject key,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> __future = this.createIndex(clientSession, key);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<String> createIndex(ClientSession clientSession, JsonObject key,
      IndexOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(key, "key is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __key = clientContext.getMapper().toBson(key);
    com.mongodb.client.model.IndexOptions __options = options.toDriverClass(clientContext);
    Publisher<String> __publisher = wrapped.createIndex(__clientSession, __key, __options);
    Promise<String> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void createIndex(ClientSession clientSession, JsonObject key, IndexOptions options,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> __future = this.createIndex(clientSession, key, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<String> createIndexes(List<IndexModel> indexes) {
    requireNonNull(indexes, "indexes is null");
    List<com.mongodb.client.model.IndexModel> __indexes = CollectionsConversionUtils.mapItems(indexes, _item -> _item.toDriverClass(clientContext));
    Publisher<String> __publisher = wrapped.createIndexes(__indexes);
    Promise<String> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void createIndexes(List<IndexModel> indexes, Handler<AsyncResult<String>> resultHandler) {
    Future<String> __future = this.createIndexes(indexes);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<String> createIndexes(List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions) {
    requireNonNull(indexes, "indexes is null");
    requireNonNull(createIndexOptions, "createIndexOptions is null");
    List<com.mongodb.client.model.IndexModel> __indexes = CollectionsConversionUtils.mapItems(indexes, _item -> _item.toDriverClass(clientContext));
    com.mongodb.client.model.CreateIndexOptions __createIndexOptions = createIndexOptions.toDriverClass(clientContext);
    Publisher<String> __publisher = wrapped.createIndexes(__indexes, __createIndexOptions);
    Promise<String> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void createIndexes(List<IndexModel> indexes, CreateIndexOptions createIndexOptions,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> __future = this.createIndexes(indexes, createIndexOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(indexes, "indexes is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    List<com.mongodb.client.model.IndexModel> __indexes = CollectionsConversionUtils.mapItems(indexes, _item -> _item.toDriverClass(clientContext));
    Publisher<String> __publisher = wrapped.createIndexes(__clientSession, __indexes);
    Promise<String> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void createIndexes(ClientSession clientSession, List<IndexModel> indexes,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> __future = this.createIndexes(clientSession, indexes);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(indexes, "indexes is null");
    requireNonNull(createIndexOptions, "createIndexOptions is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    List<com.mongodb.client.model.IndexModel> __indexes = CollectionsConversionUtils.mapItems(indexes, _item -> _item.toDriverClass(clientContext));
    com.mongodb.client.model.CreateIndexOptions __createIndexOptions = createIndexOptions.toDriverClass(clientContext);
    Publisher<String> __publisher = wrapped.createIndexes(__clientSession, __indexes, __createIndexOptions);
    Promise<String> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void createIndexes(ClientSession clientSession, List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions, Handler<AsyncResult<String>> resultHandler) {
    Future<String> __future = this.createIndexes(clientSession, indexes, createIndexOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public MongoResult<JsonObject> listIndexes() {
    ListIndexesPublisher<JsonObject> __publisher = wrapped.listIndexes(JsonObject.class);
    return new MongoResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper(), __publisher::first);
  }

  @Override
  public MongoResult<JsonObject> listIndexes(ListIndexesOptions options) {
    ListIndexesPublisher<JsonObject> __publisher = wrapped.listIndexes(JsonObject.class);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper(), __publisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper(), __publisher::first);
    }
  }

  @Override
  public MongoResult<JsonObject> listIndexes(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    ListIndexesPublisher<JsonObject> __publisher = wrapped.listIndexes(__clientSession, JsonObject.class);
    return new MongoResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper(), __publisher::first);
  }

  @Override
  public MongoResult<JsonObject> listIndexes(ClientSession clientSession,
      ListIndexesOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    ListIndexesPublisher<JsonObject> __publisher = wrapped.listIndexes(__clientSession, JsonObject.class);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper(), __publisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper(), __publisher::first);
    }
  }

  @Override
  public Future<Void> dropIndex(String indexName) {
    requireNonNull(indexName, "indexName is null");
    Publisher<Void> __publisher = wrapped.dropIndex(indexName);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndex(String indexName, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndex(indexName);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndex(JsonObject keys) {
    requireNonNull(keys, "keys is null");
    Bson __keys = clientContext.getMapper().toBson(keys);
    Publisher<Void> __publisher = wrapped.dropIndex(__keys);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndex(JsonObject keys, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndex(keys);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndex(String indexName, DropIndexOptions dropIndexOptions) {
    requireNonNull(indexName, "indexName is null");
    requireNonNull(dropIndexOptions, "dropIndexOptions is null");
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.dropIndex(indexName, __dropIndexOptions);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndex(String indexName, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndex(indexName, dropIndexOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndex(JsonObject keys, DropIndexOptions dropIndexOptions) {
    requireNonNull(keys, "keys is null");
    requireNonNull(dropIndexOptions, "dropIndexOptions is null");
    Bson __keys = clientContext.getMapper().toBson(keys);
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.dropIndex(__keys, __dropIndexOptions);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndex(JsonObject keys, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndex(keys, dropIndexOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, String indexName) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(indexName, "indexName is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.dropIndex(__clientSession, indexName);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndex(ClientSession clientSession, String indexName,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndex(clientSession, indexName);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, JsonObject keys) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(keys, "keys is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __keys = clientContext.getMapper().toBson(keys);
    Publisher<Void> __publisher = wrapped.dropIndex(__clientSession, __keys);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndex(ClientSession clientSession, JsonObject keys,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndex(clientSession, keys);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, String indexName,
      DropIndexOptions dropIndexOptions) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(indexName, "indexName is null");
    requireNonNull(dropIndexOptions, "dropIndexOptions is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.dropIndex(__clientSession, indexName, __dropIndexOptions);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndex(ClientSession clientSession, String indexName,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndex(clientSession, indexName, dropIndexOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, JsonObject keys,
      DropIndexOptions dropIndexOptions) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(keys, "keys is null");
    requireNonNull(dropIndexOptions, "dropIndexOptions is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Bson __keys = clientContext.getMapper().toBson(keys);
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.dropIndex(__clientSession, __keys, __dropIndexOptions);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndex(ClientSession clientSession, JsonObject keys,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndex(clientSession, keys, dropIndexOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndexes() {
    Publisher<Void> __publisher = wrapped.dropIndexes();
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndexes(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndexes();
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndexes(DropIndexOptions dropIndexOptions) {
    requireNonNull(dropIndexOptions, "dropIndexOptions is null");
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.dropIndexes(__dropIndexOptions);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndexes(DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndexes(dropIndexOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndexes(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.dropIndexes(__clientSession);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndexes(ClientSession clientSession, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndexes(clientSession);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> dropIndexes(ClientSession clientSession, DropIndexOptions dropIndexOptions) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(dropIndexOptions, "dropIndexOptions is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.dropIndexes(__clientSession, __dropIndexOptions);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void dropIndexes(ClientSession clientSession, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.dropIndexes(clientSession, dropIndexOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> renameCollection(MongoNamespace newCollectionNamespace) {
    requireNonNull(newCollectionNamespace, "newCollectionNamespace is null");
    Publisher<Void> __publisher = wrapped.renameCollection(newCollectionNamespace);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void renameCollection(MongoNamespace newCollectionNamespace,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.renameCollection(newCollectionNamespace);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> renameCollection(MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options) {
    requireNonNull(newCollectionNamespace, "newCollectionNamespace is null");
    requireNonNull(options, "options is null");
    com.mongodb.client.model.RenameCollectionOptions __options = options.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.renameCollection(newCollectionNamespace, __options);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void renameCollection(MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.renameCollection(newCollectionNamespace, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(newCollectionNamespace, "newCollectionNamespace is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.renameCollection(__clientSession, newCollectionNamespace);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void renameCollection(ClientSession clientSession, MongoNamespace newCollectionNamespace,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.renameCollection(clientSession, newCollectionNamespace);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace, RenameCollectionOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(newCollectionNamespace, "newCollectionNamespace is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    com.mongodb.client.model.RenameCollectionOptions __options = options.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.renameCollection(__clientSession, newCollectionNamespace, __options);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void renameCollection(ClientSession clientSession, MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.renameCollection(clientSession, newCollectionNamespace, options);
    setHandler(__future, resultHandler);
  }

  public MongoClientContext getClientContext() {
    return clientContext;
  }

  public MongoCollection<TDocument> toDriverClass(MongoClientContext clientContext) {
    return wrapped;
  }
}
