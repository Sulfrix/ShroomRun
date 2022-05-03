package com.sulfrix.shroomrun.entities.item;

import com.sulfrix.sulfur.lib.animation.AtlasSprite;
import processing.core.PGraphics;
import processing.core.PVector;

public class TestItem extends Item {

    public double lastShot = 0;

    public TestItem(PVector pos) {
        super(pos, AtlasSprite.use("shroomgibs.png", 6).images[0]);
    }

    @Override
    public void updateHeld(double timescale) {

    }

    @Override
    public void drawHeld(double timescale, PGraphics g) {
        g.circle(0, 0, 5);
    }
}
