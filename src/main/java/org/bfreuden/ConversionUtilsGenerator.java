package org.bfreuden;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConversionUtilsGenerator {
    private Map<Conversion, String> conversions = new HashMap<>();
    private static class Conversion {
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
    }

    private static final Pattern LIST_REGEX = Pattern.compile("java\\.util\\.List<(?:\\? extends )(?:([a-z]+\\.)+)([^.]+)>");
    String addConversion(TypeName from, TypeName to) {
        Conversion conversion = new Conversion(from, to);
        String methodName = conversions.get(conversion);
        if (methodName == null) {
            String fqName = to.toString();
            Matcher matcher = LIST_REGEX.matcher(fqName);
            if (to instanceof ClassName) {
                methodName = "to" + fqName.substring(fqName.lastIndexOf('.') + 1);
            } else if (fqName.endsWith("[]")) {
                String type = fqName.substring(0, fqName.length() - 2);
                type = Character.toUpperCase(type.charAt(0)) + type.substring(1);
                methodName = "to" + type + "Array";
            } else if (matcher.matches()){
                String type = matcher.group(1);
                type = Character.toUpperCase(type.charAt(0)) + type.substring(1);
                methodName = "to" + type + "List";
            } else  {
                throw new IllegalArgumentException("can't generate method name from" + to);
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
        for (String methodName : sorted) {
            Map.Entry<Conversion, String> conversionStringEntry = conversions.entrySet().stream().filter(it -> it.getValue().equals(methodName)).findFirst().get();
            MethodSpec method = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(conversionStringEntry.getKey().from, "from")
                    .returns(conversionStringEntry.getKey().to)
                    .build();
            conversionUtils.addMethod(method);
        }
        JavaFile javaFile = JavaFile.builder("io.vertx.mongo.impl", conversionUtils.build()).build();
        javaFile.writeTo(baseDir);

    }

}
