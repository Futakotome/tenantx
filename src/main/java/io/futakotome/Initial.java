package io.futakotome;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class Initial {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("This is the tenant extractor.");

        new AgentBuilder.Default()
                .type(named("org.springframework.web.servlet.DispatcherServlet"))
                .transform((builder, typeDescription, classLoader, javaModule) ->
                        builder.method(named("doDispatch"))
                                .intercept(MethodDelegation.to(SpringMvcInterceptor.class))
                )
                .installOn(instrumentation);
    }
}
