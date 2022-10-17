package com.sulfrix.sulfur.lib;

public class Color {
    public int r;
    public int g;
    public int b;
    public int a;

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public static Color hsv(float hue, float saturation, float value, int alpha) {
        // from https://stackoverflow.com/a/7901693

        float r = 0, g = 0, b = 0;

        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        if (h == 0) {
            r = value;
            g = t;
            b = p;
        } else if (h == 1) {
            r = q;
            g = value;
            b = p;
        } else if (h == 2) {
            r = p;
            g = value;
            b = t;
        } else if (h == 3) {
            r = p;
            g = q;
            b = value;
        } else if (h == 4) {
            r = t;
            g = p;
            b = value;
        } else if (h <= 6) {
            r = value;
            g = p;
            b = q;
        }

        return new Color((int)r*255, (int)g*255, (int)b*255, alpha);
    }

    public static Color hsv(float hue, float saturation, float value) {
        return hsv(hue, saturation, value, 255);
    }

    public static final Color red = new Color(255, 0, 0);
    public static final Color green = new Color(0, 255, 0);
    public static final Color blue = new Color(0, 0, 255);
    public static final Color yellow = new Color(255, 255, 0);
    public static final Color aqua = new Color(0, 255, 255);
    public static final Color purple = new Color(255, 0, 255);
    public static final Color white = new Color(255, 255, 255);
    public static final Color black = new Color(0, 0, 0);
    public static final Color gray = new Color(127, 127, 127);
}
