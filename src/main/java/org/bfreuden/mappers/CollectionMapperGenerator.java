package org.bfreuden.mappers;

import com.squareup.javapoet.ClassName;

import java.util.Collections;
import java.util.Objects;

public class CollectionMapperGenerator extends MapperGenerator {

    private MapperGenerator itemMapper;

    public void setItemMapper(MapperGenerator itemMapper) {
        this.itemMapper = itemMapper;
    }
    @Override
    public CodeBlockSource getExpressionSource(String value) {
        Objects.requireNonNull(itemMapper, "item mapper has not been set");
        CodeBlockSource itemMapperSource = itemMapper.getMapperSource();
        return new CodeBlockSource(
                String.format("$T.mapItems(%s, %s)", value, itemMapperSource.source),
                concat(Collections.singletonList(ClassName.bestGuess("io.vertx.mongo.impl.CollectionsConversionUtils")), itemMapperSource.typeNames)
        );
    }

    @Override
    public CodeBlockSource getMapperSource() {
        Objects.requireNonNull(itemMapper, "item mapper has not been set");
        CodeBlockSource itemMapperSource = itemMapper.getMapperSource();
        return new CodeBlockSource(
                String.format("_list -> $T.mapItems(_list, %s)", itemMapperSource.source),
                concat(Collections.singletonList(ClassName.bestGuess("io.vertx.mongo.impl.CollectionsConversionUtils")), itemMapperSource.typeNames)
        );
    }
}
