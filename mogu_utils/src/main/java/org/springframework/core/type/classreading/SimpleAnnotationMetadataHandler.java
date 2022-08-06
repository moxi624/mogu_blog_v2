package org.springframework.core.type.classreading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

public class SimpleAnnotationMetadataHandler {

    private String beanPackage;
    ObjectMapper objectMapper;

    {
        objectMapper = new ObjectMapper();
    }

    public String handler(AnnotatedTypeMetadata metadata) {
        SimpleAnnotationMetadata simpleAnnotationMetadata = null;
        StandardAnnotationMetadata standardAnnotationMetadata = null;
        String className = null;


        try {
            simpleAnnotationMetadata = (SimpleAnnotationMetadata) metadata;

        } catch (ClassCastException e) {
            System.out.print("");
        }

        try {
            standardAnnotationMetadata = (StandardAnnotationMetadata) metadata;
        } catch (ClassCastException e) {
            System.out.print("");
        }
        /**
         * 获取 标注@CustomConditionalOnProperty的全类名 有待优化
         */
        if (simpleAnnotationMetadata != null)
            className = simpleAnnotationMetadata.getClassName();
        if (standardAnnotationMetadata != null)
            className = standardAnnotationMetadata.getClassName();
        if (className == null)
            return "org.springframework.boot.autoconfigure.failure";


        return className;
    }
}
