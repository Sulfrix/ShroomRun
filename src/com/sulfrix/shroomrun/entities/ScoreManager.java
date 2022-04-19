package com.sulfrix.shroomrun.entities;

import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.lib.BoundingBox;
import processing.core.PGraphics;
import processing.core.PVector;

public class ScoreManager extends Entity {

    public double scoreOffset;

    public Actor trackedActor;

    public ScoreManager() {
        super(new PVector(0, 0), BoundingBox.zero());
    }

    @Override
    public void update(double timescale) {

    }

    @Override
    public void draw(double timescale, PGraphics g) {

    }
}
