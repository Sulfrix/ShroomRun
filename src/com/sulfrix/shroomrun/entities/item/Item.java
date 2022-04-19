package com.sulfrix.shroomrun.entities.item;

import com.sulfrix.shroomrun.entities.Actor;
import com.sulfrix.shroomrun.entities.RunnerPlayer;
import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.GlobalManagers.AssetCache;
import com.sulfrix.sulfur.lib.animation.AnimatedSprite;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public abstract class Item extends Entity {

    public Actor holder;
    public boolean equipable;

    public AnimatedSprite sprite;

    public Item(PVector pos, PImage img) {
        super(pos, new BoundingBox(30, 30));
        sprite = new AnimatedSprite(30, 30, img);
    }

    public Item(PVector pos, String tex) {
        this(pos, AssetCache.getImage(tex));
    }

    @Override
    public void update(double timescale) {

    }

    @Override
    public void draw(double timescale, PGraphics g) {
        sprite.draw(g, 0, 0);
    }

    public abstract void updateHeld(double timescale);

    public abstract void drawHeld(double timescale, PGraphics g);

    public void pickup(Actor ent) {
        if (equipable) {
            equip(ent);
        }
    }

    public void equip(Actor ent) {
        holder = ent;
        remove();
    }
}
