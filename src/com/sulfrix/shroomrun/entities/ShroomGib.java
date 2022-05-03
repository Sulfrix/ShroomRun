package com.sulfrix.shroomrun.entities;

import com.sulfrix.sulfur.entity.PhysicsEntity;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.GlobalManagers.RNG;
import com.sulfrix.sulfur.lib.animation.AtlasSprite;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import java.util.Random;


// not enough particles to warrant a single "Particle" superclass
public class ShroomGib extends PhysicsEntity {

    public PImage sprite;
    public AtlasSprite atlas;

    public ShroomGib(PVector pos) {
        super(pos, new BoundingBox(6, 6));
        collisionEnabled = false;
        renderingEnabled = true;
        updateEnabled = true;
        var rand = new Random();
        velocity = new PVector(rand.nextFloat(-10, 15), -rand.nextFloat(4, 12));
        removeOffscreen = true;

        ignoreCollision = entity -> entity instanceof Actor;
        bounciness = 0.5f;

        atlas = AtlasSprite.use("shroomgibs.png", 6);
        sprite = atlas.images[rand.nextInt(0, 3)];
    }

    @Override
    public void draw(double timescale, PGraphics g) {
        //g.circle(0, 0, 5);
        g.image(sprite, 0, 0, 6, 6);
    }

    @Override
    public void update(double timescale) {
        super.update(timescale);
        if (collisionSides[2]) {
            sprite = atlas.images[3];
            //updateEnabled = false;
            velocity.x -= (velocity.x/1.01)*timescale;
        }
    }
}
