package com.sulfrix.sulfur.lib.GlobalManagers;

import processing.core.PApplet;

public abstract class GlobalManager {
    static PApplet owner;

    GlobalManager() {}

    public static void init(PApplet ownerApplet) {
        owner = ownerApplet;
    }
}
