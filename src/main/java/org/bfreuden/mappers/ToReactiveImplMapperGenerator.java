package org.bfreuden.mappers;

import com.squareup.javapoet.TypeName;

import java.util.Collections;

public class ToReactiveImplMapperGenerator extends MapperGenerator {

    private final TypeName implTypeName;
    private final boolean isMongoCollection;

    public ToReactiveImplMapperGenerator(TypeName implTypeName) {
        this.implTypeName = implTypeName;
        this.isMongoCollection = implTypeName.toString().contains("MongoCollectionImpl");
    }

    @Override
    public CodeBlockSource getExpressionSource(String value) {
        String additionalParams = isMongoCollection ? ", clazz)" : ")";
        return new CodeBlockSource(
                String.format("new $T%s(clientContext, %s" + additionalParams, genericBrackets, value),
                Collections.singletonList(implTypeName)
        );
    }

    @Override
    public CodeBlockSource getMapperSource() {
        String additionalParams = isMongoCollection ? ", clazz)" : ")";
        return new CodeBlockSource(
                String.format("_reactive -> new $T%s(clientContext, _reactive" + additionalParams, genericBrackets),
                Collections.singletonList(implTypeName)
        );
    }

}
