package com.sulfrix.shroomrun.entities;

import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.lib.BoundingBox;
import processing.core.PGraphics;
import processing.core.PVector;

public class ScoreManager extends Entity {


    public double scoreBase;
    public double scoreOffset;

    public Actor trackedActor;

    public ScoreManager(Actor trackedActor) {
        super(new PVector(0, 0), BoundingBox.zero());
        this.trackedActor = trackedActor;
        updateEnabled = true;
    }

    @Override
    public void update(double timescale) {
        if (trackedActor != null) {
            scoreBase = (trackedActor.position.x/30)-scoreOffset;
        }
    }

    public void setScore() {
        scoreOffset += scoreBase;
    }

    public double getScore() {
        if (trackedActor != null) {
            if (trackedActor.health <= 0) {
                return scoreOffset;
            } else {
                return scoreOffset += scoreBase;
            }
        } else {
            return scoreOffset += scoreBase;
        }
    }

    @Override
    public void draw(double timescale, PGraphics g) {

    }
}
