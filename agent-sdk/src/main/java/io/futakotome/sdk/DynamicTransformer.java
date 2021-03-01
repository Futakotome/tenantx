package io.futakotome.sdk;

import java.util.Collection;
import java.util.ServiceLoader;

public interface DynamicTransformer {

    void ensureInstrumented(Class<?> classToInstrument, Collection<Class<? extends ApmInstrumentation>> instrumentationClasses);

    class Accessor {
        private static final DynamicTransformer transformer;

        static {
            ClassLoader classLoader = Accessor.class.getClassLoader();
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
            transformer = ServiceLoader.load(DynamicTransformer.class, classLoader).iterator().next();
        }

        public static DynamicTransformer get() {
            return transformer;
        }
    }
}
