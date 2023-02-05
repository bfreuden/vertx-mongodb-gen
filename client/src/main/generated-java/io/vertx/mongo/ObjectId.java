package io.vertx.mongo;

import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.codec.json.JsonObjectCodec;

import java.util.Objects;

public class ObjectId {

    private final String hexString;
    private final JsonObject oid;

    public ObjectId(String hexString) {
        this.hexString = hexString;
        this.oid = null;
    }

    public ObjectId(JsonObject oid) {
        Objects.requireNonNull(oid.getString(JsonObjectCodec.OID_FIELD));
        this.hexString = null;
        this.oid = oid;
    }

    public String getHexString() {
        return hexString;
    }

    public JsonObject getOid() {
        return oid;
    }

}
