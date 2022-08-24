package org.bfreuden.mappers;

import com.squareup.javapoet.ClassName;

import java.util.Collections;

public class ConversionUtilsMapperGenerator extends MapperGenerator {

    private final String conversionMethod;

    public ConversionUtilsMapperGenerator(String conversionMethod) {
        this.conversionMethod = conversionMethod;
    }

    @Override
    public CodeBlockSource getExpressionSource(String value) {
        return new CodeBlockSource(
            String.format("$T.INSTANCE.%s(%s)", conversionMethod, value),
            Collections.singletonList(ClassName.bestGuess("io.vertx.mongo.impl.ConversionUtilsImpl"))
        );
    }

    @Override
    public CodeBlockSource getMapperSource() {
        return new CodeBlockSource(
            String.format("$T.INSTANCE::%s", conversionMethod),
            Collections.singletonList(ClassName.bestGuess("io.vertx.mongo.impl.ConversionUtilsImpl"))
        );
    }
}
