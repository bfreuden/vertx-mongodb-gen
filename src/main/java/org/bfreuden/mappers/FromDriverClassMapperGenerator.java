package org.bfreuden.mappers;

import com.squareup.javapoet.TypeName;

import java.util.Collections;

public class FromDriverClassMapperGenerator extends MapperGenerator {

    private final TypeName vertxTypeName;

    public FromDriverClassMapperGenerator(TypeName vertxTypeName) {
        this.vertxTypeName = vertxTypeName;
    }

    @Override
    public CodeBlockSource getExpressionSource(String value) {
        return new CodeBlockSource(
                String.format("$T.fromDriverClass(%s)", value),
                Collections.singletonList(vertxTypeName)
        );
    }

    @Override
    public CodeBlockSource getMapperSource() {
        return new CodeBlockSource(
                "$T::fromDriverClass",
                Collections.singletonList(vertxTypeName)
        );
    }
}
