package io.futakotome.premain;

import io.futakotome.utils.ThreadUtils;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.jar.JarFile;

public class AgentMain {

    public static void premain(String agentArguments, Instrumentation instrumentation) {
        init(agentArguments, instrumentation, true);
    }

    public static void agentmain(String agentArguments, Instrumentation instrumentation) {
        init(agentArguments, instrumentation, false);
    }

    public synchronized static void init(String agentArguments, Instrumentation instrumentation, boolean premain) {
        if (Boolean.getBoolean("javaagent.attached")) {
            return;
        }
        if (!JvmRuntimeInfo.isJavaVersionSupported()) {
            String msgTemplate;

            boolean doDisable;
            if (Boolean.parseBoolean(System.getProperty("javaagent.disable_bootstrap_checks"))) {
                doDisable = false;
                msgTemplate = "WARNING : JVM version unknown or not supported, safety check disabled - %s %s %s";
            } else {
                doDisable = true;
                msgTemplate = "Failed to start agent - JVM version not supported: %s %s %s.\nTo override Java version verification, set the 'javaagent.disable_bootstrap_checks' System property to 'true'.";
            }

            System.err.println(String.format(msgTemplate,
                    JvmRuntimeInfo.getJavaVersion(), JvmRuntimeInfo.getJavaVmName(), JvmRuntimeInfo.getJavaVmVersion()));

            if (doDisable) {
                return;
            }
        }
        FileSystems.getDefault();

        long delayAgentInitMs = -1L;
        String delayAgentInitMsProperty = System.getProperty("javaagent.delay_agent_premain_ms");
        if (delayAgentInitMsProperty != null) {
            try {
                delayAgentInitMs = Long.parseLong(delayAgentInitMsProperty.trim());
            } catch (NumberFormatException numberFormatException) {
                System.err.println("The value of the \"javaagent.delay_agent_premain_ms\" System property must be a number");
            }
        }
        if (premain && shouldDelayOnPremain()) {
            delayAgentInitMs = Math.max(delayAgentInitMs, 3000L);
        }
        if (delayAgentInitMs > 0) {
            delayAndInitAgentAsync(agentArguments, instrumentation, premain, delayAgentInitMs);
        } else {
            loadAndInitialAgent(agentArguments, instrumentation, premain);
        }
    }

    static boolean shouldDelayOnPremain() {
        int majorVersion = JvmRuntimeInfo.getMajorVersion();
        return (majorVersion == 7) ||
                (majorVersion == 8 && JvmRuntimeInfo.isHotSpot() && JvmRuntimeInfo.getUpdateVersion() < 40);

    }

    private static void delayAndInitAgentAsync(final String agentArguments, final Instrumentation instrumentation,
                                               final boolean premain, final long delayAgentInitMs) {
        System.out.println("Delaying apm agent initialization by " + delayAgentInitMs + " ms");
        Thread initThread = new Thread(ThreadUtils.addThreadPrefix("agent-initialization")) {
            @Override
            public void run() {
                try {
                    synchronized (AgentMain.class) {
                        Thread.sleep(delayAgentInitMs);
                        loadAndInitialAgent(agentArguments, instrumentation, premain);
                    }
                } catch (InterruptedException e) {
                    System.err.println(getName() + " thread was interrupted, the agent will not be attached to this JVM. ");
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    System.err.println("Error during Elastic APM Agent initialization: " + throwable.getMessage());
                    throwable.printStackTrace();
                }
            }
        };
        initThread.setDaemon(true);
        initThread.start();
    }

    private synchronized static void loadAndInitialAgent(String agentArguments, Instrumentation instrumentation, boolean premain) {
        try {
            final File agentJarFile = getAgentJarFile();
            try (JarFile jarFile = new JarFile(agentJarFile)) {
                instrumentation.appendToBootstrapClassLoaderSearch(jarFile);
            }
            Class.forName("io.io.futakotome.ApmAgent", true, null)
                    .getMethod("initialize", String.class, Instrumentation.class, File.class, boolean.class)
                    .invoke(null, agentArguments, instrumentation, agentJarFile, premain);
            System.setProperty("javaagent.attached", Boolean.TRUE.toString());
        } catch (Exception | LinkageError e) {
            System.err.println("Filed to start agent");
            e.printStackTrace();
        }
    }

    private static File getAgentJarFile() throws URISyntaxException {
        ProtectionDomain protectionDomain = AgentMain.class.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        if (codeSource == null) {
            throw new IllegalStateException(String.format("Unable to get agent location, protection domain = %s", protectionDomain));
        }
        URL location = codeSource.getLocation();
        if (location == null) {
            throw new IllegalStateException(String.format("Unable to get agent location, code source = %s", codeSource));
        }
        final File agentJar = new File(location.toURI());
        if (!agentJar.getName().endsWith(".jar")) {
            throw new IllegalStateException("Agent is not a jar file: " + agentJar);
        }
        return agentJar.getAbsoluteFile();
    }
}
