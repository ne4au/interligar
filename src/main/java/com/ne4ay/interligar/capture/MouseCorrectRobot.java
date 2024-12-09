package com.ne4ay.interligar.capture;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;

public class MouseCorrectRobot extends Robot {
    private final Dimension screenSize;// Primary Screen Size

    public MouseCorrectRobot() throws AWTException {
        super();
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();;
    }

    private static double getTav(Point a, Point b) {
        double resX = a.getX() - b.getX();
        double resY = a.getY() - b.getY();
        // TODO fast sqrt
        return Math.sqrt((resX * resX) + (resY) * (resY));
    }

    public void moveMouse(double xbe, double ybe) {// Position of the cursor in [0,1] ranges. (0,0) is the upper left corner
        int xbepix = (int) (screenSize.width * xbe);
        int ybepix = (int) (screenSize.height * ybe);

        int x = xbepix;
        int y = ybepix;

        Point mert = MouseInfo.getPointerInfo().getLocation();
        Point elozoInitPont = new Point(0, 0);

        int ugyanAztMeri = 0;
        final int ugyanAZtMeriLimit = 30;

        int i = 0;
        final int lepesLimit = 20000;
        while (
            (mert.x != xbepix || mert.y != ybepix) && i < lepesLimit && ugyanAztMeri < ugyanAZtMeriLimit)
        {
            ++i;
            if (mert.x < xbepix)
                ++x;
            else
                --x;
            if (mert.y < ybepix)
                ++y;
            else
                --y;
            mouseMove(x, y);

            mert = MouseInfo.getPointerInfo().getLocation();

            if (getTav(elozoInitPont, mert) < 5)
                ++ugyanAztMeri;
            else
            {
                ugyanAztMeri = 0;
                elozoInitPont.x = mert.x;
                elozoInitPont.y = mert.y;
            }

        }
    }

}
