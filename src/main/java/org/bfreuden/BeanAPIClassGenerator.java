package org.bfreuden;

import com.squareup.javapoet.*;
import com.sun.javadoc.*;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BeanAPIClassGenerator extends GenericAPIClassGenerator {

    private boolean isAbstract;

    public BeanAPIClassGenerator(InspectionContext context, ClassDoc classDoc, boolean resultBean) {
        super(context, classDoc);
        this.isResultOnlyOptions = true; // don't write toMongoMethod or setters
        this.resultBean = resultBean;
    }

    @Override
    protected void analyzeClass() {
        ConstructorDoc[] ctors = classDoc.constructors();
        isAbstract = classDoc.isAbstract();
        if (!isAbstract && (ctors == null || ctors.length == 0))
            throw new IllegalStateException("bean class must have constructors " + classDoc);

        TypeVariable[] typeVariables1 = classDoc.typeParameters();
        for (TypeVariable variable : typeVariables1)
            this.typeVariables.add(TypeVariableName.get(variable.toString()));

        List<MethodDoc> methodDocs = Arrays.stream(classDoc.methods())
                .filter(it -> !it.name().equals("toString") && !it.name().equals("hashCode") && !it.name().equals("equals"))
                .filter(it -> !it.isStatic())
                .collect(Collectors.toList());
        for (MethodDoc methodDoc: methodDocs) {
            Matcher matcher = GETTER_NAME.matcher(methodDoc.name());
            if (matcher.matches()) {
                MongoMethod mongoMethod = analyzeMethod(methodDoc);
                Option option = new Option();
                option.name = matcher.group(1).toLowerCase() + matcher.group(2);
                option.type = getActualType(methodDoc, option.name, methodDoc.returnType(), resultBean ? TypeLocation.RETURN : TypeLocation.PARAMETER);
                option.deprecated = Arrays.stream(methodDoc.annotations()).anyMatch(it -> it.annotationType().toString().equals(Deprecated.class.getName()));
                option.mongoGetterJavadoc = methodDoc.getRawCommentText().replace("$", "$$");
                option.mongoGetterName = methodDoc.name();
                optionsByName.put(option.name, option);
                methods.add(mongoMethod);
            } else {
                throw new IllegalStateException("unknown bean method: " + methodDoc);
            }
        }

        for (ConstructorDoc ctor: ctors) {
            MongoMethod mongoCtor = analyzeConstructor(ctor);
            constructors.add(mongoCtor);
            for (MongoMethodParameter param : mongoCtor.params) {
                getOptionOfCtorParam(param, ctor);
            }
        }

        Type type = classDoc.superclassType();
        if (type != null) {
            String superQualifiedType = type.qualifiedTypeName();
            if (context.modelApiClasses.contains(superQualifiedType) || context.resultApiClasses.contains(superQualifiedType)) {
                superClass = ClassName.bestGuess(mapPackageName(superQualifiedType));
            }
        }

    }


    @Override
    protected List<JavaFile.Builder> getJavaFiles() {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(getTargetClassName());
        typeBuilder.addModifiers(Modifier.PUBLIC);

//        if (isAbstract)
//            typeBuilder.addModifiers(Modifier.ABSTRACT);

        for (TypeVariableName typeVariable : typeVariables)
            typeBuilder.addTypeVariable(typeVariable);

        if (superClass != null) {
            if (typeVariables.isEmpty())
                typeBuilder.superclass(superClass);
            else
                typeBuilder.superclass(ParameterizedTypeName.get((ClassName) superClass, typeVariables.toArray(new TypeName[0])));
        }
        // write fields and getters
        inflateOptionType(typeBuilder, false, false, null);

        if (constructors.size() > 1)
            typeBuilder.addField(FieldSpec.builder(TypeName.INT, "__ctorIndex").build());

        int i = 0;
        if (!resultBean) {
            // we need the same constructors
            for (MongoMethod ctor : constructors) {
                MethodSpec.Builder ctorBuilder = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC);
                for (MongoMethodParameter param: ctor.params)
                    ctorBuilder.addParameter(ParameterSpec.builder(param.type.vertxType,  param.name).build());
                if (ctor.mongoJavadoc != null)
                    ctorBuilder.addJavadoc(ctor.mongoJavadoc);
                if (constructors.size() > 1)
                    ctorBuilder.addStatement("__ctorIndex = " + i);
                i++;
                for (MongoMethodParameter param : ctor.params) {
                    Option optionOfCtorParam = getOptionOfCtorParam(param, null);
                    ctorBuilder.addStatement("this." +  optionOfCtorParam.name + " = " + param.name);
                }
                typeBuilder.addMethod(ctorBuilder.build());
            }
            // we need the toMongo method
            addToMongoMethod(typeBuilder);

        } else {
            // write exception fields
            for (Option option: optionsByName.values())
                typeBuilder.addField(FieldSpec.builder(Exception.class, option.name + "Exception").addModifiers(Modifier.PRIVATE).build());

            // private constructor
            typeBuilder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build());

            // we just need a fromDriverClass method
            MethodSpec.Builder methodBuilder = fromDriverClassBuilder();
            typeBuilder.addMethod(methodBuilder.build());
        }
        JavaFile.Builder builder = JavaFile.builder(getTargetPackage(), typeBuilder.build());
        addStaticImports(builder);
        return Collections.singletonList(builder);
    }

    private void addToMongoMethod(TypeSpec.Builder typeBuilder) {
        MethodSpec.Builder toMongo = MethodSpec.methodBuilder("toDriverClass")
                .addParameter(ClassName.bestGuess("io.vertx.mongo.impl.MongoClientContext"), "clientContext")
                .addJavadoc("@return MongoDB driver object\n@hidden")
                .addModifiers(Modifier.PUBLIC);
        TypeName returnType;
        String typeVariableName = null;
        if (typeVariables.isEmpty())
            returnType = ClassName.bestGuess(classDoc.qualifiedTypeName());
        else {
            typeVariableName = typeVariables.get(0).toString();
            returnType = ParameterizedTypeName.get(ClassName.bestGuess(classDoc.qualifiedTypeName()), typeVariables.toArray(new TypeName[0]));
            toMongo.addParameter(
                    ParameterizedTypeName.get(
                            ClassName.get(Function.class),
                            TypeVariableName.get(typeVariableName),
                            TypeVariableName.get(typeVariableName)), "inputMapper");
        }
        toMongo.returns(returnType);
        if (optionsByName.isEmpty()) {
            typeBuilder.addModifiers(Modifier.ABSTRACT);
            toMongo.addModifiers(Modifier.ABSTRACT);
            typeBuilder.addMethod(toMongo.build());
            return;
        }
        int j = 0;
        for (MongoMethod constructor: constructors) {
            if (constructors.size() > 1) {
                if (j == 0)
                    toMongo.beginControlFlow("if (__ctorIndex == " + j + ")");
                else
                    toMongo.nextControlFlow("else if (__ctorIndex == " + j + ")");
            }
            StringJoiner ctorParams = new StringJoiner(", ");
            for (MongoMethodParameter param : constructor.params) {
                Option option = getOptionOfCtorParam(param, null);
                if (option.type.mapper != null) {
                    ctorParams.add("__" + option.name);
                    toMongo.addStatement(option.type.mapper.asStatementFromExpression("$T __" + option.name + " = %s", "this." + option.name, option.type.mongoType, null));
                } else if (option.type.vertxType.toString().equals(typeVariableName)) {
                    ctorParams.add(String.format("inputMapper == null ? this.%s : inputMapper.apply(this.%s)", option.name, option.name));
                } else {
                    ctorParams.add("this." + option.name);
                }
            }
            toMongo.addStatement("return new $T(" + ctorParams + ")", returnType);
            j++;
        }
        if (constructors.size() > 1) {
            toMongo.nextControlFlow("else");
            toMongo.addStatement("throw new IllegalArgumentException($S)", "unknown constructor");
            toMongo.endControlFlow();
        }

        typeBuilder.addMethod(toMongo.build());
    }

    private Pattern GETTER_NAME = Pattern.compile("(?:is|was|get)([A-Z])(.*)");

    private Option getOptionOfCtorParam(MongoMethodParameter param, ConstructorDoc ctor) {
        Optional<Option> matchingOption = optionsByName.values().stream().filter(it ->
                it.name.equals(param.name) &&
                        it.type.mongoType.toString().equals(
                                param.type.mongoType.toString())).findFirst();
        if (matchingOption.isEmpty()) {
            List<Option> possibleMatchingOptions = optionsByName.values().stream().filter(it -> it.type.mongoType.toString().equals(param.type.mongoType.toString())).collect(Collectors.toList());
            if (possibleMatchingOptions.isEmpty())
                throw new IllegalStateException("can't find matching option for ctor param " + param.name + " of "  + ctor);
            else if (possibleMatchingOptions.size() > 1)
                throw new IllegalStateException("many matching options for ctor param " + param.name + " of "  + ctor);
            else
                return possibleMatchingOptions.get(0);
        } else {
            return matchingOption.get();
        }
    }

}
