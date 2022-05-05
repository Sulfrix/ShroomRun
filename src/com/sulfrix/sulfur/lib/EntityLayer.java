package com.sulfrix.sulfur.lib;

import com.sulfrix.sulfur.World;
import com.sulfrix.sulfur.entity.Entity;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public class EntityLayer {
    public String name;
    public List<Entity> entities;

    public World world;

    public boolean updateEnabled;
    public boolean drawEnabled;

    public EnumSet<LayerFlag> flags;

    public int drawPriority;

    public EntityLayer(String name) {
        this.name = name;
        entities = new LinkedList<>();
        flags = EnumSet.noneOf(LayerFlag.class);
    }

    public EntityLayer(String name, int drawPriority) {
        this(name);
        this.drawPriority = drawPriority;
    }

    public Entity AddEntity(Entity entity) {
        if (!entities.contains(entity)) {
            if (entity.layer != null) {
                entity.layer.RemoveEntity(entity);
            }
            entity.layer = this;
            entities.add(entity);
        }
        return entity;
    }

    public Entity RemoveEntity(Entity entity) {
        entities.remove(entity);
        entity.layer = null;
        return entity;
    }
}
