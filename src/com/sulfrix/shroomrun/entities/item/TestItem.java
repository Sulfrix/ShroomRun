package com.sulfrix.shroomrun.entities.item;

import com.sulfrix.shroomrun.entities.Projectile;
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
        if (holder.world.input.getActionPressed("fire") && holder.world.time > lastShot+5) {
            var proj = new Projectile(holder.position.copy(), 10, 30, new PVector(20, 0).add(holder.velocity), 34, holder.team, holder, 1);
            holder.world.AddEntity(proj, "util");
            lastShot = holder.world.time;
        }
    }

    @Override
    public void drawHeld(double timescale, PGraphics g) {
        g.circle(0, 0, 5);
    }
}
