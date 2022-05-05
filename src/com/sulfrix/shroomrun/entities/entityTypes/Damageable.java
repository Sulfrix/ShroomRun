package com.sulfrix.shroomrun.entities.entityTypes;

import com.sulfrix.sulfur.entity.Entity;

public interface Damageable {

    boolean damage(DamageInfo dmgInfo);
    void setHealth(float hp);
    float getHealth();
    DamageTeam getTeam();
}
