package io.futakotome.sdk.state;

import com.blogspot.mydailyjava.weaklockfree.DetachedThreadLocal;
import io.futakotome.sdk.NullCheck;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalThreadLocal<T> extends DetachedThreadLocal<T> {

    private static final ConcurrentHashMap<String, GlobalThreadLocal<?>> registry = new ConcurrentHashMap<>();
    @Nullable
    private final T defaultValue;

    private GlobalThreadLocal(@Nullable T defaultValue) {
        super(Cleaner.INLINE);
        this.defaultValue = defaultValue;
    }

    public static <T> GlobalThreadLocal<T> get(Class<?> adviceClass, String key) {
        return get(adviceClass.getName() + "." + key, null);
    }

    public static <T> GlobalThreadLocal<T> get(Class<?> adviceClass, String key, @Nullable T defaultValue) {
        return get(adviceClass.getName() + "." + key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    private static <T> GlobalThreadLocal<T> get(String key, @Nullable T defaultValue) {
        GlobalThreadLocal<?> threadLocal = registry.get(key);
        if (threadLocal == null) {
            registry.putIfAbsent(key, new GlobalThreadLocal<>(defaultValue));
        }
        return (GlobalThreadLocal<T>) threadLocal;
    }

    public T get(T defaultValue) {
        T value = get();
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    @Override
    public void set(@Nullable T value) {
        if (NullCheck.isNullKey(value)) {
            return;
        }
        super.set(value);
    }

    @Override
    @Nullable
    protected T initialValue(Thread thread) {
        return defaultValue;
    }

}
