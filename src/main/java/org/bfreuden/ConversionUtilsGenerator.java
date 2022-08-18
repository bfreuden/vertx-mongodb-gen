package org.bfreuden;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConversionUtilsGenerator {
    private Map<Conversion, String> conversions = new HashMap<>();
    private static class Conversion implements Comparable<Conversion> {
        TypeName from;
        TypeName to;

        public Conversion(TypeName from, TypeName to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Conversion that = (Conversion) o;
            return from.equals(that.from) && to.equals(that.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public int compareTo(Conversion o) {
            return from.toString().compareTo(to.toString());
        }
    }

    private static final Pattern LIST_REGEX = Pattern.compile("java\\.util\\.List<(?:\\? (?:extends|super) )((?:[a-z]+\\.)+)([^.]+)>");
    String addConversion(TypeName from, TypeName to) {
        TypeName to2 = to;
        String fqName = to2.toString();
        Matcher matcher = LIST_REGEX.matcher(fqName);
        String type = null;
        if (matcher.matches()) {
            to2 = ParameterizedTypeName.get(ClassName.get(List.class), ClassName.bestGuess(matcher.group(1) + matcher.group(2)));
            type = matcher.group(2);
        }
        Conversion conversion = new Conversion(from, to2);
        String methodName = conversions.get(conversion);
        if (methodName == null) {
            if (to2 instanceof ClassName) {
                methodName = "to" + fqName.substring(fqName.lastIndexOf('.') + 1);
            } else if (fqName.endsWith("[]")) {
                type = fqName.substring(0, fqName.length() - 2);
                type = Character.toUpperCase(type.charAt(0)) + type.substring(1);
                methodName = "to" + type + "Array";
            } else if (matcher.matches()){
                methodName = "to" + type + "List";
            } else  {
                throw new IllegalArgumentException("can't generate method name from " + to2);
            }
            conversions.put(conversion, methodName);
        }
        return methodName;
    }

    void generateSource(File baseDir) throws IOException {
        TypeSpec.Builder conversionUtils = TypeSpec
                .interfaceBuilder("ConversionUtils")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("@hidden");
        List<String> sorted = conversions.values().stream().sorted().collect(Collectors.toList());
        TreeSet<String> methodNames = new TreeSet<>(sorted);
        for (String methodName : methodNames) {
            List<Conversion> conversionsOfName = conversions.entrySet()
                    .stream()
                    .filter(it -> it.getValue().equals(methodName))
                    .map(Map.Entry::getKey)
                    .sorted()
                    .collect(Collectors.toList());
            for (Conversion conversion: conversionsOfName) {
                MethodSpec method = MethodSpec.methodBuilder(methodName)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(conversion.from, "from")
                        .returns(conversion.to)
                        .build();
                conversionUtils.addMethod(method);
            }
        }
        JavaFile javaFile = JavaFile.builder("io.vertx.mongo.impl", conversionUtils.build())
                .addFileComment(Copyright.COPYRIGHT)
                .build();
        javaFile.writeTo(baseDir);

    }

}
