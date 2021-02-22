package io.futakotome;

import net.bytebuddy.asm.Advice;

import java.util.Arrays;

public class MyAdvice {

    @Advice.OnMethodEnter
    public static void enter(@Advice.This Object obj,
                             @Advice.AllArguments Object[] args,
                             @Advice.Return Object result) {
        System.out.println(obj);
        System.out.println(Arrays.toString(args));
        System.out.println(result);
    }
}
