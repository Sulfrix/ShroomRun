package com.sulfrix.shroomrun.entities.entityTypes;

import com.sulfrix.sulfur.entity.Entity;
import processing.core.PVector;

public class DamageInfo {
    public float damage;
    public DamageTeam team;
    public PVector force;
    public Entity attacker;
    public Entity inflictor;
    public Entity victim;
    public String type = "normal";

    public DamageInfo(float damage, DamageTeam team, PVector force, Entity attacker, Entity inflictor, Entity victim) {
        this.damage = damage;
        this.team = team;
        this.force = force;
        this.attacker = attacker;
        this.inflictor = inflictor;
        this.victim = victim;
    }
}
