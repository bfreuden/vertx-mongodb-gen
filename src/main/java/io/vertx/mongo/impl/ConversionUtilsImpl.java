package io.vertx.mongo.impl;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.codec.json.JsonObjectCodec;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class ConversionUtilsImpl implements ConversionUtils {

    private final CodecRegistry codecRegistry;
    private final Function<JsonObject, JsonObject> inputMapper;
    private final Function<JsonObject, JsonObject> outputMapper;
    //TODO is it safe to share?
    private final DecoderContext decoderContext = DecoderContext.builder().build();
    private final Codec<JsonObject> jsonObjectCodec;
    private final Codec<Document> documentCodec;
    private final boolean useObjectIds;

    public ConversionUtilsImpl(CodecRegistry codecRegistry, boolean useObjectIds, Function<JsonObject, JsonObject> inputMapper, Function<JsonObject, JsonObject> outputMapper) {
        this.codecRegistry = codecRegistry;
        this.inputMapper = inputMapper;
        this.outputMapper = outputMapper;
        this.jsonObjectCodec = codecRegistry.get(JsonObject.class);
        this.useObjectIds = useObjectIds;
        // FIXME
        this.documentCodec = null;//codecRegistry.get(Document.class);
    }

    @Override
    public Bson toBson(JsonObject from) {
        //TODO check it is working
        from = inputMapper == null ? from : inputMapper.apply(from);
        return new JsonObjectBsonAdapter(from);
    }

    @Override
    public BsonBinary toBsonBinary(byte[] from) {
        return new BsonBinary(BsonBinarySubType.BINARY, from);
    }

    @Override
    public BsonDocument toBsonDocument(JsonObject from) {
        //TODO check it is working
        Bson bson = toBson(from);
        return bson.toBsonDocument(BsonDocument.class, codecRegistry);
    }

    @Override
    public BsonInt64 toBsonInt64(Long from) {
        return new BsonInt64(from);
    }

    @Override
    public List<Bson> toBsonList(JsonArray from) {
        ArrayList<Bson> result = new ArrayList<>();
        for (int i=0 ; i<from.size() ; i++) {
            JsonObject jsonObject = from.getJsonObject(i);
            result.add(toBson(jsonObject));
        }
        return result;
    }

    @Override
    public BsonTimestamp toBsonTimestamp(Long from) {
        return new BsonTimestamp(from);
    }

    @Override
    public BsonValue toBsonValue(Object from) {
        return toBsonValueInternal(from);
    }

    public static BsonValue toBsonValueInternal(Object from) {
        if (from instanceof String)
            return new BsonString((String) from);
        else if (from instanceof JsonObject) {
            JsonObject json = (JsonObject) from;
            String hexString = json.getString("$oid");
            if (json.size() == 1 && hexString != null)
                return new BsonObjectId(new ObjectId(hexString));
        }
        //FIXME
        throw new IllegalStateException("not implemented");
    }

    @Override
    public Buffer toBuffer(ByteBuffer from) {
        return Buffer.buffer(copiedBuffer(from));
    }

    @Override
    public byte[] toByteArray(BsonBinary from) {
        return from.getData();
    }

    @Override
    public ByteBuffer toByteBuffer(Buffer from) {
        byte[] bytes = from.getBytes();
        return ByteBuffer.wrap(bytes);
    }

    @Override
    public Document toDocument(JsonObject from) {
        //TODO check it is working
        BsonDocument bson = toBsonDocument(from);
        return documentCodec.decode(bson.asBsonReader(), decoderContext);
    }

    @Override
    public JsonObject toJsonObject(Bson from) {
        //TODO check it is working
        BsonDocument bsonDocument = from.toBsonDocument(JsonObject.class, codecRegistry);
        return toJsonObject(bsonDocument);
    }

    @Override
    public JsonObject toJsonObject(Document from) {
        //TODO check it is working
        BsonDocument bsonDocument = from.toBsonDocument(JsonObject.class, codecRegistry);
        return toJsonObject(bsonDocument);
    }

    @Override
    public JsonObject toJsonObject(BsonDocument from) {
        //TODO check it is working
        JsonObject to = jsonObjectCodec.decode(new BsonDocumentReader(from), decoderContext);
        if (outputMapper != null)
            to = outputMapper.apply(to);
        return to;
    }

    @Override
    public Long toLong(BsonTimestamp from) {
        return from.getValue();
    }

    @Override
    public Long toLong(BsonInt64 from) {
        return from.getValue();
    }

    @Override
    public Object toObject(BsonValue from) {
        if (from == null)
            return null;
        if (from instanceof BsonObjectId) {
            String hexString = ((BsonObjectId) from).getValue().toHexString();
            return useObjectIds ? new JsonObject().put(JsonObjectCodec.OID_FIELD, hexString) : hexString;
        } else if (from instanceof BsonString) {
            return ((BsonString)from).getValue();
        } else if (from instanceof BsonInt64) {
            return ((BsonInt64)from).getValue();
        } else if (from instanceof BsonInt32) {
            return ((BsonInt32)from).getValue();
        }
        //FIXME continue
        throw new IllegalStateException("not implemented: " + from.getClass());
    }

    @Override
    public ObjectId toObjectId(io.vertx.mongo.ObjectId from) {
        return useObjectIds ? new ObjectId(from.getOid().getString(JsonObjectCodec.OID_FIELD)) : new ObjectId(from.getHexString());
    }

    @Override
    public io.vertx.mongo.ObjectId toObjectId(ObjectId from) {
        return getVertxObjectId(from.toHexString());
    }

    private io.vertx.mongo.ObjectId getVertxObjectId(String hexString) {
        if (useObjectIds)
            return new io.vertx.mongo.ObjectId(new JsonObject().put(JsonObjectCodec.OID_FIELD, hexString));
        else
            return new io.vertx.mongo.ObjectId(hexString);
    }

}
