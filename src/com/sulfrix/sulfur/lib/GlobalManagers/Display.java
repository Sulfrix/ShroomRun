package com.sulfrix.sulfur.lib.GlobalManagers;

import processing.core.PApplet;

public abstract class Display extends GlobalManager {
    public static double getOptimalScale(int targetWidth, int targetHeight) {
        double tW = targetWidth;
        double tH = targetHeight;
        double oW = owner.width;
        double oH = owner.height;
        double ratio1 = tW/tH;
        double ratio2 = oW/oH;
        double outputScale = 1;
        if (ratio1 > ratio2) {
            outputScale = oW/tW;
        } else {
            outputScale = oH/tH;
        }
        if (outputScale < 0.5) {
            outputScale = 0.5;
        }
        return outputScale;
    }

    public static double width() {
        return owner.width;
    }

    public static double height() {
        return owner.height;
    }

    public static PApplet getApplet() {
        return owner;
    }
}
