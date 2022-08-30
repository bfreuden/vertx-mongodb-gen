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
import java.util.function.Consumer;

public abstract interface MongoClientSettingsInitializer {
  void initializeWithConnectionPoolSettings(
      Consumer<ConnectionPoolSettings.Builder> builderInitializer);

  void initializeWithSocketSettings(Consumer<SocketSettings.Builder> builderInitializer);

  void initializeWithMongoClientSettings(Consumer<MongoClientSettings.Builder> builderInitializer);

  void initializeWithClusterSettings(Consumer<ClusterSettings.Builder> builderInitializer);

  void initializeWithAutoEncryptionSettings(
      Consumer<AutoEncryptionSettings.Builder> builderInitializer);

  void initializeWithServerSettings(Consumer<ServerSettings.Builder> builderInitializer);

  void initializeWithSslSettings(Consumer<SslSettings.Builder> builderInitializer);
}
