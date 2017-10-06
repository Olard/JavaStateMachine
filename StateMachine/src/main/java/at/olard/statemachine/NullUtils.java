package at.olard.statemachine;

import java.util.Collections;
import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * Utility functions for null checks.
 */
@UtilityClass
public class NullUtils {

    private final Map<String, Object> nullMap = Collections.emptyMap();

    /**
     * Can be used to avoid null checks via static code analysis where the value is filled in later.
     *
     * @return {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> T promiseNonNull() {
        return (T) nullMap.get("");
    }
}
