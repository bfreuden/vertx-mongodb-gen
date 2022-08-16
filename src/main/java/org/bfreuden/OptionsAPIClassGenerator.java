package org.bfreuden;

import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.bson.conversions.Bson;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class OptionsAPIClassGenerator extends APIClassGenerator {


    protected LinkedHashMap<String, Option> options = new LinkedHashMap<>();
    protected static class Option {
        String name;
        String setterParamName;
        TypeName mongoType;
        TypeName vertxType;
        String mongoSetterName;
        String mongoJavadoc;
        String mongoGetterJavadoc;
        boolean optionType = false;
        boolean inCtor = false;
        boolean withTimeUnit = false;
    }
    public OptionsAPIClassGenerator(InspectionContext context, ClassDoc classDoc) {
        super(context, classDoc);
    }
    @Override
    protected void analyzeClass() {
        Type superclassType = classDoc.superclassType();
        if (!superclassType.qualifiedTypeName().equals("java.lang.Object"))
            throw new IllegalStateException("unsupported super class: " + superclassType + " for " + classDoc.qualifiedTypeName());
        ClassDoc[] interfaces = classDoc.interfaces();
        for (ClassDoc inter: interfaces) {
            throw new IllegalStateException("unsupported interface: " + inter + " for " + classDoc.qualifiedTypeName());
        }
        List<MethodDoc> methods = new ArrayList<>(Arrays.stream(classDoc.methods())
                .filter(m -> !m.name().equals("toString"))
                .filter(m -> !m.name().equals("hashCode"))
                .filter(m -> !m.name().equals("equals"))
                .collect(Collectors.toList()));
        String builderFullyQualifiedName = classDoc.qualifiedTypeName() + ".Builder";
        if (context.builderClasses.contains(builderFullyQualifiedName)) {
            List<MethodDoc> builders = methods.stream()
                    .filter(m -> m.isStatic() && m.name().equals("builder") && m.returnType().qualifiedTypeName().equals(builderFullyQualifiedName))
                    .collect(Collectors.toList());
            for (MethodDoc builder : builders) {
                if (builder.parameters().length == 0) {
                    methods.remove(builder);
                    continue;
                }
                if (builder.parameters().length == 1 && builder.parameters()[0].type().qualifiedTypeName().equals(classDoc.qualifiedTypeName())) {
                    methods.remove(builder);
                    continue;
                }
                throw new IllegalStateException("unsupported builder signature for " + classDoc.qualifiedTypeName());
            }
            ClassDoc builderClassDoc = context.classDocs.get(builderFullyQualifiedName);
            Arrays.stream(builderClassDoc.methods())
                    .filter(m -> !m.name().equals("toString"))
                    .filter(m -> !m.name().equals("hashCode"))
                    .filter(m -> !m.name().equals("equals"))
                    .filter(m -> !m.name().equals("build"))
                    .forEach(methods::add);
        } else {
            ConstructorDoc[] constructors = classDoc.constructors();
            if (constructors.length > 1)
                throw new IllegalStateException("unsupported number of constructors for " + classDoc.qualifiedTypeName());

            if (classDoc.typeParameters().length > 0)
                throw new IllegalStateException("unsupported number of type parameters for " + classDoc.qualifiedTypeName());

            for (ConstructorDoc constructor : constructors) {
                Parameter[] parameters = constructor.parameters();
                for (Parameter parameter: parameters) {
                    Type optionType = parameter.type();
                    String optionName = parameter.name();
                    String mongoJavadoc = null;
                    createOption(methods, optionType, optionName, optionName, mongoJavadoc, null, false, parameter.name());
                }

            }
        }
        List<MethodDoc> fluentSetters = methods.stream()
                .filter(m -> m.parameters().length == 1 || m.parameters().length == 2 && m.parameters()[1].type().qualifiedTypeName().equals(TimeUnit.class.getName()))
                .filter(m -> isFluentSetter(classDoc, m))
                .collect(Collectors.toList());
        analyzeSetterOptions(methods, fluentSetters);
        List<MethodDoc> regularSetters = methods.stream()
                .filter(m -> m.parameters().length == 1 || m.parameters().length == 2 && m.parameters()[1].type().qualifiedTypeName().equals(TimeUnit.class.getName()))
                .filter(this::isRegularSetter)
                .collect(Collectors.toList());
        analyzeSetterOptions(methods, regularSetters);
        if (!methods.isEmpty())
            throw new IllegalStateException("unknown method: " + methods);
    }

    protected void analyzeSetterOptions(List<MethodDoc> methods, List<MethodDoc> setters) {
        for (MethodDoc setter: setters) {
            methods.remove(setter);
            Parameter parameter = setter.parameters()[0];
            String setterParamName = parameter.name();
            Type optionType = parameter.type();
            String optionName = setter.name();
            boolean hasTimeUnit = setter.parameters().length == 2;
            if (optionName.startsWith("set") && Character.isUpperCase(optionName.charAt(3)))
                optionName = Character.toLowerCase(optionName.charAt(3)) + optionName.substring(3);
            String mongoJavadoc = setter.getRawCommentText();
            String mongoSetterName = setter.name();
            createOption(methods, optionType, optionName, setterParamName, mongoJavadoc, mongoSetterName, hasTimeUnit, null);
        }
    }

    protected void createOption(List<MethodDoc> methods, Type optionType, String optionName, String setterParamName, String mongoJavadoc, String mongoSetterName, boolean withTimeUnit, String mongoCtorParamName) {
        if (options.containsKey(optionName))
            throw new IllegalStateException("option already exists: " + optionName + " for " + classDoc.qualifiedTypeName());
        Option option = new Option();
        option.name = optionName;
        option.withTimeUnit = withTimeUnit;
        options.put(option.name, option);
        option.mongoJavadoc = mongoJavadoc;
        option.mongoSetterName = mongoSetterName;
        option.setterParamName = setterParamName;
        if (mongoCtorParamName != null)
            option.inCtor = true;
        if (optionType.asParameterizedType() != null) {
            if (optionType.asParameterizedType().toString().equals("java.util.List<? extends org.bson.conversions.Bson>")) {
                option.mongoType = ParameterizedTypeName.get(ClassName.bestGuess(List.class.getName()), WildcardTypeName.subtypeOf(Bson.class));
                option.vertxType = ParameterizedTypeName.get(ClassName.bestGuess(List.class.getName()), ClassName.get(JsonObject.class));
            } else if (optionType.asParameterizedType().toString().equals("java.util.List<java.lang.String>")) {
                option.mongoType = ParameterizedTypeName.get(ClassName.bestGuess(List.class.getName()), ClassName.get(String.class));
                option.vertxType = ParameterizedTypeName.get(ClassName.bestGuess(List.class.getName()), ClassName.get(String.class));
            } else {
                throw new IllegalStateException("unsupported parametrized option: " + optionType.asParameterizedType() + " for " + classDoc.qualifiedTypeName());
            }
        }
        String qualifiedTypeName = optionType.qualifiedTypeName();
        if (optionType.isPrimitive()) {
            String typeName = optionType.typeName();
            switch (typeName) {
                case "boolean":
                    option.mongoType = TypeName.BOOLEAN;
                    break;
                case "int":
                    option.mongoType = TypeName.INT;
                    break;
                case "long":
                    option.mongoType = TypeName.LONG;
                    break;
                default:
                    throw new IllegalStateException("unsupported primitive option type: " + optionType.asParameterizedType() + " for " + classDoc.qualifiedTypeName());
            }
            option.vertxType = option.mongoType.box();
        } else if (Types.isKnown(qualifiedTypeName)) {
            option.mongoType = ClassName.bestGuess(qualifiedTypeName);
            option.vertxType = Types.getMapped(qualifiedTypeName);
        } else if (context.optionsApiClasses.contains(qualifiedTypeName)) {
            option.optionType = true;
            option.mongoType = ClassName.bestGuess(qualifiedTypeName);
            option.vertxType = ClassName.bestGuess(mapPackageName(qualifiedTypeName));
        } else if (context.enumApiClasses.contains(qualifiedTypeName)) {
            option.optionType = true;
            option.mongoType = ClassName.bestGuess(qualifiedTypeName);
            option.vertxType = ClassName.bestGuess(qualifiedTypeName);
        } else {
            String builderFullyQualifiedName = qualifiedTypeName + ".Builder";
            if (context.builderClasses.contains(builderFullyQualifiedName)) {
                option.mongoType = ClassName.bestGuess(qualifiedTypeName);
                option.vertxType = ClassName.bestGuess(mapPackageName(qualifiedTypeName));
            } else {
                if (!qualifiedTypeName.equals("com.mongodb.CreateIndexCommitQuorum"))
                    throw new IllegalStateException("unsupported parameter type: " + qualifiedTypeName + " for " + classDoc.qualifiedTypeName());
            }
        }
        Optional<MethodDoc> maybeGetter = methods.stream()
                .filter(m -> m.parameters().length == 0 || option.withTimeUnit && m.parameters().length == 1 && m.parameters()[0].type().qualifiedTypeName().equals(TimeUnit.class.getName()))
                .filter(m -> m.returnType().equals(optionType) || m.returnType().qualifiedTypeName().equals("java.lang.Boolean") && optionType.qualifiedTypeName().equals("boolean")|| option.name.equals("arrayFilters") && m.name().equals("getArrayFilters")|| option.name.equals("keyAltNames") && m.name().equals("getKeyAltNames"))
                .filter(m -> m.name().equals("get" + option.name.toUpperCase().charAt(0) + option.name.substring(1)) || m.name().equals("is" + option.name.toUpperCase().charAt(0) + option.name.substring(1)))
                .findFirst();
        if (maybeGetter.isPresent()) {
            MethodDoc getter = maybeGetter.get();
            methods.remove(getter);
            option.mongoGetterJavadoc = getter.getRawCommentText();
        }
    }

    protected boolean isFluentSetter(ClassDoc classDoc, MethodDoc m) {
        return m.returnType().qualifiedTypeName().equals(classDoc.qualifiedTypeName()) || m.returnType().qualifiedTypeName().equals(classDoc.qualifiedTypeName() + ".Builder");
    }
    protected boolean isRegularSetter(MethodDoc m) {
        return m.name().startsWith("set") && Character.isUpperCase(m.name().charAt(3));
    }

    @Override
    protected List<JavaFile> getJavaFiles() {
        TypeSpec.Builder type = TypeSpec.classBuilder(getTargetClassName())
                .addModifiers(Modifier.PUBLIC);
        String rawCommentText = classDoc.getRawCommentText();
        if (rawCommentText != null) {
            String[] split = rawCommentText.split("\n");
            StringJoiner joiner = new StringJoiner("\n");
            boolean first = true;
            for (String line : split) {
                if (first) {
                    line = line.replaceAll("([Pp]ublisher|[Ii]terable)( interface)?", "Options");
                    first = false;
                }
                if (!line.contains("@param")) {
                    joiner.add(line);
                }
            }
            type.addJavadoc(joiner.toString().replace("$", "&#x24;"));
        }
        type.addAnnotation(AnnotationSpec.builder(DataObject.class).addMember("generateConverter", CodeBlock.of("true")).build());
        for (Option option: options.values()) {
            if (option.name.equals("commitQuorum"))
                continue;
            FieldSpec.Builder fieldBuilder = FieldSpec.builder(option.vertxType, option.name).addModifiers(Modifier.PRIVATE);
            if (option.mongoJavadoc != null) {
                option.mongoJavadoc = option.mongoJavadoc.replace("hint(Bson)", "hint(JsonObject)");
                Optional<String> firstParam = Arrays.stream(option.mongoJavadoc.split("\n+")).filter(it -> it.contains("@param")).findFirst();
                if (firstParam.isPresent()) {
                    String paramLine = firstParam.get();
                    paramLine = paramLine.substring(paramLine.indexOf("@param") + 6).trim();
                    paramLine = paramLine.substring(paramLine.indexOf(' ') + 1);
                    fieldBuilder.addJavadoc(CodeBlock.of(paramLine));
                }
            }
            type.addField(fieldBuilder.build());
            MethodSpec.Builder setterBuilder = MethodSpec.methodBuilder(option.name)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(option.vertxType, option.setterParamName)
                    .returns(ClassName.bestGuess(getTargetPackage() + "." + getTargetClassName()))
                    .addStatement("return this");
            if (option.mongoJavadoc != null) {
                if (option.withTimeUnit) {
                    String[] split = option.mongoJavadoc.split("\n");
                    boolean paramFound = false;
                    StringJoiner joiner = new StringJoiner("\n");
                    for (String line : split) {
                        if (!paramFound) {
                            paramFound = line.contains("@param");
                            if (paramFound)
                                line += " (in milliseconds)";
                            joiner.add(line);
                        } else if (!line.contains("@param")) {
                            joiner.add(line);
                        }
                    }
                    setterBuilder.addJavadoc(joiner.toString().replace("$", "&#x24;"));
                } else {
                    setterBuilder.addJavadoc(option.mongoJavadoc.replace("$", "&#x24;"));
                }
            }
            type.addMethod(setterBuilder.build());
             boolean isBoolean = option.vertxType.toString().toLowerCase().contains("boolean");
            MethodSpec.Builder getterBuilder = MethodSpec.methodBuilder((isBoolean ? "is" : "get") + Character.toUpperCase(option.name.charAt(0)) + option.name.substring(1))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(option.vertxType)
                    .addStatement("return " + option.name);
            if (option.mongoGetterJavadoc != null) {
                if (option.withTimeUnit) {
                    String[] split = option.mongoGetterJavadoc.split("\n");
                    StringJoiner joiner = new StringJoiner("\n");
                    for (String line : split) {
                        if (line.contains("@param"))
                            continue;
                        if (option.withTimeUnit && line.contains("@return") && line.contains("in the given time unit"))
                            line = line.replace("in the given time unit", "(in milliseconds)");
                        joiner.add(line);
                    }
                    getterBuilder.addJavadoc(joiner.toString().replace("$", "&#x24;"));
                } else {
                    getterBuilder.addJavadoc(option.mongoGetterJavadoc.replace("$", "&#x24;"));
                }
            }
            type.addMethod(getterBuilder.build());
        }
        return Collections.singletonList(JavaFile.builder(getTargetPackage(), type.build()).build());
    }
}
