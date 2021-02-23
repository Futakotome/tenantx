package io.futakotome;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MyAdvice {

    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin Method method,
                             @Advice.AllArguments Object[] args) {
        System.out.println("enter");
        System.out.println(method.toString());
        System.out.println(Arrays.toString(args));
    }

    @Advice.OnMethodExit
    public static void exit(@Advice.Origin Method method,
                            @Advice.AllArguments Object[] args,
                            @Advice.Return Object result) {
        System.out.println("exit");
        System.out.println(method.toString());
        System.out.println(Arrays.toString(args));
        System.out.println(result);
    }
}
