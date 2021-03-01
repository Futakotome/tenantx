package io.futakotome.agent.impl;

import io.futakotome.agent.impl.error.ErrorCapture;
import io.futakotome.agent.impl.transaction.AbstractSpan;
import io.futakotome.agent.impl.transaction.Span;
import io.futakotome.agent.impl.transaction.Transaction;

import javax.annotation.Nullable;

public interface Tracer {
    @Nullable
    Transaction startRootTransaction(@Nullable ClassLoader initiatingClassLoader);

    @Nullable
    Transaction currentTransaction();

    @Nullable
    AbstractSpan<?> getActive();

    @Nullable
    Span getActiveSpan();

    void captureAndReportException(@Nullable Throwable e, ClassLoader initiatingClassLoader);

    @Nullable
    String captureAndReportException(long epochMicros, @Nullable Throwable e, @Nullable AbstractSpan<?> parent);

    @Nullable
    ErrorCapture captureException(@Nullable Throwable e,
                                  @Nullable AbstractSpan<?> parent,
                                  @Nullable ClassLoader initiatingClassLoader);

    Span getActiveExitSpan();

    TracerState getState();

    void overrideServiceNameForClassLoader(@Nullable ClassLoader classLoader,
                                           @Nullable String serviceName);

    void stop();

    boolean isRunning();


    enum TracerState {
        UNINITIALIZED,
        RUNNING,
        PAUSED,
        STOPPED,
    }
}
