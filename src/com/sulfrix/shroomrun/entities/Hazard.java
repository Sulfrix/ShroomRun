package com.sulfrix.shroomrun.entities;

import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.shroomrun.entities.entityTypes.DamageTeam;
import com.sulfrix.shroomrun.entities.entityTypes.Damageable;
import com.sulfrix.sulfur.entity.PhysicsEntity;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.GlobalManagers.AssetCache;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import java.util.HashSet;

public class Hazard extends Entity {

    public String texture;
    PImage texImg;
    HashSet<Entity> hasCollided = new HashSet<>();

    public Hazard(PVector pos, String tex) {
        super(pos);
        renderingEnabled = true;
        renderingOffscreen = false;
        updateEnabled = false;
        OBBCenter = false;
        isTrigger = true;
        removeOffscreen = true;
        texture = tex;
        texImg = AssetCache.getImage(texture);
        var bb = new BoundingBox(30, 6);
        boundingBox = bb;
        bb.offset = new PVector(0, 9);
    }

    @Override
    public void update(double timescale) {

    }

    @Override
    public void draw(double timescale, PGraphics g) {
        g.image(texImg, -15, -15, 30, 30);
    }

    @Override
    public void collide(Entity source) {
        super.collide(source);
        if (source instanceof Damageable) {
            if (!hasCollided.contains(source)) {
                if (source instanceof PhysicsEntity phys) {
                    if (!phys.collisionSides[2]) {
                        return;
                    }
                }
                var dmg = (Damageable)source;
                dmg.damage(DamageTeam.ENEMY, 10, this);
                hasCollided.add(source);
            }
        }

    }
}
