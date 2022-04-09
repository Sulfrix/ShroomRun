package com.sulfrix.shroomrun;

import com.sulfrix.shroomrun.lib.BoundingBox;
import com.sulfrix.shroomrun.lib.RenderPosType;
import processing.core.*;

import java.util.UUID;

public abstract class Entity {
    public PVector position;
    public double parallax = 1;
    public PVector velocity = new PVector(0, 0);
    public float ZPos = 0;
    public BoundingBox boundingBox;
    public RenderPosType renderPosType = RenderPosType.WORLD_SPACE;
    public UUID uuid;

    public boolean queueRemove = false;

    public World world;
    // focused entities receive player input from World
    public boolean isFocused;

    public boolean OBBCenter = true;
    public boolean collisionEnabled = false;
    public boolean isTrigger = false;
    public boolean renderingEnabled = false;
    public boolean updateEnabled = true;
    public boolean renderingOffscreen = false;
    public boolean updateOffscreen = true;

    public boolean removeOffscreen = false; // Despite saying "offscreen," this actuall refers to when an entity leaves the screen on the left side.

    public Entity(PVector pos, BoundingBox bb) {
        position = pos;
        boundingBox = bb;
        genUUID();
    }

    public Entity(PVector pos) {
        position = pos;
        boundingBox = BoundingBox.zero();
        genUUID();
    }

    public Entity() {
        position = new PVector(0, 0);
        boundingBox = new BoundingBox(0, 0);
        genUUID();
    }

    void genUUID() {
        uuid = UUID.randomUUID();
    }

    public static boolean touching(Entity e1, Entity e2) {
        if ((!e1.collisionEnabled || !e2.collisionEnabled) || (e1 == e2)) {
            return false;
        } else {
            return BoundingBox.touching(e1.boundingBox, e1.position, e2.boundingBox, e2.position);
        }
    }

    public abstract void update(double timescale);

    public abstract void draw(double timescale, PGraphics g);

    public boolean touching(Entity e2) {
        return Entity.touching(this, e2);
    }

    public boolean touching(BoundingBox bb, PVector pos) {
        return bb.touching(pos, boundingBox, position);
    }

    public boolean touching(PVector thisPos, Entity e2) {
        if ((!collisionEnabled || !e2.collisionEnabled) && !(isTrigger || e2.isTrigger)) {
            return false;
        } else {
            return BoundingBox.touching(boundingBox, thisPos, e2.boundingBox, e2.position);
        }
    }

    public void collide(Entity source) {}

    public void remove() {
        world.RemoveEntity(this);
    }
}
