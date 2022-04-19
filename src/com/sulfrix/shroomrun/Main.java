package com.sulfrix.shroomrun;

import com.sulfrix.shroomrun.scenarios.MainScenario;
import com.sulfrix.sulfur.DisplayMode;
import processing.core.PApplet;

public class Main {
    public static void main(String[] args) {
        runSketch(new ShroomRun(new DisplayMode.Windowed(), new MainScenario()));
        //PApplet.main("com.sulfrix.shroomrun.ShroomRun");
    }

    public static void runSketch(PApplet sketch) {
        PApplet.runSketch(new String[] {"ShroomRun"}, sketch);
    }
}
