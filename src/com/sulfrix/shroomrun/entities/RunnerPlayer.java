package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.entities.item.Item;
import com.sulfrix.shroomrun.entities.item.TestItem;
import com.sulfrix.sulfur.debug.console.Console;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.animation.AnimatedSprite;
import processing.core.PVector;

import java.util.Random;

public class RunnerPlayer extends Actor {

    public float jumpTime;
    public boolean hasJumped = true;
    public boolean prevJumpInput = false;

    Item currentItem;

    public RunnerPlayer(PVector pos) {
        super(pos);
        boundingBox.width = 18;
        sprite = new AnimatedSprite(30, 30, "shroom.png");
        sprite.addSequence("running", 0.035f, new int[] {0, 1, 2, 1}, true);
        sprite.addSequence("jump", 0.25f, new int[] {0}, false);
        sprite.addSequence("fall", 0.35f, new int[] {0}, false);
        equip(new TestItem(position.copy()));
    }

    @Override
    public void update(double timescale) {
        MoveForward(timescale);
        JumpLogic(timescale);
        super.update(timescale);
        float terminalVelocity = 30;
        if (velocity.y > terminalVelocity) {
            velocity.y = terminalVelocity;
        }
        if (health <= 0) {
            renderingEnabled = false;
        }
    }

    public void gib() {
        Random rand = new Random();
        PVector pos = new PVector(position.x+(rand.nextFloat(30)-15), position.y+(rand.nextFloat(30)-15));
        world.AddEntity(new ShroomGib(pos));
    }

    public void JumpLogic(double timescale) {
        var willJump = world.input.getActionPressed("jump");
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
            gravityMult = 1.9;
        }
        else if (Math.abs(velocity.y) < 2){
            gravityMult = 0.5;
        }
        else{
            gravityMult = 0.9;
        }

        if (!willJump && prevJumpInput && velocity.y < 0){
            velocity.y *= 0.6;
        }
        prevJumpInput = willJump;
    }

    void MoveForward(double timescale) {
        if (stunned) {
            return;
        }
        var moveSpeed = Console.getConVar("shroom_movespeed").getDouble();
        if (health > 0) {
            if (velocity.x < moveSpeed) {
                velocity.x += 1 * timescale;
            } else {
                velocity.x -= ((velocity.x - moveSpeed) / 15)*timescale;
            }
        } else {
            velocity.x -= (velocity.x / 15)*timescale;
        }

    }
}
