package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.Entity;
import com.sulfrix.shroomrun.lib.BoundingBox;
import com.sulfrix.shroomrun.lib.GlobalManagers.RNG;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class BackgroundTile extends Entity {

    public static final int tileWidth = 1000;
    public float[] points;
    public float depth;

    public BackgroundTile(PVector pos, float depth, PVector worldPos) {
        super(pos, new BoundingBox(tileWidth, 5));
        collisionEnabled = false;
        renderingEnabled = true;
        renderingOffscreen = true;
        this.depth = depth;
        ZPos = -depth;
        parallax = depth;
        points = new float[(tileWidth/7)];
        for (int i = 0; i < points.length; i++) {
            double prog = (double)i/(double)(points.length-1);
            float xPos = (float) (worldPos.x + (prog-0.5)*tileWidth);
            points[i] = RNG.noise((xPos*(depth/1500))+depth*500+(float)(Background.seed*10))*(1500/depth);
        }
    }

    @Override
    public void update(double timescale) {
        if (world.camera.position.x - world.camera.getBB().width > position.x+tileWidth*depth) {
            remove();
        }
    }

    @Override
    public void draw(double timescale, PGraphics g) {
        g.push();
        g.colorMode(PConstants.HSB);
        g.fill(85, depth*-3+150, depth*6+180);
        g.beginShape();
        g.noStroke();
        for (int i = 0; i < points.length; i++) {
            double prog = (double)(i)/(double)(points.length-1);
            float xPos = (float) (prog*(double)tileWidth);
            g.vertex(xPos, points[i]);
        }
        g.vertex(tileWidth, 1000);
        g.vertex(0, 1000);
        g.endShape();
        g.pop();
    }
}
