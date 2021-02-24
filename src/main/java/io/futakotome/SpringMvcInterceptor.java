package io.futakotome;

import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

public class SpringMvcInterceptor {
    @RuntimeType
    public static Object intercept(@Argument(0) HttpServletRequest request,
                                   @SuperCall Callable<?> callable) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (request.getParameterMap() != null && request.getParameterMap().size() > 0) {
            request.getParameterMap().keySet().forEach(
                    key -> stringBuilder.append("key=" + key + ";value=" + request.getParameter(key) + ","));
        }
        long agentStart = System.currentTimeMillis();
        try {
            return callable.call();
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            return null;
        } finally {
            System.out.println("path:" + request.getRequestURI() + " 入参:" + stringBuilder.toString() + " 耗时:" + (System.currentTimeMillis() - agentStart));
        }
    }
}
