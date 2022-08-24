package org.bfreuden.mappers;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MapperGenerator {

    protected String genericBrackets = "";

    public void setGeneric(boolean generic) {
        genericBrackets = generic ? "<>" : "";
    }

    public static class CodeBlockSource {
        final String source;
        final List<TypeName> typeNames;

        public CodeBlockSource(String source, List<TypeName> typeNames) {
            this.source = source;
            this.typeNames = Collections.unmodifiableList(typeNames);
        }
    }

    protected abstract CodeBlockSource getExpressionSource(String value);

    protected abstract  CodeBlockSource getMapperSource();

    public CodeBlock asStatementFromExpression(String expression, String value, TypeName... types) {
        CodeBlockSource source = getExpressionSource(value);
        String code = String.format(expression, source.source);
        return getCodeBlock(code, source.typeNames, types);
    }

    public CodeBlock asStatementFromLambdaOrMethodRef(String expression, TypeName... types) {
        CodeBlockSource source = getMapperSource();
        return getCodeBlock(String.format(expression, source.source), source.typeNames, types);
    }

    protected CodeBlock getCodeBlock(String code, List<TypeName> mappingTypes, TypeName... statementTypes) {
        int nulls = 0;
        List<TypeName> typeNames;
        if (statementTypes.length == 0) {
            typeNames = mappingTypes;
        } else {
            typeNames = new ArrayList<>();
            for (TypeName type : statementTypes) {
                if (type == null) {
                    nulls++;
                    typeNames.addAll(mappingTypes);
                } else {
                    typeNames.add(type);
                }
            }
            if (!mappingTypes.isEmpty() && nulls != 1)
                throw new IllegalArgumentException("expected one null vararg, got " + nulls);
        }
        return CodeBlock.of(code, (Object[]) typeNames.toArray(new TypeName[0]));
    }

    protected List<TypeName> concat(List<TypeName>... lists) {
        if (lists.length == 0)
            return Collections.emptyList();
        else if (lists.length == 1)
            return lists[0];
        else {
            ArrayList<TypeName> result = new ArrayList<>();
            for (List<TypeName> list : lists)
                result.addAll(list);
            return result;
        }
    }

}
