package io.futakotome.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class NullCheck {

    private static final Logger logger = LoggerFactory.getLogger(NullCheck.class);

    private static <T> boolean isNull(@Nullable T v, boolean isKey) {
        if (null != v) {
            return false;
        }
        String msg = String.format("trying to use null %s", isKey ? "key" : "value");
        if (logger.isDebugEnabled()) {
            logger.debug(msg, new RuntimeException(msg));
        } else {
            logger.warn(msg);
        }
        return true;
    }

    public static <T> boolean isNullKey(@Nullable T key) {
        return isNull(key, true);
    }

    public static <T> boolean isNullValue(@Nullable T value) {
        return isNull(value, false);
    }
}
