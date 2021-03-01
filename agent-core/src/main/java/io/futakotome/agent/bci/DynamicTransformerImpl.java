package io.futakotome.agent.bci;

import io.futakotome.sdk.ApmInstrumentation;
import io.futakotome.sdk.DynamicTransformer;

import java.util.Collection;

public class DynamicTransformerImpl implements DynamicTransformer {
    @Override
    public void ensureInstrumented(Class<?> classToInstrument, Collection<Class<? extends ApmInstrumentation>> instrumentationClasses) {
        ApmAgent.ensureInstrumented(classToInstrument, instrumentationClasses);
    }
}
