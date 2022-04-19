package com.sulfrix.shroomrun.entities;

import com.sulfrix.sulfur.entity.PhysicsEntity;
import com.sulfrix.sulfur.lib.BoundingBox;
import processing.core.PGraphics;
import processing.core.PVector;

public class PhysicsTest extends PhysicsEntity {

    public PhysicsTest(PVector pos) {
        super(pos, new BoundingBox(30, 30));
        gravityMult = 1;
        renderingEnabled = true;
        collisionEnabled = true;
    }

    @Override
    public void update(double timescale) {
        if (world.input.KeyPressed(32)) {
            super.update(timescale);
            if (world.input.KeyPressed(39)) {
                velocity.x += 1 * timescale;
            }
            if (world.input.KeyPressed(37)) {
                velocity.x -= 1 * timescale;
            }
            if (world.input.KeyPressed(38)) {
                if (collisionSides[2]) {
                    velocity.y = -9f;
                }
            }
            if (world.input.KeyPressed(40)) {
                velocity.y += 1 * timescale;
            }
        } else {
            velocity.x = 0;
            velocity.y = 0;
        }
    }

    @Override
    public void draw(double timescale, PGraphics g) {
        g.noStroke();
        g.fill(0);
        g.rect(0, 0, 30, 30);
    }
}
