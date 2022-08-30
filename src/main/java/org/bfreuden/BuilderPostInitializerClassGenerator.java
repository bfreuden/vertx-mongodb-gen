package org.bfreuden;

import com.mongodb.MongoClientSettings;
import com.squareup.javapoet.*;
import com.sun.javadoc.ClassDoc;
import io.vertx.core.Context;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BuilderPostInitializerClassGenerator extends APIClassGenerator {

    public BuilderPostInitializerClassGenerator(InspectionContext context) {
        super(context, null);
    }

    @Override
    protected List<JavaFile.Builder> getJavaFiles() {
        String packageName = mapPackageName(MongoClientSettings.class.getPackageName());
        String className = MongoClientSettings.class.getSimpleName() + "Initializer";
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className);
        typeBuilder.addModifiers(Modifier.PUBLIC);
        for (ClassDoc classDoc : context.actualClientSettingsBuilders) {

            ParameterizedTypeName initializerType = ParameterizedTypeName.get(ClassName.get(BiConsumer.class), ClassName.get(Context.class), ClassName.bestGuess(classDoc.qualifiedTypeName() + ".Builder"));

            String fieldName = classDoc.name() + "Initializer";
            typeBuilder.addField(FieldSpec.builder(initializerType, fieldName, Modifier.PRIVATE).build());

            typeBuilder.addMethod(MethodSpec.methodBuilder("initialize" + classDoc.name() + "With")
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(initializerType, "initializer")
                            .addStatement("this." + fieldName + " = initializer")
                            .addStatement("return this")
                            .returns(ClassName.bestGuess(packageName + "." + className))
                    .build());

            typeBuilder.addMethod(MethodSpec.methodBuilder("get" + classDoc.name() + "Initializer")
                            .addModifiers(Modifier.PUBLIC)
                            .addStatement("return this." + fieldName)
                            .returns(initializerType)
                    .build());
        }
        return Collections.singletonList(JavaFile.builder(packageName, typeBuilder.build()));
    }

    @Override
    protected void analyzeClass() {

    }
}
