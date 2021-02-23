package io.futakotome;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.ModifierReviewable;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class Initial {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("This is the tenant extractor.");

        new AgentBuilder.Default()
                .type(hasSuperType(nameStartsWith("org.springframework.web.servlet")))
                .transform((builder, typeDescription, classLoader, javaModule) ->
                        builder.visit(Advice
                                .to(MyAdvice.class)
                                .on(isMethod()))
                )
                .installOn(instrumentation);

    }
}
