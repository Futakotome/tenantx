import io.futakotome.premain.JvmRuntimeInfo;
import org.junit.Test;

import java.util.stream.Stream;

public class JvmInfoTest {

    private static final String HOTSPOT_VM_NAME = "Java HotSpot(TM) 64-Bit Server VM";

    @Test
    public void java8HotspotOnlySupportedAfterUpdate40() {

        // non-hotspot JVM will be supported by default
        checkSupported("", hotspotJava8NotSupported());
        checkSupported("", hotspotJava8supported());

        checkNotSupported(HOTSPOT_VM_NAME, hotspotJava8NotSupported());
        checkSupported(HOTSPOT_VM_NAME, hotspotJava8supported());
    }

    private Stream<String> hotspotJava8supported() {
        return Stream.of(
                "1.8.0_40",
                "1.8.0_40-hello",
                "1.8.0_241",
                "1.8.0_241-hello"
        );
    }

    private Stream<String> hotspotJava8NotSupported() {
        return Stream.of(
                "1.8.0",
                "1.8.0-hello",
                "1.8.0_1",
                "1.8.0_1-hello",
                "1.8.0_39",
                "1.8.0_39-hello"
        );
    }

    private static void checkSupported(String vmName, Stream<String> versions) {
        versions.forEach((v) -> {
            JvmRuntimeInfo.parseVmInfo(v, vmName, null);
            boolean supported = JvmRuntimeInfo.isJavaVersionSupported();
            System.out.println(supported);
        });
    }

    private static void checkNotSupported(String vmName, Stream<String> versions) {
        versions.forEach((v) -> {
            JvmRuntimeInfo.parseVmInfo(v, vmName, null);
            boolean supported = JvmRuntimeInfo.isJavaVersionSupported();
            System.out.println(supported);
        });
    }
}
