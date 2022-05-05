package com.sulfrix.sulfur;

import com.sulfrix.sulfur.debug.console.Console;
import com.sulfrix.sulfur.entity.Camera;
import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.EntityLayer;
import com.sulfrix.sulfur.lib.GlobalManagers.Display;
import com.sulfrix.sulfur.lib.input.Input;
import com.sulfrix.sulfur.lib.RenderPosType;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.*;

public class World {
    public double globalTimescale = 1;
    public double gravity = 1.2;

    @Deprecated
    public List<Entity> entities;
    private List<Entity> pendingAdd = new ArrayList<>();
    private boolean queueSort;
    private boolean isUpdating;
    public Camera camera;
    public Input input;

    public double time;

    public HashMap<String, EntityLayer> layers;

    public int renderedEnts;

    public boolean updateEnabled = true;

    public World() {
        entities = new LinkedList<>();
        AddLayer(new EntityLayer("default"));
    }

    public void update(double timescale) {
        if (updateEnabled) {
            isUpdating = true;
            // unmodifiable list used in case an entity is deleted in the middle of an update() loop
            var cambb = camera.getBB();
            for (Entity e : entities) {
                if (e.updateEnabled && (e.updateOffscreen || e.onScreen)) {
                    e.update(timescale * globalTimescale);
                }
                if (Console.getConVar("ent_culling").getBoolean() && e.removeOffscreen && cambb.boxIsLeftOf(e.boundingBox, e.position, camera.position)) {
                    RemoveEntity(e);
                }
            }
            isUpdating = false;

            for (Entity newEnt : pendingAdd) {
                AddEntity(newEnt);
            }
            pendingAdd.clear();

            for (int i = 0; i < entities.size(); i++) {
                var e = entities.get(i);
                if (e.queueRemove) {
                    RemoveEntity(e);
                }
            }

            //queueSort = true;
            if (queueSort) {
                entities.sort((o1, o2) -> (int) (o1.ZPos - o2.ZPos));
            }

            DoCameraFocus(timescale);
        }
        time+=timescale;
    }

    public void draw(double timescale, PGraphics g) {
        renderedEnts = 0;
        var doCulling = Console.getConVar("ent_culling").getBoolean();
        for (Entity e : entities) {
            if ((!doCulling || e.renderingOffscreen || EntOnscreen(e)) && e.renderingEnabled) {
                DrawEntity(e, timescale * globalTimescale, g);
                renderedEnts++;
            }
        }
    }

    public Entity AddEntity(Entity ent, String layer) {
        if (isUpdating) {
            pendingAdd.add(ent);
            return ent;
        }
        if (ent.world != null) {
            ent.world.RemoveEntity(ent);
        }
        ent.world = this;
        if (ent.related != null) {
            entities.add(entities.indexOf(ent.related)-1, ent);
        } else {
            entities.add(ent);
        }
        ent.timeCreated = time;

        return ent;
    }

    public void AddEntitySort(Entity ent) {
        AddEntity(ent);
        queueSort = true;
    }

    public void RemoveEntity(Entity ent) {
        if (isUpdating) {
            ent.queueRemove = true;
        } else {
            ent.world = null;
            entities.remove(ent);
        }

    }

    public void AddLayer(EntityLayer layer) {
        layers.put(layer.name, layer);
    }

    public EntityLayer GetLayer(String layerName) {
        return layers.get(layerName);
    }

    public void DrawEntity(Entity entity, double timescale, PGraphics g) {
        g.push();
        RenderOffsets(entity, g);
        entity.draw(timescale, g);
        if (false) {
            g.push();
            g.stroke(255);
            g.strokeWeight(1);
            g.fill(0, 0, 0, 40);
            g.translate(entity.boundingBox.offset.x, entity.boundingBox.offset.y);
            g.rect(0, 0, (float) entity.boundingBox.width, (float) entity.boundingBox.height);
            g.stroke(255, 0, 0);
            g.noFill();
            g.strokeWeight(2);
            g.point(0, 0);
            g.pop();
        }
        g.pop();
    }

    public void RenderOffsets(Entity entity, PGraphics g) {
        if (entity.renderPosType == RenderPosType.WORLD_SPACE) {
            PVector camPos = CameraPos();
            //g.translate(-camPos.x + (g.width / 2) + entity.position.x, -camPos.y + (g.height / 2) + entity.position.y);
            g.translate((float) (-camPos.x * camera.getScale()) / (float) entity.parallax, (float) (-camPos.y * camera.getScale()) / (float) entity.parallax);
            g.translate(g.width / 2f, g.height / 2f);
            g.scale((float) camera.getScale());
            //g.translate(entity.position.x*(float)entity.parallax, entity.position.y*(float)entity.parallax, entity.ZPos);
            g.translate(entity.position.x / (float) entity.parallax, entity.position.y / (float) entity.parallax, entity.ZPos*10);

            //g.translate(0, 0, entity.ZPos);
        } else {
            g.translate(entity.position.x, entity.position.y, entity.ZPos);
        }
        if (entity.OBBCenter) {
            g.translate(-(float) entity.boundingBox.width / 2, -(float) entity.boundingBox.height / 2);
        }
    }

    public PVector CameraPos() {
        if (camera != null) {
            return camera.position;
        } else {
            return new PVector(0, 0);
        }
    }

    public void DoCameraFocus(double timescale) {
        if (camera != null) {
            camera.doFocus(timescale);
        }
    }

    public boolean EntOnscreen(Entity ent) {

        if (ent.renderPosType == RenderPosType.SCREEN_SPACE || ent.parallax != 1) {
            ent.onScreen = true;
            return true;
        } else {
            var w = (float) (Display.width() * (1 / camera.getScale())) + 0;
            var h = (float) (Display.height() * (1 / camera.getScale())) + 0;
            //return ent.touching(new BoundingBox(w, h), PVector.sub(CameraPos(), new PVector(w/2, h/2)));
            ent.onScreen = ent.touching(new BoundingBox(w, h), CameraPos());
            return ent.onScreen;
        }
    }
}
