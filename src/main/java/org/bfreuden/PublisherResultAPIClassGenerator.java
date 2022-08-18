package org.bfreuden;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;
import io.vertx.mongo.client.MongoCollectionResult;
import io.vertx.mongo.MongoResult;
import org.reactivestreams.Publisher;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class PublisherResultAPIClassGenerator extends APIClassGenerator {

    private final Map<String, String> publisherResultClasses;
    boolean hasFirst = false;
    boolean hasOtherReactive = false;
    boolean hasToCollection = false;
    boolean hasSpecial = false;

    public PublisherResultAPIClassGenerator(InspectionContext context, ClassDoc classDoc, Map<String, String> publisherResultClasses) {
        super(context, classDoc);
        this.publisherResultClasses = publisherResultClasses;
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
        List<MethodDoc> methods = new ArrayList<>(Arrays.stream(classDoc.methods())
                .filter(m -> !m.name().equals("toString"))
                .filter(m -> !m.name().equals("hashCode"))
                .filter(m -> !m.name().equals("withDocumentClass")) // FIXME
                .filter(m -> !m.name().equals("equals"))
                .filter(m -> !m.returnType().qualifiedTypeName().equals(classDoc.qualifiedTypeName())) // ignore fluent setters
                .collect(Collectors.toList()));

        ArrayList<MethodDoc> methods2 = new ArrayList<>(methods);
        for (MethodDoc method: methods2) {
            Type type = method.returnType();
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
            publisherResultClasses.put(classDoc.qualifiedTypeName(), MongoResult.class.getName());
//            System.out.println("---> vanilla with first " + classDoc.qualifiedTypeName());
        } else if (hasFirst && hasToCollection && !hasSpecial && !hasOtherReactive) {
            publisherResultClasses.put(classDoc.qualifiedTypeName(), MongoCollectionResult.class.getName());
//            System.out.println("---> vanilla with first + toCollection " + classDoc.qualifiedTypeName());
        } else if (!hasFirst && !hasToCollection) {
            publisherResultClasses.put(classDoc.qualifiedTypeName(), getTargetPackage() + "." + getTargetClassName());
//            System.out.println("---> hasSpecial=" + hasSpecial +" hasOtherReactive=" + hasOtherReactive +" "  + classDoc.qualifiedTypeName());
        } else {
            throw new IllegalStateException("mixed result detected");
        }
    }

    @Override
    protected List<JavaFile.Builder> getJavaFiles() {
        if (!hasSpecial && !hasOtherReactive)
            return Collections.emptyList();
        TypeSpec.Builder type = TypeSpec.classBuilder(getTargetClassName())
                .addModifiers(Modifier.PUBLIC);
        return Collections.singletonList(JavaFile.builder(getTargetPackage(), type.build()));
    }
}
