package org.bfreuden.mappers;

import com.mongodb.client.model.Filters;
import com.squareup.javapoet.ClassName;

import java.util.Collections;

public class DocumentIdFilterGenerator extends MapperGenerator {

    @Override
    public CodeBlockSource getExpressionSource(String value) {
        return new CodeBlockSource(
                "$T.eq(idProvider.apply(replacement))",
                Collections.singletonList(ClassName.get(Filters.class))
        );
    }

    @Override
    public CodeBlockSource getMapperSource() {
        return new CodeBlockSource(
                "_item -> $T.eq(idProvider.apply(replacement))",
                Collections.singletonList(ClassName.get(Filters.class))
        );
    }
}
