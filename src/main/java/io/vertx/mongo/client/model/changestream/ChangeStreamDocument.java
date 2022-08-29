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
package io.vertx.mongo.client.model.changestream;

import static java.util.Objects.requireNonNull;

import com.mongodb.MongoNamespace;
import com.mongodb.client.model.changestream.OperationType;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Exception;
import java.lang.Long;
import java.lang.String;

public class ChangeStreamDocument<TDocument> {
  private JsonObject resumeToken;

  private MongoNamespace namespace;

  private JsonObject namespaceDocument;

  private MongoNamespace destinationNamespace;

  private JsonObject destinationNamespaceDocument;

  private String databaseName;

  private TDocument fullDocument;

  private JsonObject documentKey;

  private Long clusterTime;

  private OperationType operationType;

  private UpdateDescription updateDescription;

  private Long txnNumber;

  private JsonObject lsid;

  private Exception resumeTokenException;

  private Exception namespaceException;

  private Exception namespaceDocumentException;

  private Exception destinationNamespaceException;

  private Exception destinationNamespaceDocumentException;

  private Exception databaseNameException;

  private Exception fullDocumentException;

  private Exception documentKeyException;

  private Exception clusterTimeException;

  private Exception operationTypeException;

  private Exception updateDescriptionException;

  private Exception txnNumberException;

  private Exception lsidException;

  private ChangeStreamDocument() {
  }

  /**
   *  Returns the resumeToken
   *
   *  @return the resumeToken
   */
  public JsonObject getResumeToken() {
    if (resumeTokenException != null)  {
      throw new RuntimeException(resumeTokenException);
    }
    return resumeToken;
  }

  /**
   *  Returns the namespace, derived from the "ns" field in a change stream document.
   *
   *  The invalidate operation type does include a MongoNamespace in the ChangeStreamDocument response. The
   *  dropDatabase operation type includes a MongoNamespace, but does not include a collection name as part
   *  of the namespace.
   *
   *  @return the namespace. If the namespaceDocument is null or if it is missing either the 'db' or 'coll' keys,
   *  then this will return null.
   */
  public MongoNamespace getNamespace() {
    if (namespaceException != null)  {
      throw new RuntimeException(namespaceException);
    }
    return namespace;
  }

  /**
   *  Returns the namespace cocument, derived from the "ns" field in a change stream document.
   *
   *  The namespace document is a BsonDocument containing the values associated with a MongoNamespace. The
   *  'db' key refers to the database name and the 'coll' key refers to the collection name.
   *
   *  @return the namespaceDocument
   *  @since 3.8
   */
  public JsonObject getNamespaceDocument() {
    if (namespaceDocumentException != null)  {
      throw new RuntimeException(namespaceDocumentException);
    }
    return namespaceDocument;
  }

  /**
   *  Returns the destination namespace, derived from the "to" field in a change stream document.
   *
   *  <p>
   *  The destination namespace is used to indicate the destination of a collection rename event.
   *  </p>
   *
   *  @return the namespace. If the "to" document is null or absent, then this will return null.
   *  @see OperationType#RENAME
   *  @since 3.11
   */
  public MongoNamespace getDestinationNamespace() {
    if (destinationNamespaceException != null)  {
      throw new RuntimeException(destinationNamespaceException);
    }
    return destinationNamespace;
  }

  /**
   *  Returns the destination namespace document, derived from the "to" field in a change stream document.
   *
   *  <p>
   *  The destination namespace document is a BsonDocument containing the values associated with a MongoNamespace. The
   *  'db' key refers to the database name and the 'coll' key refers to the collection name.
   *  </p>
   *  @return the destinationNamespaceDocument
   *  @since 3.11
   */
  public JsonObject getDestinationNamespaceDocument() {
    if (destinationNamespaceDocumentException != null)  {
      throw new RuntimeException(destinationNamespaceDocumentException);
    }
    return destinationNamespaceDocument;
  }

  /**
   *  Returns the database name
   *
   *  @return the databaseName. If the namespaceDocument is null or if it is missing the 'db' key, then this will
   *  return null.
   *  @since 3.8
   */
  public String getDatabaseName() {
    if (databaseNameException != null)  {
      throw new RuntimeException(databaseNameException);
    }
    return databaseName;
  }

  /**
   *  Returns the fullDocument
   *
   *  @return the fullDocument
   */
  public TDocument getFullDocument() {
    if (fullDocumentException != null)  {
      throw new RuntimeException(fullDocumentException);
    }
    return fullDocument;
  }

  /**
   *  Returns a document containing just the _id of the changed document.
   *  <p>
   *  For unsharded collections this contains a single field, _id, with the
   *  value of the _id of the document updated.  For sharded collections,
   *  this will contain all the components of the shard key in order,
   *  followed by the _id if the _id isn’t part of the shard key.
   *  </p>
   *
   *  @return the document key, or null if the event is not associated with a single document (e.g. a collection rename event)
   */
  public JsonObject getDocumentKey() {
    if (documentKeyException != null)  {
      throw new RuntimeException(documentKeyException);
    }
    return documentKey;
  }

  /**
   *  Gets the cluster time at which the change occurred.
   *
   *  @return the cluster time at which the change occurred
   *  @since 3.8
   *  @mongodb.server.release 4.0
   */
  public Long getClusterTime() {
    if (clusterTimeException != null)  {
      throw new RuntimeException(clusterTimeException);
    }
    return clusterTime;
  }

  /**
   *  Returns the operationType
   *
   *  @return the operationType
   */
  public OperationType getOperationType() {
    if (operationTypeException != null)  {
      throw new RuntimeException(operationTypeException);
    }
    return operationType;
  }

  /**
   *  Returns the updateDescription
   *
   *  @return the updateDescription, or null if the event is not associated with a single document (e.g. a collection rename event)
   */
  public UpdateDescription getUpdateDescription() {
    if (updateDescriptionException != null)  {
      throw new RuntimeException(updateDescriptionException);
    }
    return updateDescription;
  }

  /**
   *  Returns the transaction number
   *
   *  @return the transaction number, or null if not part of a multi-document transaction
   *  @since 3.11
   *  @mongodb.server.release 4.0
   */
  public Long getTxnNumber() {
    if (txnNumberException != null)  {
      throw new RuntimeException(txnNumberException);
    }
    return txnNumber;
  }

  /**
   *  Returns the identifier for the session associated with the transaction
   *
   *  @return the lsid, or null if not part of a multi-document transaction
   *  @since 3.11
   *  @mongodb.server.release 4.0
   */
  public JsonObject getLsid() {
    if (lsidException != null)  {
      throw new RuntimeException(lsidException);
    }
    return lsid;
  }

  /**
   * @return mongo object
   * @hidden
   */
  public static <TDocument> ChangeStreamDocument<TDocument> fromDriverClass(
      com.mongodb.client.model.changestream.ChangeStreamDocument<TDocument> from) {
    requireNonNull(from, "from is null");
    ChangeStreamDocument<TDocument> result = new ChangeStreamDocument<TDocument>();
    try {
      result.resumeToken = ConversionUtilsImpl.INSTANCE.toJsonObject(from.getResumeToken());
    } catch (Exception ex) {
      result.resumeTokenException = ex;
    }
    try {
      result.namespace = from.getNamespace();
    } catch (Exception ex) {
      result.namespaceException = ex;
    }
    try {
      result.namespaceDocument = ConversionUtilsImpl.INSTANCE.toJsonObject(from.getNamespaceDocument());
    } catch (Exception ex) {
      result.namespaceDocumentException = ex;
    }
    try {
      result.destinationNamespace = from.getDestinationNamespace();
    } catch (Exception ex) {
      result.destinationNamespaceException = ex;
    }
    try {
      result.destinationNamespaceDocument = ConversionUtilsImpl.INSTANCE.toJsonObject(from.getDestinationNamespaceDocument());
    } catch (Exception ex) {
      result.destinationNamespaceDocumentException = ex;
    }
    try {
      result.databaseName = from.getDatabaseName();
    } catch (Exception ex) {
      result.databaseNameException = ex;
    }
    try {
      result.fullDocument = from.getFullDocument();
    } catch (Exception ex) {
      result.fullDocumentException = ex;
    }
    try {
      result.documentKey = ConversionUtilsImpl.INSTANCE.toJsonObject(from.getDocumentKey());
    } catch (Exception ex) {
      result.documentKeyException = ex;
    }
    try {
      result.clusterTime = ConversionUtilsImpl.INSTANCE.toLong(from.getClusterTime());
    } catch (Exception ex) {
      result.clusterTimeException = ex;
    }
    try {
      result.operationType = from.getOperationType();
    } catch (Exception ex) {
      result.operationTypeException = ex;
    }
    try {
      result.updateDescription = UpdateDescription.fromDriverClass(from.getUpdateDescription());
    } catch (Exception ex) {
      result.updateDescriptionException = ex;
    }
    try {
      result.txnNumber = ConversionUtilsImpl.INSTANCE.toLong(from.getTxnNumber());
    } catch (Exception ex) {
      result.txnNumberException = ex;
    }
    try {
      result.lsid = ConversionUtilsImpl.INSTANCE.toJsonObject(from.getLsid());
    } catch (Exception ex) {
      result.lsidException = ex;
    }
    return result;
  }
}
