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

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import io.vertx.core.Context;
import java.util.function.BiConsumer;

public class MongoClientSettingsInitializer {
  private BiConsumer<Context, SslSettings.Builder> SslSettingsInitializer;

  private BiConsumer<Context, ConnectionPoolSettings.Builder> ConnectionPoolSettingsInitializer;

  private BiConsumer<Context, SocketSettings.Builder> SocketSettingsInitializer;

  private BiConsumer<Context, ClusterSettings.Builder> ClusterSettingsInitializer;

  private BiConsumer<Context, MongoClientSettings.Builder> MongoClientSettingsInitializer;

  private BiConsumer<Context, AutoEncryptionSettings.Builder> AutoEncryptionSettingsInitializer;

  private BiConsumer<Context, ServerSettings.Builder> ServerSettingsInitializer;

  public MongoClientSettingsInitializer initializeSslSettingsWith(
      BiConsumer<Context, SslSettings.Builder> initializer) {
    this.SslSettingsInitializer = initializer;
    return this;
  }

  public BiConsumer<Context, SslSettings.Builder> getSslSettingsInitializer() {
    return this.SslSettingsInitializer;
  }

  public MongoClientSettingsInitializer initializeConnectionPoolSettingsWith(
      BiConsumer<Context, ConnectionPoolSettings.Builder> initializer) {
    this.ConnectionPoolSettingsInitializer = initializer;
    return this;
  }

  public BiConsumer<Context, ConnectionPoolSettings.Builder> getConnectionPoolSettingsInitializer(
      ) {
    return this.ConnectionPoolSettingsInitializer;
  }

  public MongoClientSettingsInitializer initializeSocketSettingsWith(
      BiConsumer<Context, SocketSettings.Builder> initializer) {
    this.SocketSettingsInitializer = initializer;
    return this;
  }

  public BiConsumer<Context, SocketSettings.Builder> getSocketSettingsInitializer() {
    return this.SocketSettingsInitializer;
  }

  public MongoClientSettingsInitializer initializeClusterSettingsWith(
      BiConsumer<Context, ClusterSettings.Builder> initializer) {
    this.ClusterSettingsInitializer = initializer;
    return this;
  }

  public BiConsumer<Context, ClusterSettings.Builder> getClusterSettingsInitializer() {
    return this.ClusterSettingsInitializer;
  }

  public MongoClientSettingsInitializer initializeMongoClientSettingsWith(
      BiConsumer<Context, MongoClientSettings.Builder> initializer) {
    this.MongoClientSettingsInitializer = initializer;
    return this;
  }

  public BiConsumer<Context, MongoClientSettings.Builder> getMongoClientSettingsInitializer() {
    return this.MongoClientSettingsInitializer;
  }

  public MongoClientSettingsInitializer initializeAutoEncryptionSettingsWith(
      BiConsumer<Context, AutoEncryptionSettings.Builder> initializer) {
    this.AutoEncryptionSettingsInitializer = initializer;
    return this;
  }

  public BiConsumer<Context, AutoEncryptionSettings.Builder> getAutoEncryptionSettingsInitializer(
      ) {
    return this.AutoEncryptionSettingsInitializer;
  }

  public MongoClientSettingsInitializer initializeServerSettingsWith(
      BiConsumer<Context, ServerSettings.Builder> initializer) {
    this.ServerSettingsInitializer = initializer;
    return this;
  }

  public BiConsumer<Context, ServerSettings.Builder> getServerSettingsInitializer() {
    return this.ServerSettingsInitializer;
  }
}
