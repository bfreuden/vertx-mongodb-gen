package org.bfreuden;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;
import org.reactivestreams.Publisher;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PublisherResultAPIClassGenerator extends APIClassGenerator {

    boolean hasFirst = false;
    boolean hasOtherReactive = false;
    boolean hasToCollection = false;
    boolean hasSpecial = false;
    boolean hasBatchSize = false;

    public PublisherResultAPIClassGenerator(InspectionContext context, ClassDoc classDoc) {
        super(context, classDoc);
    }

    @Override
    protected String getTargetClassName() {
        return super.getTargetClassName().replace("Publisher", "Result");
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
        hasBatchSize = Arrays.stream(classDoc.methods()).anyMatch(m -> m.name().equals("batchSize"));
        List<MethodDoc> methods = new ArrayList<>(Arrays.stream(classDoc.methods())
                .filter(m -> !m.name().equals("toString"))
                .filter(m -> !m.name().equals("hashCode"))
                .filter(m -> !m.name().equals("withDocumentClass")) // TODO ??
                .filter(m -> !m.name().equals("equals"))
                .filter(m -> !m.name().equals("explain")) //FIXME handle explain in AggregatePublisher and FindPublisher
                .filter(m -> !m.returnType().qualifiedTypeName().equals(classDoc.qualifiedTypeName())) // ignore fluent setters
                .collect(Collectors.toList()));

        ArrayList<MethodDoc> methods2 = new ArrayList<>(methods);
        for (MethodDoc method: methods2) {
            if (method.returnType().qualifiedTypeName().equals(Publisher.class.getName())) {
                if (method.name().equals("first")) {
                    methods.remove(method);
                    hasFirst = true;
                } else if (method.name().equals("toCollection")) {
                    methods.remove(method);
                    hasToCollection = true;
                } else {
                    methods.remove(method);
                    System.out.println("**** other reactive method " + method);
                    hasOtherReactive = true;
                }
            }
        }

        if (!methods.isEmpty()) {
            for (MethodDoc method: methods) {
                System.out.println("**** unknown method " + method);
                hasSpecial = true;
            }
        }
        if (!hasFirst && !hasToCollection && !hasSpecial && !hasOtherReactive) {
            throw new IllegalStateException("pure vanilla result detected");
        } else if (hasFirst && !hasToCollection && !hasSpecial && !hasOtherReactive) {
            InspectionContext.PublisherDesc publisherDesc = new InspectionContext.PublisherDesc();
            publisherDesc.firstMethodName = "first";
            publisherDesc.resultClassName = ClassName.bestGuess("io.vertx.mongo.MongoResult");
            if (hasBatchSize)
                publisherDesc.batchSizePropertyName = "batchSize";
            context.publisherDescriptions.put(classDoc.qualifiedTypeName(),publisherDesc);
        } else if (hasFirst && hasToCollection && !hasSpecial && !hasOtherReactive) {
            InspectionContext.PublisherDesc publisherDesc = new InspectionContext.PublisherDesc();
            publisherDesc.firstMethodName = "first";
            publisherDesc.toCollectionMethodName = "toCollection";
            publisherDesc.resultClassName = ClassName.bestGuess("io.vertx.mongo.MongoCollectionResult");
            if (hasBatchSize)
                publisherDesc.batchSizePropertyName = "batchSize";
            context.publisherDescriptions.put(classDoc.qualifiedTypeName(),publisherDesc);
        } else if (!hasFirst && !hasToCollection) {
            InspectionContext.PublisherDesc publisherDesc = new InspectionContext.PublisherDesc();
            publisherDesc.resultClassName = ClassName.bestGuess(getTargetPackage() + "." + getTargetClassName());
            if (hasBatchSize)
                publisherDesc.batchSizePropertyName = "batchSize";
            context.publisherDescriptions.put(classDoc.qualifiedTypeName(),publisherDesc);
        } else {
            throw new IllegalStateException("mixed result detected");
        }
    }

    @Override
    protected List<JavaFile.Builder> getJavaFiles() {
        return Collections.emptyList();
//        if (!hasSpecial && !hasOtherReactive)
//            return Collections.emptyList();
//        TypeSpec.Builder type = TypeSpec.classBuilder(getTargetClassName())
//                .addModifiers(Modifier.PUBLIC);
//        return Collections.singletonList(JavaFile.builder(getTargetPackage(), type.build()));
    }
}
