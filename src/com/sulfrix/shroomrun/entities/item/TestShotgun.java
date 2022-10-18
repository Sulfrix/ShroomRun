package com.sulfrix.shroomrun.entities.item;

import com.sulfrix.shroomrun.entities.Projectile;
import com.sulfrix.sulfur.lib.animation.AtlasSprite;
import processing.core.PGraphics;
import processing.core.PVector;

public class TestShotgun extends Item {

    public double lastShot = 0;

    public TestShotgun(PVector pos) {
        super(pos, AtlasSprite.use("shroomgibs.png", 6).images[0]);
    }

    @Override
    public void updateHeld(double timescale) {
        if (holder.world.input.getActionPressed("fire") && holder.world.time > lastShot+20) {
            holder.velocity.add(new PVector(-16, -5));
            holder.world.AddEntity(new Projectile(holder.position.copy(), 3, 60, new PVector(16, 1), 15, holder.team, holder, 0.3f), "util");
            holder.world.AddEntity(new Projectile(holder.position.copy(), 4, 90, new PVector(19, ((float)Math.random()*2f)-1f), 35, holder.team, holder, 0.8f), "util");
            int numTinys = 12;
            for (int i = 0; i < numTinys; i++) {
                holder.world.AddEntity(new Projectile(holder.position.copy(), 1f, 45, new PVector(17+(float)(Math.random()*3f), ((float)Math.random()*5f)-2.5f), 36f/numTinys, holder.team, holder, 1f/numTinys), "util");
            }
            holder.world.AddEntity(new Projectile(holder.position.copy(), 3, 60, new PVector(16, -1), 15, holder.team, holder, 0.3f), "util");
            lastShot = holder.world.time;
        }
    }

    @Override
    public void drawHeld(double timescale, PGraphics g) {
        g.circle(0, 0, 5);
    }
}
