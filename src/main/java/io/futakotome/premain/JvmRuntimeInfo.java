package io.futakotome.premain;

public class JvmRuntimeInfo {
    private static String javaVersion;
    private static String javaVmName;
    private static String javaVmVersion;
    private static int majorVersion;
    private static int updateVersion;
    private static boolean isHotSpot;

    static {
        parseVmInfo(System.getProperty("java.version"), System.getProperty("java.vm.name"), System.getProperty("java.vm.version"));
    }

    public static void parseVmInfo(String version, String vmName, String vmVersion) {
        javaVersion = version;
        javaVmName = vmName;
        javaVmVersion = vmVersion;

        isHotSpot = vmName.contains("HotSpot(TM)") || vmName.contains("OpenJDK");

        if (version.startsWith("1.")) {
            majorVersion = Character.digit(version.charAt(2), 10);
        } else {
            String majorAsString = version.split("\\.")[0];
            int indexOfDash = majorAsString.indexOf("-");
            if (indexOfDash > 0) {
                majorAsString = majorAsString.substring(0, indexOfDash);
            }
            majorVersion = Integer.parseInt(majorAsString);
        }

        int updateIndex = version.lastIndexOf("_");
        if (updateIndex <= 0) {
            updateVersion = 0;
        } else {
            String updateVersionString;
            int versionSuffixIndex = version.indexOf("-", updateIndex + 1);
            if (versionSuffixIndex <= 0) {
                updateVersionString = version.substring(updateIndex + 1);
            } else {
                updateVersionString = version.substring(updateIndex + 1, versionSuffixIndex);
            }
            try {
                updateVersion = Integer.parseInt(updateVersionString);
            } catch (NumberFormatException e) {
                updateVersion = -1;
            }
        }
        if (updateVersion < 0) {
            System.err.println("Unsupported format of the java.version system property - " + version);
        }
    }

    public static String getJavaVersion() {
        return javaVersion;
    }

    public static String getJavaVmName() {
        return javaVmName;
    }

    public static String getJavaVmVersion() {
        return javaVmVersion;
    }

    public static int getMajorVersion() {
        return majorVersion;
    }

    public static int getUpdateVersion() {
        return updateVersion;
    }

    public static boolean isIsHotSpot() {
        return isHotSpot;
    }

    public static boolean isJavaVersionSupported() {
        if (majorVersion < 7) {
            return false;
        }
        if (isHotSpot) {
            return isHotSpotVersionSupported();
        }
        return true;
    }

    private static boolean isHotSpotVersionSupported() {
        if (updateVersion < 0) {
            return true;
        }
        int java7min = 60;
        int java8min = 40;
        switch (majorVersion) {
            case 7:
                return updateVersion >= java7min;
            case 8:
                return updateVersion >= java8min;
            default:
                return true;
        }
    }
}
