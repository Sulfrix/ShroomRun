package com.sulfrix.shroomrun.entities;

import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.GlobalManagers.RNG;
import processing.core.PGraphics;
import processing.core.PVector;

public class Background extends Entity {

    public float genPos = 0;
    public float depth = 8;

    public static int seed;

    public Background(float depth) {
        super(new PVector(0, 0), BoundingBox.zero());
        updateOffscreen = true;
        updateEnabled = true;
        this.depth = depth;
        seed = RNG.RandomInt(-100, 100);
    }

    public Background() {
        this(8);
    }

    @Override
    public void update(double timescale) {
        var camera = world.camera;
        if (camera.position.x + camera.getBB().width/2 > (genPos-BackgroundTile.tileWidth)*depth) {
            genTile();
        }
    }

    public void genTile() {
        world.AddEntitySort(new BackgroundTile(new PVector(genPos*depth, (depth-2)*-100), depth, new PVector(genPos, 0)));
        genPos+=BackgroundTile.tileWidth;
    }

    @Override
    public void draw(double timescale, PGraphics g) {

    }
}
