package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.Entity;
import com.sulfrix.shroomrun.entities.entityTypes.Damageable;
import com.sulfrix.shroomrun.entities.entityTypes.DamageTeam;
import com.sulfrix.shroomrun.lib.animation.AnimatedSprite;
import com.sulfrix.shroomrun.lib.BoundingBox;
import processing.core.PGraphics;
import processing.core.PVector;

public class RunnerPlayer extends PhysicsEntity implements Damageable {

    public float jumpTime;
    public boolean hasJumped = true;
    public boolean prevJumpInput = false;
    public float health = 100f;
    public DamageTeam team;

    public AnimatedSprite sprite;

    public RunnerPlayer(PVector pos) {
        super(pos, new BoundingBox(30, 30));
        renderingEnabled = true;
        collisionEnabled = true;
        ZPos = 1;
        gravityMult = 1.5;
        sprite = new AnimatedSprite(30, 30, "shroom.png");
        sprite.addSequence("running", 0.035f, new int[] {0, 1, 2, 1}, true);
        sprite.addSequence("jump", 0.25f, new int[] {0}, false);
        sprite.addSequence("fall", 0.35f, new int[] {0}, false);
    }

    @Override
    public void update(double timescale) {
        MoveForward(timescale);
        super.update(timescale);
        JumpLogic(timescale);
        UpdateSprite(timescale);
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

    public void JumpLogic(double timescale) {
        var willJump = world.input.KeyPressed(32);
        if (collisionSides[2]) {
            jumpTime = 4;
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
        }
        else if (velocity.y > 2 || !willJump) {
            gravityMult = 2.2;
        }
        else if (velocity.y > -2 && velocity.y < 2 && willJump){
            gravityMult = 0.5;
        }
        else{
            gravityMult = 0.9;
        }

        if(!willJump && prevJumpInput && velocity.y < 0){
            velocity.y *= 0.6;
        }
        prevJumpInput = willJump;
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
