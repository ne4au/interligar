package com.ne4ay.interligar.data;

import java.awt.Point;

public record MouseLocationDelta(double delX, double delY) {
    public static MouseLocationDelta delta(Point previousPoint, Point currentPoint) {
        return new MouseLocationDelta(
            previousPoint.getX() - currentPoint.getX(),
            previousPoint.getY() - currentPoint.getY()
        );
    }
}
