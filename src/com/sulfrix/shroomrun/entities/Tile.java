package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.Entity;
import com.sulfrix.shroomrun.lib.GlobalManagers.AssetCache;
import com.sulfrix.shroomrun.lib.BoundingBox;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class Tile extends Entity {

    public String texture;
    PImage texImg;

    public Tile(PVector pos, String tex) {
        super(pos, new BoundingBox(30, 30));
        renderingEnabled = true;
        renderingOffscreen = false;
        updateEnabled = false;
        collisionEnabled = true;
        removeOffscreen = true;
        OBBCenter = false;
        texture = tex;
        texImg = AssetCache.getImage(texture);
        startY = position.y;
    }

    public float temp;
    public float startY;

    @Override
    public void update(double timescale) {
        position.y = startY+(float)Math.sin(temp+position.x)*29;
        temp += timescale/10;
    }

    @Override
    public void draw(double timescale, PGraphics g) {

        g.scale((float)(1/parallax));
        g.imageMode(PConstants.CENTER);
        g.image(texImg, 0, 0, 30, 30);
        /*if (world.input.mousePressed) {
            g.image(texImg, 0, 0, 30, 200);
        }*/
    }
}
