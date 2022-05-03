package com.sulfrix.shroomrun.entities.entityTypes;

import com.sulfrix.sulfur.entity.Entity;

public interface Damageable {
    DamageTeam team = DamageTeam.PLAYER;

    boolean damage(DamageInfo dmgInfo);
    void setHealth(float hp);
    float getHealth();
}
