package com.ne4ay.interligar.utils;

import javafx.application.Platform;

public final class InterligarUtils {
    public static final String DEFAULT_PORT = "24000";
    private InterligarUtils() {}

    public static Runnable wrapInPlatformCall(Runnable runnable) {
        return () -> Platform.runLater(runnable);
    }
}
