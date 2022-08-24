package org.bfreuden.mappers;

import com.squareup.javapoet.TypeName;

import java.util.Collections;

public class ToReactiveImplMapperGenerator extends MapperGenerator {

    private final TypeName implTypeName;

    public ToReactiveImplMapperGenerator(TypeName implTypeName) {
        this.implTypeName = implTypeName;
    }

    @Override
    public CodeBlockSource getExpressionSource(String value) {
        return new CodeBlockSource(
                String.format("new $T%s(clientContext, %s)", genericBrackets, value),
                Collections.singletonList(implTypeName)
        );
    }

    @Override
    public CodeBlockSource getMapperSource() {
        return new CodeBlockSource(
                String.format("_reactive -> new $T%s(clientContext, _reactive)", genericBrackets),
                Collections.singletonList(implTypeName)
        );
    }

}
