package com.ne4ay.interligar.data;

import java.awt.Point;

public record MouseLocationDelta(int delX, int delY) {
    public static MouseLocationDelta delta(Point previousPoint, Point currentPoint) {
        return new MouseLocationDelta(
            previousPoint.x - currentPoint.x,
            previousPoint.y - currentPoint.y
        );
    }
}
