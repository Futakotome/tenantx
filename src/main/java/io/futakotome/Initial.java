package io.futakotome;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

public class Initial {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("This is the method monitor.");

        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> builder
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(MethodTimeCostInterceptor.class));

        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {

            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {

            }

            @Override
            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }
        };

        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("javax.servlet.http"))
                .transform(transformer)
                .with(listener)
                .installOn(instrumentation);

    }
}
