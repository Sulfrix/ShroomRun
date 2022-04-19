package com.sulfrix.sulfur.lib.GlobalManagers;

import com.sulfrix.sulfur.SulfurGame;
import processing.core.PApplet;

import java.util.ArrayList;

public abstract class TimeManager extends GlobalManager {

    public static int lastSync = 0;
    /**
     * DeltaTime is not in seconds but is aligned based on a 30hz refresh rate.
     * e.g. running at 60hz gives a 0.5 deltatime
     */
    public static double deltaTime;
    public static double frameTime;

    public static ArrayList<Double> frameTimeList = new ArrayList<>();
    public static double avgFrameTime;
    public static final int fpsAvgSize = 100;

    public static void init(SulfurGame ownerApplet) {
        owner = ownerApplet;
        lastSync=owner.millis();
    }

    /**
     * MUST be run after every draw() at the topmost level, or timing WILL break!
     */
    public static void sync() {
        deltaTime = ((owner.millis() - lastSync)) / 32.0;
        frameTime = ((owner.millis() - lastSync));
        frameTimeList.add(frameTime);
        if (frameTimeList.size() > fpsAvgSize) {
            frameTimeList.remove(0);
        }
        avgFrameTime = calcAvg();
        lastSync = owner.millis();
    }

    private static double calcAvg() {
        var size = frameTimeList.size();
        if (size == 0) {
            return 0;
        }
        double acc = 0;
        for (double a : frameTimeList) {
            acc += a;
        }
        acc /= size;
        return acc;
    }

    public static class TimestepInfo {
        public int timesteps;
        public double deltaTimePerStep;

        public TimestepInfo(int s, double dt) {
            timesteps = s;
            deltaTimePerStep = dt;
        }
    }

    public static TimestepInfo calcTimesteps() {
        if (deltaTime < 1) {
            return new TimestepInfo(1, deltaTime);
        }
        var steps = (int)Math.ceil(deltaTime);
        var dt = deltaTime/steps;
        steps = Math.min(10, steps);
        System.out.println("Overstep activated: " + steps + " steps");
        return new TimestepInfo(steps, dt);
    }

}
