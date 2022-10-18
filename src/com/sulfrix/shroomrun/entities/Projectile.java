package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.entities.entityTypes.DamageInfo;
import com.sulfrix.shroomrun.entities.entityTypes.DamageTeam;
import com.sulfrix.shroomrun.entities.entityTypes.Damageable;
import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.entity.PhysicsEntity;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.RenderPosType;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

public class Projectile extends PhysicsEntity {

    public float damage;
    public DamageTeam damageTeam;
    public Entity owner;
    public double lifetime;
    public double maxLifetime;

    public float forceMult;

    public int hits = 0;

    public double trailLifetime = 2;

    public Projectile(PVector pos, float size, double lifetime, PVector velocity, float damage, DamageTeam damageTeam, Entity owner, float forceMult) {
        super(pos, new BoundingBox(size, size));
        OBBCenter = false;
        this.lifetime = lifetime;
        this.maxLifetime = lifetime;
        this.velocity = velocity;
        this.gravityMult = 0;
        this.damage = damage;
        this.damageTeam = damageTeam;
        this.owner = owner;
        this.forceMult = forceMult;
        collisionEnabled = false;
        renderingEnabled = true;
        updateEnabled = true;
        ignoreCollision = entity -> entity == owner;
        points = new ArrayList<>();
        renderPosType = RenderPosType.WORLD_SPACE;
    }

    public static class TrailPoint {
        public PVector position;
        public double time;

        public TrailPoint(PVector pos, double t) {
            position = pos;
            time = t;
        }
    }

    public ArrayList<TrailPoint> points;

    private float getPointWidth(TrailPoint point) {
        float maxSize = boundingBox.height;
        float frac = (float) (trailLifetime-(world.time-point.time));
        return frac*maxSize;
    }

    @Override
    public void update(double timescale) {
        super.update(timescale);
        points.add(new TrailPoint(position.copy(), world.time));
        lifetime -= timescale;
        if (lifetime <= 0) {
            remove();
        }
    }

    @Override
    public void draw(double timescale, PGraphics g) {
        for (int i = points.size()-1; i >= 0; i--) {
            var point = points.get(i);
            if (world.time > point.time + trailLifetime) {
                points.remove(i);
            }
        }

        g.push();
        g.strokeCap(PConstants.ROUND);
        g.beginShape();
        if (points.size() > 2) {
            for (int i = 0; i < points.size(); i++) {
                float frac = i/(float)points.size();
                float nextFrac = (i+i)/(float)points.size();
                var point = points.get(i);
                g.noFill();
                g.strokeWeight(boundingBox.height);
                g.stroke(235, 207, 52, 255*frac);
                g.vertex(point.position.x, point.position.y);
            }
        }
        g.endShape();
        g.pop();
        //g.ellipse(position.x, position.y, boundingBox.width, boundingBox.height);
    }

    @Override
    protected void onCollide(Entity otherEnt) {
        remove();
        if (hits == 0 && otherEnt instanceof Damageable dmg) {
            DamageInfo info = new DamageInfo(damage, damageTeam, velocity.copy().mult(forceMult), owner, this, otherEnt);
            info.type = "projectile";
            boolean result = dmg.damage(info);
            collisionEnabled = false;
            hits ++;
        }
    }
}
