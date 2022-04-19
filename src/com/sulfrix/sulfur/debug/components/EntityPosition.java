package com.sulfrix.sulfur.debug.components;

import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.debug.InfoComponent;
import com.sulfrix.sulfur.entity.Entity;
import processing.core.PGraphics;

public class EntityPosition extends InfoComponent {

    public String name;
    public Entity trackedEntity;

    public EntityPosition(String name, Entity entity, boolean expandedOnly) {
        super(expandedOnly);
        this.name = name;
        trackedEntity = entity;
        clearOnScenario = true;
    }

    @Override
    public float draw(PGraphics g, SulfurGame game) {
        if (trackedEntity != null) {
            var pos = trackedEntity.position;
            String vecStr = "[" + String.format("%.1f", pos.x)+", "+String.format("%.1f", pos.y) + "]";
            return basicDrawText(g, name + " Position: " + vecStr);
        } else {
            return basicDrawText(g, name + " NOT FOUND");
        }

    }
}
