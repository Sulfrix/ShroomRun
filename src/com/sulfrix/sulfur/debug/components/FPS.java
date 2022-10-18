package com.sulfrix.sulfur.debug.components;

import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.debug.InfoComponent;
import com.sulfrix.sulfur.lib.GlobalManagers.TimeManager;
import processing.core.PGraphics;

public class FPS extends InfoComponent {

    public FPS() {
        super(false);
    }

    @Override
    public float draw(PGraphics g, SulfurGame game, boolean exp) {
        return basicDrawText(g, "FPS: " + (int)(1d/(TimeManager.avgFrameTime/1000d)), exp);
    }
}
