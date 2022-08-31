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
            String.format("clientContext.getConversionUtils().%s(%s)", conversionMethod, value),
            Collections.emptyList()
        );
    }

    @Override
    public CodeBlockSource getMapperSource() {
        return new CodeBlockSource(
            String.format("clientContext.getConversionUtils()::%s", conversionMethod),
                Collections.emptyList()
        );
    }
}
