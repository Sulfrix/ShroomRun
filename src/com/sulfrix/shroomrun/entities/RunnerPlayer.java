package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.Entity;
import com.sulfrix.shroomrun.entities.entityTypes.Damageable;
import com.sulfrix.shroomrun.entities.entityTypes.DamageTeam;
import com.sulfrix.shroomrun.lib.AnimatedSprite;
import com.sulfrix.shroomrun.lib.BoundingBox;
import processing.core.PGraphics;
import processing.core.PVector;

public class RunnerPlayer extends PhysicsEntity implements Damageable {

    public float jumpTime;
    public boolean hasJumped = true;
    public float health = 100f;
    public DamageTeam team;

    public AnimatedSprite sprite;
    public double animTimer;

    public RunnerPlayer(PVector pos) {
        super(pos, new BoundingBox(30, 30));
        renderingEnabled = true;
        collisionEnabled = true;
        ZPos = 1;
        sprite = new AnimatedSprite(30, 30, "shroom.png");
    }

    @Override
    public void update(double timescale) {
        MoveForward(timescale);
        JumpLogic(timescale);
        UpdateSprite(timescale);
        super.update(timescale);
    }

    public void UpdateSprite(double timescale) {
        animTimer += (velocity.x * 0.035) * timescale;
        if (collisionSides[2]) {
            if (animTimer > 4) {
                animTimer = 0;
            }
            if (animTimer <= 3) {
                sprite.currentFrame = (int)animTimer;
            } else {
                sprite.currentFrame = 1;
            }
        } else {
            animTimer = 1.5;
            sprite.currentFrame = 0;
        }
    }

    public void JumpLogic(double timescale) {
        var willJump = world.input.KeyPressed(32);
        if (collisionSides[2]) {
            jumpTime = 8;
            hasJumped = false;
        } else {
            if (!willJump) {
                jumpTime = 0;
            }
        }
        if (jumpTime > 0 && willJump) {
            velocity.y = -9;
            jumpTime -= timescale;
            hasJumped = true;
        } else {
            if (velocity.y > 0 || (jumpTime <= 0 && hasJumped)) {
                gravityMult = 1.5;
            } else {
                gravityMult = 1;
            }
        }
    }

    @Override
    public void draw(double timescale, PGraphics g) {
        sprite.draw(g, 0, 0);
    }

    void MoveForward(double timescale) {
        if (velocity.x < 10) {
            velocity.x += 1 * timescale;
        } else {
            velocity.x -= ((velocity.x - 10) / 15)*timescale;
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

    public void setHealth(float hp) {
        health = hp;
    }

    public float getHealth() {
        return health;
    }
}
