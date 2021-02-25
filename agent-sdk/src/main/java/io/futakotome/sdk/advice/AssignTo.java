package io.futakotome.sdk.advice;

import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AssignTo {

    Argument[] arguments() default {};

    Field[] fields() default {};

    Return[] returns() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Argument {
        int value();

        Assigner.Typing typing() default Assigner.Typing.STATIC;

        int index() default -1;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Field {
        String value();

        Class<?> declaringType() default Void.class;

        Assigner.Typing typing() default Assigner.Typing.STATIC;

        int index() default -1;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Return {
        Assigner.Typing typing() default Assigner.Typing.STATIC;

        int index() default -1;
    }
}
