package io.futakotome.agent.bci;

import com.blogspot.mydailyjava.weaklockfree.WeakConcurrentMap;
import io.futakotome.sdk.ApmInstrumentation;
import io.futakotome.sdk.WeakMapSupplier;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ApmAgent {

    private static final WeakConcurrentMap<Class<?>, Set<Collection<Class<? extends ApmInstrumentation>>>> dynamicallyInstrumentedClasses = WeakMapSupplier.createMap();

    public static void ensureInstrumented(Class<?> classToInstrument, Collection<Class<? extends ApmInstrumentation>> instrumentationClasses) {
        Set<Collection<Class<? extends ApmInstrumentation>>> appliedInstrumentations = getOrCreate(classToInstrument);

        if (!appliedInstrumentations.contains(instrumentationClasses)) {
            synchronized (ApmAgent.class) {

            }
        }
    }

    private static Set<Collection<Class<? extends ApmInstrumentation>>> getOrCreate(Class<?> classToInstrument) {
        Set<Collection<Class<? extends ApmInstrumentation>>> instrumentedClasses = dynamicallyInstrumentedClasses.get(classToInstrument);
        if (instrumentedClasses == null) {
            instrumentedClasses = new HashSet<>();
            Set<Collection<Class<? extends ApmInstrumentation>>> racy = dynamicallyInstrumentedClasses.put(classToInstrument, instrumentedClasses);
            if (racy != null) {
                instrumentedClasses = racy;
            }
        }
        return instrumentedClasses;
    }
}
