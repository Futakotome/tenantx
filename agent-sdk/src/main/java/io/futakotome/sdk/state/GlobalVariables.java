package io.futakotome.sdk.state;

import java.util.concurrent.ConcurrentHashMap;

public class GlobalVariables {
    private static final ConcurrentHashMap<String, Object> registry = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<?> adviceClass, String key, T defaultValue) {
        key = adviceClass.getName() + "." + key;
        if (defaultValue.getClass().getClassLoader() != null && !defaultValue.getClass().getName().startsWith("io.futakotome")) {
            throw new IllegalArgumentException("Registering try specific to an instrumentation plugin would lead to class loader leaks: " + defaultValue);
        }
        T value = (T) registry.get(key);
        if (value == null) {
            registry.putIfAbsent(key, defaultValue);
            value = (T) registry.get(key);
        }
        return value;
    }

}
