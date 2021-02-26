package io.futakotome.sdk;

import com.blogspot.mydailyjava.weaklockfree.WeakConcurrentMap;
import com.blogspot.mydailyjava.weaklockfree.WeakConcurrentSet;

public class WeakMapSupplier {
    private static final WeakConcurrentSet<WeakConcurrentMap<?, ?>> registerMaps = new WeakConcurrentSet<>(WeakConcurrentSet.Cleaner.INLINE);
    private static final WeakConcurrentSet<WeakConcurrentSet<?>> registeredSets = new WeakConcurrentSet<>(WeakConcurrentSet.Cleaner.INLINE);

    public static <K, V> WeakConcurrentMap<K, V> createMap() {
        WeakConcurrentMap<K, V> result = new NullSafeWeakConcurrentMap<>(false);
        registerMaps.add(result);
        return result;
    }

    public static <V> WeakConcurrentSet<V> createSet() {
        WeakConcurrentSet<V> weakSet = new NullSafeWeakConcurrentSet<>(WeakConcurrentSet.Cleaner.MANUAL);
        registeredSets.add(weakSet);
        return weakSet;
    }

    public static void expungeStaleEntries() {
        for (WeakConcurrentMap<?, ?> weakConcurrentMap : registerMaps) {
            weakConcurrentMap.expungeStaleEntries();
        }
        for (WeakConcurrentSet<?> weakConcurrentSet : registeredSets) {
            weakConcurrentSet.expungeStaleEntries();
        }
    }


}
