package io.futakotome.agent.impl.sampling;

import io.futakotome.agent.impl.transaction.Id;

public interface Sampler {

    boolean isSampled(Id traceId);

    double getSampleRate();

    String getTraceStateHeader();
}
