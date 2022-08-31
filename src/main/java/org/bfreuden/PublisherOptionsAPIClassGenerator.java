package org.bfreuden;

import com.squareup.javapoet.*;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;
import org.reactivestreams.Publisher;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PublisherOptionsAPIClassGenerator extends OptionsAPIClassGenerator {

    public PublisherOptionsAPIClassGenerator(InspectionContext context, ClassDoc classDoc) {
        super(context, classDoc, true, false);
    }

    @Override
    protected String getTargetClassName() {
        if (classDoc.name().equals("GridFSDownloadPublisher"))
            return super.getTargetClassName().replace("Publisher", "ControlOptions");
        else
            return super.getTargetClassName().replace("Publisher", "Options");
    }

    @Override
    protected void analyzeClass() {
        Type superclassType = classDoc.superclassType();
        if (superclassType != null && !superclassType.qualifiedTypeName().equals("java.lang.Object"))
            throw new IllegalStateException("unsupported super class: " + superclassType + " for " + classDoc.qualifiedTypeName());
        ClassDoc[] interfaces = classDoc.interfaces();
        for (ClassDoc inter: interfaces) {
            if (!inter.qualifiedTypeName().equals(Publisher.class.getName()))
                throw new IllegalStateException("unsupported interface: " + inter + " for " + classDoc.qualifiedTypeName());
        }
        List<MethodDoc> methods = new ArrayList<>(Arrays.stream(classDoc.methods())
                .filter(m -> !m.name().equals("toString"))
                .filter(m -> !m.name().equals("hashCode"))
                .filter(m -> !m.name().equals("getGridFSFile")) // TODO do something more generic for publishers
                .filter(m -> !m.name().equals("withDocumentClass")) // TODO do something more generic for publishers
                .filter(m -> !m.name().equals("toCollection")) // TODO do something more generic for publishers
                .filter(m -> !m.name().equals("getId")) // FIXME this method is missing in the generated classes
                .filter(m -> !m.name().equals("getObjectId")) // FIXME this method is missing in the generated classes
                .filter(m -> !m.name().equals("equals"))
                .collect(Collectors.toList()));

        MethodDoc first = Arrays.stream(classDoc.methods())
                .filter(m -> m.name().equals("first"))
                .findFirst().orElse(null);
        if (first == null  && !getTargetPackage().contains("gridfs"))
            throw new IllegalStateException("no first method in " + classDoc.qualifiedTypeName());
        methods.remove(first);

        MethodDoc toCollection = Arrays.stream(classDoc.methods())
                .filter(m -> m.name().equals("toCollection"))
                .findFirst().orElse(null);
        if (toCollection != null)
            methods.remove(first);

        List<MethodDoc> fluentSetters = methods.stream()
                .filter(m -> m.parameters().length == 1 || m.parameters().length == 2 && m.parameters()[1].type().qualifiedTypeName().equals(TimeUnit.class.getName()))
                .filter(m -> isFluentSetter(classDoc, m))
                .collect(Collectors.toList());
        analyzeSetterOptions(methods, fluentSetters);
        if (!methods.isEmpty())
            throw new IllegalStateException("unknown method: " + methods);
        if (!optionsByName.isEmpty())
            context.publisherOptionsClasses.put(classDoc.qualifiedTypeName(), getTargetPackage() + "." + getTargetClassName());
    }

    @Override
    protected MethodSpec.Builder toDriverClassOrInitDriverBuilderClassMethod(boolean isDelegatingClass) {
        boolean hasParam = Arrays.stream(classDoc.typeParameters()).count() != 0;
        ClassName publisherType = ClassName.bestGuess(classDoc.qualifiedTypeName());
        TypeName publisherTypeWithParam = ParameterizedTypeName.get(publisherType, TypeVariableName.get("TDocument"));
        String paramDoc = hasParam ? "@param <TDocument> document class\n" : "";
        MethodSpec.Builder toMongo = MethodSpec.methodBuilder("initializePublisher")
                .addParameter(ClassName.bestGuess("io.vertx.mongo.impl.MongoClientContext"), "clientContext")
                .addJavadoc("@param publisher MongoDB driver publisher\n" + paramDoc + "@hidden")
                .addParameter(ParameterSpec.builder(hasParam ? publisherTypeWithParam : publisherType, "publisher").build())
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID);
        if (hasParam)
            toMongo.addTypeVariable(TypeVariableName.get("TDocument"));
        List<Option> requiredOptions = optionsByName.values().stream().filter(it -> it.mandatory).collect(Collectors.toList());
        if (!requiredOptions.isEmpty())
            throw new IllegalStateException("not supported");
        configurableName = "publisher";
        return toMongo;
    }

    protected MethodSpec finalizeToDriverClassMethod(MethodSpec.Builder toMongoBuilder) {
        return toMongoBuilder.build();
    }

}
