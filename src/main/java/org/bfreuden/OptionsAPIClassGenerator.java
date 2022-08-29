package org.bfreuden;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.squareup.javapoet.*;
import com.sun.javadoc.*;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class OptionsAPIClassGenerator extends GenericAPIClassGenerator {

    private final boolean clientSettingsOption;

    public OptionsAPIClassGenerator(InspectionContext context, ClassDoc classDoc, boolean clientSettingsOption) {
        super(context, classDoc);
        this.clientSettingsOption = clientSettingsOption;
        this.generatePackageInfo = true;
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
                .filter(m -> !m.name().equals("toBsonDocument"))
                .filter(m -> !m.name().equals("asDocument"))
                .collect(Collectors.toList()));
        String builderFullyQualifiedName = classDoc.qualifiedTypeName() + ".Builder";
        if (context.builderClasses.contains(builderFullyQualifiedName)) {
            this.hasBuilder = true;
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
                    AnnotationDesc[] annotations = parameter.annotations();
                    boolean deprecated = Arrays.stream(annotations).anyMatch(it -> it.annotationType().qualifiedTypeName().equals(Deprecated.class.getName()));
                    createOption(methods, constructor, optionType, optionName, optionName, mongoJavadoc, null, false, parameter.name(), deprecated, false);
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
        if (!methods.isEmpty() && !clientSettingsOption)
            throw new IllegalStateException("unknown method: " + methods);
    }

    protected void analyzeSetterOptions(List<MethodDoc> methods, List<MethodDoc> setters) {
        for (MethodDoc setter: setters) {
            try {
                methods.remove(setter);
                Parameter parameter = setter.parameters()[0];
                String setterParamName = parameter.name();
                Type optionType = parameter.type();
                // HACK for client settings options builder
                if (optionType.qualifiedTypeName().contains(MongoClientSettings.class.getSimpleName()))
                    throw new IllegalStateException("@unsupported class@ " + optionType.qualifiedTypeName());
                // HACK for client settings options builder
                if (classDoc.qualifiedTypeName().contains(AutoEncryptionSettings.class.getSimpleName())) {
                    ParameterizedType parameterizedType = optionType.asParameterizedType();
                    // can't use vertx generators for Object?
                    if (parameterizedType != null && parameterizedType.toString().matches(".*\\bObject\\b.*"))
                        throw new IllegalStateException("@unsupported class@ " + optionType.qualifiedTypeName());
                };
                String optionName = setter.name();
                boolean isBlock = false;
                // HACK for client settings options builder
                if (setter.name().startsWith("applyTo") && setter.parameters().length == 1 && setter.parameters()[0].type().qualifiedTypeName().equals(Block.class.getName())) {
                    ParameterizedType parameterizedType = setter.parameters()[0].type().asParameterizedType();

                    optionType = parameterizedType.typeArguments()[0];
                    String qname = optionType.qualifiedTypeName();
                    if (qname.endsWith(".Builder"))
                        optionType = context.classDocs.get(qname.substring(0, qname.length() - ".Builder".length()));
                    optionName = optionName.substring("applyTo".length());
                    optionName = Character.toLowerCase(optionName.charAt(0)) + optionName.substring(1);
                    isBlock = true;
                }
                boolean hasTimeUnit = setter.parameters().length == 2;
                // HACK
                if (classDoc.name().equals("Collation") && optionName.startsWith("collation")) {
                    optionName = optionName.substring("collation".length());
                    optionName = Character.toLowerCase(optionName.charAt(0)) + optionName.substring(1);

                }
                if (optionName.startsWith("set") && Character.isUpperCase(optionName.charAt(3)))
                    optionName = Character.toLowerCase(optionName.charAt(3)) + optionName.substring(3);
                String mongoJavadoc = setter.getRawCommentText();
                String mongoSetterName = setter.name();
                AnnotationDesc[] annotations = setter.annotations();
                boolean deprecated = Arrays.stream(annotations).anyMatch(it -> it.annotationType().qualifiedTypeName().equals(Deprecated.class.getName()));
                createOption(methods, setter, optionType, optionName, setterParamName, mongoJavadoc, mongoSetterName, hasTimeUnit, null, deprecated, isBlock);
            } catch (RuntimeException ex) {
                if (!ex.getMessage().startsWith("@unsupported class@") || !clientSettingsOption)
                    throw ex;
            }

        }
    }

    protected void createOption(List<MethodDoc> methods, ExecutableMemberDoc methodDoc, Type optionType, String optionName, String setterParamName, String mongoJavadoc, String mongoSetterName, boolean withTimeUnit, String mongoCtorParamName, boolean deprecated, boolean isBlock) {
        if (optionsByName.containsKey(optionName))
            throw new IllegalStateException("option already exists: " + optionName + " for " + classDoc.qualifiedTypeName());
        Option option = new Option();
        option.isBlock = isBlock;
        option.name = optionName;
        option.deprecated = deprecated;
        option.withTimeUnit = withTimeUnit;
        option.mongoJavadoc = mongoJavadoc;
        option.mongoSetterName = mongoSetterName;
        option.setterParamName = setterParamName;
        option.type = getActualType(methodDoc, optionName, optionType, TypeLocation.PARAMETER);
        if (option.type.vertxType.isPrimitive())
            option.type.vertxType = option.type.vertxType.box();
        if (mongoCtorParamName != null)
            option.mandatory = true;
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
        optionsByName.put(option.name, option);
    }

    protected boolean isFluentSetter(ClassDoc classDoc, MethodDoc m) {
        return m.returnType().qualifiedTypeName().equals(classDoc.qualifiedTypeName()) || m.returnType().qualifiedTypeName().equals(classDoc.qualifiedTypeName() + ".Builder");
    }
    protected boolean isRegularSetter(MethodDoc m) {
        return m.name().startsWith("set") && Character.isUpperCase(m.name().charAt(3));
    }

    @Override
    protected List<JavaFile.Builder> getJavaFiles() {
        if (optionsByName.isEmpty())
            return Collections.emptyList();
        if (clientSettingsOption)
            context.actualClientSettingsBuilders.add(classDoc);
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
            type.addJavadoc(joiner.toString().replace("$", "$$"));
        }
        inflateOptionType(type);
        return Collections.singletonList(JavaFile.builder(getTargetPackage(), type.build()));
    }

}
