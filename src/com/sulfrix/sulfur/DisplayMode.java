package com.sulfrix.sulfur;

import jdk.jshell.spi.ExecutionControl;
import processing.core.PApplet;
import processing.core.PConstants;

public abstract class DisplayMode {
    public abstract void use(PApplet app, int width, int height);
    public abstract void use(PApplet app);

    public static class Windowed extends DisplayMode {
        @Override
        public void use(PApplet app, int width, int height) {
            app.size(width, height, PConstants.P3D);
        }
        @Override
        public void use(PApplet app) {
            use(app, 480, 360);
        }
    }

    public static class Fullscreen extends DisplayMode {
        @Override
        public void use(PApplet app, int width, int height) {
            use(app);
        }
        @Override
        public void use(PApplet app) {
            app.fullScreen(PConstants.P3D);
        }
    }
}
