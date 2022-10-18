package com.sulfrix.shroomrun.entities;

import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.lib.RenderPosType;
import processing.core.PGraphics;
import processing.core.PVector;

public class HighscoreIndicator extends Entity {

    public HighscoreIndicator(PVector pos) {
        super(pos);
        renderingEnabled = true;
        updateEnabled = false;
        renderPosType = RenderPosType.WORLD_SPACE;
        renderingOffscreen = true;
    }

    @Override
    public void update(double timescale) {

    }

    @Override
    public void draw(double timescale, PGraphics g) {
        g.noStroke();
        g.fill(0);
        g.rect(position.x-2, -1000, 7, 3000);
        g.fill(255, 0, 0);
        g.rect(position.x, -1000, 3, 3000);
    }
}
