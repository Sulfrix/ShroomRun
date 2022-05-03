package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.entities.entityTypes.DamageInfo;
import com.sulfrix.shroomrun.entities.entityTypes.DamageTeam;
import com.sulfrix.shroomrun.entities.entityTypes.Damageable;
import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.entity.PhysicsEntity;
import com.sulfrix.sulfur.lib.BoundingBox;
import processing.core.PGraphics;
import processing.core.PVector;

public class Projectile extends PhysicsEntity {

    public float damage;
    public DamageTeam damageTeam;
    public Entity owner;

    public Projectile(PVector pos, float size, PVector velocity, float damage, DamageTeam damageTeam, Entity owner) {
        super(pos, new BoundingBox(size, size));
        OBBCenter = false;
        this.velocity = velocity;
        this.gravityMult = 0;
        this.damage = damage;
        this.damageTeam = damageTeam;
        this.owner = owner;
        collisionEnabled = false;
        renderingEnabled = true;
        updateEnabled = true;
    }

    @Override
    public void update(double timescale) {
        super.update(timescale);
    }

    @Override
    public void draw(double timescale, PGraphics g) {
        g.ellipse(0, 0, boundingBox.width, boundingBox.height);
    }

    @Override
    protected void onCollide(Entity otherEnt) {
        if (world.time - timeCreated < 10) {
            if (otherEnt instanceof Damageable dmg) {
                DamageInfo info = new DamageInfo(damage, damageTeam, velocity, owner, this, otherEnt);
                boolean result = dmg.damage(info);
                if (result) {
                    remove();
                }
            } else remove();
        }
    }
}
