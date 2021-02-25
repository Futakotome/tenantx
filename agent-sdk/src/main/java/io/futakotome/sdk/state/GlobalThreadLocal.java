package io.futakotome.sdk.state;

import com.blogspot.mydailyjava.weaklockfree.DetachedThreadLocal;

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

}
