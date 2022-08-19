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
package io.vertx.mongo.impl;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.mongo.client.ClientConfig;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

public class MongoClientContext {

    private final Vertx vertx;
    private final Context context;
    private final ClientConfig config;

    public MongoClientContext(Vertx vertx, Context context, ClientConfig config) {
        this.vertx = vertx;
        this.context = context;
        this.config = config;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public Context getContext() {
        return context;
    }
    public ClientConfig getConfig() {
        return config;
    }
}
