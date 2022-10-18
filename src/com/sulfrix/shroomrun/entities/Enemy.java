package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.entities.entityTypes.DamageInfo;
import com.sulfrix.shroomrun.entities.entityTypes.DamageTeam;
import com.sulfrix.shroomrun.entities.item.Item;
import com.sulfrix.shroomrun.entities.item.TestItem;
import com.sulfrix.sulfur.debug.console.Console;
import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.lib.animation.AnimatedSprite;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.Random;

public class Enemy extends Actor {


    public Enemy(PVector pos) {
        super(pos);
        boundingBox.width = 18;
        sprite = new AnimatedSprite(30, 30, "evilshroom.png");
        sprite.addSequence("running", 0.035f, new int[] {0, 1, 2, 1}, true);
        sprite.addSequence("standing", 0, new int[] {1}, false);
        sprite.addSequence("jump", 0.25f, new int[] {0}, false);
        sprite.addSequence("fall", 0.35f, new int[] {0}, false);
        sprite.switchAnimation("running");
        team = DamageTeam.ENEMY;
        kbMult = 0.4f;
        kbUp = 8;
        removeOffscreen = true;
        collisionEnabled = false;
        isTrigger = true;
    }

    @Override
    public void update(double timescale) {
        super.update(timescale);
        float terminalVelocity = 30;
        if (velocity.y > terminalVelocity) {
            velocity.y = terminalVelocity;
        }
        if (health <= 0) {
            remove();
        }
        if (collisionSides[2]) {
            if (velocity.x > 0) {
                velocity.x -= timescale/3f;
            } else if (velocity.x < 0) {
                velocity.x += timescale/3f;
            }
        }
    }

    public void gib() {
        Random rand = new Random();
        PVector pos = new PVector(position.x+(rand.nextFloat(30)-15), position.y+(rand.nextFloat(30)-15));
        world.AddEntity(new ShroomGib(pos), "util");
    }

    @Override
    public void collide(Entity source) {
        super.collide(source);
        if (source instanceof Actor actor) {
            if (!stunned && !actor.stunned && team != actor.team) {
                var dmgInfo = new DamageInfo(10, team, actor.position.copy().sub(position).normalize().mult(20), this, this, actor);
                velocity.add(actor.velocity);
                actor.damage(dmgInfo);
            }
        }
    }
}
