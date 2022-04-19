package com.sulfrix.sulfur.lib.GlobalManagers;

import com.sulfrix.sulfur.SulfurGame;
import processing.core.PApplet;

public abstract class GlobalManager {
    static SulfurGame owner;

    GlobalManager() {}

    public static void init(SulfurGame ownerApplet) {
        owner = ownerApplet;
    }
}
