package org.bfreuden.mappers;

import java.util.Collections;

public class ToDriverClassMapperGenerator extends MapperGenerator {

    @Override
    public CodeBlockSource getExpressionSource(String value) {
        return new CodeBlockSource(
                String.format(value.indexOf(' ') != -1 ? "(%s).toDriverClass(clientContext)" : "%s.toDriverClass(clientContext)", value),
                Collections.emptyList()
        );
    }

    @Override
    public CodeBlockSource getMapperSource() {
        return new CodeBlockSource(
                "_item -> _item.toDriverClass(clientContext)",
                Collections.emptyList()
        );
    }
}
