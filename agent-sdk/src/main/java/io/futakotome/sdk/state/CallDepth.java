package io.futakotome.sdk.state;

import java.util.concurrent.ConcurrentHashMap;

public class CallDepth {
    private static final ConcurrentHashMap<String, CallDepth> registry = new ConcurrentHashMap<>();
    private final ThreadLocal<Integer> callDepthPerThread = new ThreadLocal<>();

    private CallDepth() {

    }

    public static CallDepth get(Class<?> adviceClass) {
        String key = adviceClass.getName();
        CallDepth callDepth = registry.get(key);
        if (callDepth == null) {
            registry.putIfAbsent(key, new CallDepth());
            callDepth = registry.get(key);
        }
        return callDepth;
    }

    static void clearRegistry() {
        registry.clear();
    }

    public int increment() {
        int depth = get();
        set(depth + 1);
        return depth;
    }

    public boolean isNestedCallAndIncrement() {
        return increment() != 0;
    }

    public int decrement() {
        int depth = get() - 1;
        set(depth);
        assert depth >= 0;
        return depth;
    }

    public boolean isNestedCallAndDecrement() {
        return decrement() != 0;
    }

    private int get() {
        Integer callDepthForCurrentThread = callDepthPerThread.get();
        if (callDepthForCurrentThread == null) {
            callDepthForCurrentThread = 0;
            callDepthPerThread.set(callDepthForCurrentThread);
        }
        return callDepthForCurrentThread;
    }

    private void set(int depth) {
        callDepthPerThread.set(depth);
    }
}
