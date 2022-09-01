package org.bfreuden.mappers;

import java.util.Collections;

public class ToDriverClassMapperGenerator extends MapperGenerator {


    private final boolean needsInputMapper;

    public ToDriverClassMapperGenerator() {
        this.needsInputMapper = false;
    }

    public ToDriverClassMapperGenerator(boolean needsInputMapper) {
        this.needsInputMapper = needsInputMapper;
    }

    @Override
    public CodeBlockSource getExpressionSource(String value) {
        String inputMapperParam = needsInputMapper ? ", inputMapper" : "";
        return new CodeBlockSource(
                String.format((value.indexOf(' ') != -1 ? "(%s)" : "%s") + String.format(".toDriverClass(clientContext%s)", inputMapperParam), value),
                Collections.emptyList()
        );
    }

    @Override
    public CodeBlockSource getMapperSource() {
        String inputMapperParam = needsInputMapper ? ", inputMapper" : "";
        return new CodeBlockSource(
                String.format("_item -> _item.toDriverClass(clientContext%s)", inputMapperParam),
                Collections.emptyList()
        );
    }
}
