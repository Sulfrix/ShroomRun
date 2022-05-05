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
    public double lifetime;
    public double maxLifetime;

    public int hits = 0;

    public Projectile(PVector pos, float size, double lifetime, PVector velocity, float damage, DamageTeam damageTeam, Entity owner) {
        super(pos, new BoundingBox(size, size));
        OBBCenter = false;
        this.lifetime = lifetime;
        this.maxLifetime = lifetime;
        this.velocity = velocity;
        this.gravityMult = 0;
        this.damage = damage;
        this.damageTeam = damageTeam;
        this.owner = owner;
        collisionEnabled = false;
        renderingEnabled = true;
        updateEnabled = true;
        ignoreCollision = entity -> entity == owner;
    }

    @Override
    public void update(double timescale) {
        super.update(timescale);
        lifetime -= timescale;
        if (lifetime <= 0) {
            remove();
        }
    }

    @Override
    public void draw(double timescale, PGraphics g) {
        g.ellipse(0, 0, boundingBox.width, boundingBox.height);
    }

    @Override
    protected void onCollide(Entity otherEnt) {
        remove();
        if (hits == 0 && otherEnt instanceof Damageable dmg) {
            DamageInfo info = new DamageInfo(damage, damageTeam, velocity.copy(), owner, this, otherEnt);
            info.type = "projectile";
            boolean result = dmg.damage(info);
            collisionEnabled = false;
            hits ++;
        }
    }
}
