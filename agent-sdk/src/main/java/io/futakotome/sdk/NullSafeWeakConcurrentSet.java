package io.futakotome.sdk;

import com.blogspot.mydailyjava.weaklockfree.WeakConcurrentSet;

import static io.futakotome.sdk.NullCheck.isNullValue;

public class NullSafeWeakConcurrentSet<V> extends WeakConcurrentSet<V> {
    public NullSafeWeakConcurrentSet(Cleaner cleaner) {
        super(cleaner);
    }

    @Override
    public boolean add(V value) {
        if (isNullValue(value)) {
            return false;
        }
        return super.add(value);
    }

    @Override
    public boolean contains(V value) {
        if (isNullValue(value)) {
            return false;
        }
        return super.contains(value);
    }

    @Override
    public boolean remove(V value) {
        if (isNullValue(value)) {
            return false;
        }
        return super.remove(value);
    }
}
