package org.bfreuden;

import com.mongodb.MongoClientSettings;
import com.squareup.javapoet.*;
import com.sun.javadoc.ClassDoc;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class BuilderPostInitializerClassGenerator extends APIClassGenerator {

    public BuilderPostInitializerClassGenerator(InspectionContext context) {
        super(context, null);
    }

    @Override
    protected List<JavaFile.Builder> getJavaFiles() {
        TypeSpec.Builder typeBuilder = TypeSpec.interfaceBuilder(MongoClientSettings.class.getSimpleName() + "Initializer");
        typeBuilder.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        for (ClassDoc classDoc : context.actualClientSettingsBuilders) {
            typeBuilder.addMethod(MethodSpec.methodBuilder("initializeWith" + classDoc.name())
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .addParameter(ParameterizedTypeName.get(ClassName.get(Consumer.class), ClassName.bestGuess(classDoc.qualifiedTypeName()  +".Builder")), "builderInitializer")
                    .build());

        }
        return Collections.singletonList(JavaFile.builder(mapPackageName(MongoClientSettings.class.getPackageName()), typeBuilder.build()));
    }

    @Override
    protected void analyzeClass() {

    }
}
