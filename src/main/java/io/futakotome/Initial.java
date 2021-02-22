package io.futakotome;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.isMethod;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class Initial {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("This is the tenant extractor.");

        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("javax.servlet"))
                .transform((builder, typeDescription, classLoader, javaModule) -> builder.visit(Advice
                        .to(MyAdvice.class)
                        .on(isMethod().and(named("getParameterMap")))))
                .installOn(instrumentation);

    }
}
