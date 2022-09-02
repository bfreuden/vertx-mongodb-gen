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

import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import io.vertx.mongo.client.ClientConfig;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.function.Function;

public final class MongoClientContext {

    private final VertxInternal vertx;
    private final ContextInternal context;
    private final ClientConfig config;
    private final ConversionUtils conversionUtils;
    public MongoClientContext(VertxInternal vertx, ContextInternal context, CodecRegistry codecRegistry, ClientConfig config) {
        this.vertx = vertx;
        this.context = context;
        this.config = config;
        conversionUtils = new ConversionUtilsImpl(codecRegistry, config.isUseObjectIds(), config.getInputMapper(), config.getOutputMapper());
    }

    public VertxInternal getVertx() {
        return vertx;
    }

    public ContextInternal getContext() {
        return context;
    }
    public ClientConfig getConfig() {
        return config;
    }
    public ConversionUtils getMapper() {
        return conversionUtils;
    }

}
