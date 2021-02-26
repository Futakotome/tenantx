package io.futakotome.sdk;

import com.blogspot.mydailyjava.weaklockfree.WeakConcurrentMap;

import javax.annotation.Nullable;

import static io.futakotome.sdk.NullCheck.isNullKey;
import static io.futakotome.sdk.NullCheck.isNullValue;

public class NullSafeWeakConcurrentMap<K, V> extends WeakConcurrentMap<K, V> {
    public NullSafeWeakConcurrentMap(boolean cleanerThread) {
        super(cleanerThread);
    }

    @Nullable
    @Override
    public V get(K key) {
        if (isNullKey(key)) {
            // super implementation silently adds entries from default value when there is none
            // in the case of 'null', we won't return the default value nor create a map entry with it.
            return null;
        }
        return super.get(key);
    }

    @Nullable
    @Override
    public V getIfPresent(K key) {
        if (isNullKey(key)) {
            return null;
        }
        return super.getIfPresent(key);
    }

    @Override
    public boolean containsKey(K key) {
        if (isNullKey(key)) {
            return false;
        }
        return super.containsKey(key);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        if (isNullKey(key) || isNullValue(value)) {
            return null;
        }
        return super.put(key, value);
    }

    @Nullable
    @Override
    public V putIfAbsent(K key, V value) {
        if (isNullKey(key) || isNullValue(value)) {
            return null;
        }
        return super.putIfAbsent(key, value);
    }

    @Nullable
    @Override
    public V putIfProbablyAbsent(K key, V value) {
        if (isNullKey(key) || isNullValue(value)) {
            return null;
        }
        return super.putIfProbablyAbsent(key, value);
    }

    @Nullable
    @Override
    public V remove(K key) {
        if (isNullKey(key)) {
            return null;
        }
        return super.remove(key);
    }
}
