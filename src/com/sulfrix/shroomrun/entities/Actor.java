package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.entities.entityTypes.DamageTeam;
import com.sulfrix.shroomrun.entities.entityTypes.Damageable;
import com.sulfrix.shroomrun.entities.item.Item;
import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.entity.PhysicsEntity;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.animation.AnimatedSprite;
import processing.core.PGraphics;
import processing.core.PVector;

public class Actor extends PhysicsEntity implements Damageable {

    public float health = 100f;

    Item currentItem;

    public AnimatedSprite sprite;

    public Actor(PVector pos) {
        super(pos, new BoundingBox(30, 30));
        renderingEnabled = true;
        collisionEnabled = true;
        ZPos = 1;
        gravityMult = 1.5;
    }

    @Override
    public void update(double timescale) {
        super.update(timescale);
        UpdateSprite(timescale);
        if (currentItem != null) {
            currentItem.updateHeld(timescale);
        }
    }

    public void UpdateSprite(double timescale) {
        if (collisionSides[2]) {
            if (!sprite.currentAnimationName.equals("running")) {
                sprite.switchAnimation("running");
                sprite.setTimer(1.5f);
            }
            sprite.progress(velocity.x*(float)timescale);
        } else {
            if (velocity.y < 0) {
                sprite.switchAnimation("jump");
            } else {
                sprite.switchAnimation("fall");
            }

            sprite.progress((float)timescale);
        }

    }


    @Override
    public void draw(double timescale, PGraphics g) {
        sprite.draw(g, 0, 0);
        if (currentItem != null) {
            currentItem.drawHeld(timescale, g);
        }
    }

    @Override
    public boolean damage(DamageTeam team, float amount, Entity source) {
        if (team != this.team) {
            health -= amount;
            velocity.x = 0;
        }
        return true;
    }

    public void equip(Item item) {
        item.equip(this);
        currentItem = item;
    }

    public void setHealth(float hp) {
        health = hp;
    }

    public float getHealth() {
        return health;
    }
}
