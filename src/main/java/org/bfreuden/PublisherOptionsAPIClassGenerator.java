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

    private final Map<String, String> publisherOptionsClasses;

    public PublisherOptionsAPIClassGenerator(InspectionContext context, ClassDoc classDoc, Map<String, String> publisherOptionsClasses) {
        super(context, classDoc);
        this.publisherOptionsClasses = publisherOptionsClasses;
    }

    @Override
    protected String getTargetClassName() {
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
                .filter(m -> !m.name().equals("getGridFSFile")) // FIXME do something more generic for publishers
                .filter(m -> !m.name().equals("withDocumentClass")) // FIXME do something more generic for publishers
                .filter(m -> !m.name().equals("toCollection")) // FIXME do something more generic for publishers
                .filter(m -> !m.name().equals("getId")) // FIXME do something more generic for publishers
                .filter(m -> !m.name().equals("getObjectId")) // FIXME do something more generic for publishers
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
        if (!options.isEmpty())
            publisherOptionsClasses.put(classDoc.qualifiedTypeName(), getTargetPackage() + "." + getTargetClassName());
    }

    protected MethodSpec.Builder toMongoBuilder() {
        MethodSpec.Builder toMongo = MethodSpec.methodBuilder("initializePublisher")
                .addTypeVariable(TypeVariableName.get("TDocument"))
                .addJavadoc("@hidden")
                .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.bestGuess(classDoc.qualifiedTypeName()), TypeVariableName.get("TDocument")), "publisher").build())
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID);
        List<Option> requiredOptions = options.values().stream().filter(it -> it.inCtor).collect(Collectors.toList());
        if (!requiredOptions.isEmpty())
            throw new IllegalStateException("not supported");
        configurableName = "publisher";
        return toMongo;
    }

    protected MethodSpec toMongo(MethodSpec.Builder toMongoBuilder) {
        return toMongoBuilder.build();
    }

}
