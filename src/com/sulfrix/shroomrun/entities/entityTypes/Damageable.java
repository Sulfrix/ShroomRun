package com.sulfrix.shroomrun.entities.entityTypes;

import com.sulfrix.sulfur.entity.Entity;

public interface Damageable {
    DamageTeam team = DamageTeam.PLAYER;

    boolean damage(DamageTeam team, float amount, Entity source);
    void setHealth(float hp);
    float getHealth();
}
