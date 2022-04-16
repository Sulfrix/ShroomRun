package com.sulfrix.shroomrun.entities.item;

import com.sulfrix.shroomrun.entities.RunnerPlayer;
import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.animation.AnimatedSprite;
import processing.core.PGraphics;
import processing.core.PVector;

public abstract class Item extends Entity {

    public RunnerPlayer holder;

    public AnimatedSprite sprite;

    public Item(PVector pos) {
        super(pos, new BoundingBox(30, 30));
    }

    @Override
    public void update(double timescale) {

    }

    @Override
    public void draw(double timescale, PGraphics g) {

    }

    public abstract void updateHeld(double timescale);

    public abstract void drawHeld(double timescale, PGraphics g);
}
