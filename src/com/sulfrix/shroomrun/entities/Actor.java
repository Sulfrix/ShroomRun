package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.entities.entityTypes.DamageInfo;
import com.sulfrix.shroomrun.entities.entityTypes.Damageable;
import com.sulfrix.shroomrun.entities.item.Item;
import com.sulfrix.sulfur.entity.PhysicsEntity;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.animation.AnimatedSprite;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.Random;

public class Actor extends PhysicsEntity implements Damageable {

    public float health = 100f;

    public float kbMult = 1;
    public float kbUp = 1.2f;
    public boolean stunned = false;
    public float stunTimer = 0f;

    Item currentItem;

    public AnimatedSprite sprite;

    public Actor(PVector pos) {
        super(pos, new BoundingBox(30, 30));
        OBBCenter = false;
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
        if (collisionSides[2]) {
            if (stunTimer <= 0) {
                stunned = false;
            }
        }
        if (stunTimer > 0) {
            stunTimer -= timescale;
        } else {
            stunTimer = 0;
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
        if (stunned) {
            if (stunTimer <= 0) {
                g.tint(230);
            } else {
                g.tint(255, 200, 200);
            }
        }
        sprite.draw(g, -15, -15);
        if (currentItem != null) {
            currentItem.drawHeld(timescale, g);
        }
    }

    @Override
    public boolean damage(DamageInfo dmgInfo) {
        if (dmgInfo.team != this.team) {
            health -= dmgInfo.damage;
            velocity.add(PVector.mult(dmgInfo.force, kbMult));
            for (int i = 0; i < dmgInfo.damage/3; i++) {
                gib();
            }
            if (dmgInfo.type.equals("hazard")) {
                velocity.x = 0;
            } else {
                velocity.y -= kbUp;
            }
            if (dmgInfo.force.mag() > 2) {
                stun(10);
            }
            return true;
        }
        return false;
    }

    public void gib() {}

    public void stun(float time) {
        stunTimer += time;
        stunned = true;
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
