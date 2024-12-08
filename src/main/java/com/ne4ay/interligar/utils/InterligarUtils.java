package com.ne4ay.interligar.utils;

import javafx.application.Platform;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public final class InterligarUtils {
    public static final String DEFAULT_PORT = "24000";
    private InterligarUtils() {}

    public static Runnable wrapInPlatformCall(Runnable runnable) {
        return () -> Platform.runLater(runnable);
    }

    public static Point getMouseLocation() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    public static void moveMouse(Robot robot, Point destination) {
        int n = 0;
        while ((!destination.equals(getMouseLocation())) && (n++ <= 5)) {
            robot.mouseMove(destination.x, destination.y);
        }
    }
}
