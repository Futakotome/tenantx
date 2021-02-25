package io.futakotome.utils;

public final class ThreadUtils {
    public static final String THREAD_PREFIX = "javaagent-";

    private ThreadUtils() {

    }

    public static String addThreadPrefix(String purpose, AgentType agentType) {
        return THREAD_PREFIX + agentType.name() + "-" + purpose;
    }
}
