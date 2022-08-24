package org.bfreuden.mappers;

import com.squareup.javapoet.ClassName;

import java.util.Collections;
import java.util.Objects;

public class MapMapperGenerator extends MapperGenerator {

    private MapperGenerator keyMapper;
    private MapperGenerator valueMapper;

    public void setKeyMapper(MapperGenerator keyMapper) {
        this.keyMapper = keyMapper;
    }

    public void setValueMapper(MapperGenerator valueMapper) {
        this.valueMapper = valueMapper;
    }
    @Override
    public CodeBlockSource getExpressionSource(String value) {
        if (keyMapper == null && valueMapper == null)
            throw new IllegalStateException("key and/or value mapper have/has not been set");
        if (keyMapper != null) {
            CodeBlockSource keyMapperSource = keyMapper.getMapperSource();
            if (valueMapper != null) {
                CodeBlockSource valueMapperSource = keyMapper.getMapperSource();
                return new CodeBlockSource(
                        String.format("$T.mapEntries(%s, %s, %s)", value, keyMapperSource.source, valueMapperSource.source),
                        concat(Collections.singletonList(ClassName.bestGuess("io.vertx.mongo.impl.CollectionsConversionUtils")), keyMapperSource.typeNames, valueMapperSource.typeNames)
                );
            } else {
                return new CodeBlockSource(
                        String.format("$T.mapKeys(%s, %s)", value, keyMapperSource.source),
                        concat(Collections.singletonList(ClassName.bestGuess("io.vertx.mongo.impl.CollectionsConversionUtils")), keyMapperSource.typeNames)
                );
            }
        } else {
            CodeBlockSource valueMapperSource = valueMapper.getMapperSource();
            return new CodeBlockSource(
                    String.format("$T.mapValues(%s, %s)", value, valueMapperSource.source),
                    concat(Collections.singletonList(ClassName.bestGuess("io.vertx.mongo.impl.CollectionsConversionUtils")), valueMapperSource.typeNames)
            );
        }
    }

    @Override
    public CodeBlockSource getMapperSource() {
        Objects.requireNonNull(keyMapper, "item mapper has not been set");
        CodeBlockSource itemMapperSource = keyMapper.getMapperSource();
        return new CodeBlockSource(
                String.format("_list -> $T.mapItems(_list, %s)", itemMapperSource),
                concat(Collections.singletonList(ClassName.bestGuess("io.vertx.mongo.impl.CollectionsConversionUtils")), itemMapperSource.typeNames)
        );
    }
}
