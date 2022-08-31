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
                String.format("$T.fromDriverClass(clientContext, %s)", value),
                Collections.singletonList(vertxTypeName)
        );
    }

    @Override
    public CodeBlockSource getMapperSource() {
        return new CodeBlockSource(
                "_item -> $T.fromDriverClass(clientContext, _item)",
                Collections.singletonList(vertxTypeName)
        );
    }
}
