package com.sulfrix.sulfur.lib.GlobalManagers;

import processing.core.PApplet;

import java.util.Random;

public abstract class RNG extends GlobalManager {

    public static int seed;
    public static int offset;

    public static int RandomInt(int min, int max) {
        var rand = new Random();
        rand.setSeed((seed+offset)*100);
        offset++;
        var offset = min+rand.nextInt((max-min)+1);
        return offset;
    }

    public static int RandomInt(int min, int max, int useOffset) {
        var tempOffset = offset;
        offset = useOffset;
        var output = RandomInt(min, max);
        offset = tempOffset;
        return output;
    }

    public static float noise(float x, float y, float z)  {
        return owner.noise(x, y, z);
    }

    public static float noise(float x, float y) {
        return noise(x, y, 0);
    }

    public static float noise(float x) {
        return noise(x, 0, 0);
    }

    public static void SetSeed(int seedSet) {
        seed = seedSet;
        offset = 0;
    }

    public static void Scramble() {
        SetSeed(new Random().nextInt());
    }
}
