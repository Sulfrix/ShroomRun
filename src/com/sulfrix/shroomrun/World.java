package com.sulfrix.shroomrun;

import com.sulfrix.shroomrun.entities.Camera;
import com.sulfrix.shroomrun.lib.BoundingBox;
import com.sulfrix.shroomrun.lib.GlobalManagers.Display;
import com.sulfrix.shroomrun.lib.Input;
import com.sulfrix.shroomrun.lib.RenderPosType;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collections;

public class World {
    public double globalTimescale = 1;
    public double gravity = 1.2;

    public ArrayList<Entity> entities;
    private ArrayList<Entity> pendingAdd = new ArrayList<>();
    private boolean isUpdating;
    public Camera camera;
    public Input input;

    public int renderedEnts;

    public boolean updateEnabled = true;

    public World() {
        entities = new ArrayList<>();
    }

    public void update(double timescale) {
        if (updateEnabled) {
            isUpdating = true;
            // unmodifiable list used in case an entity is deleted in the middle of an update() loop
            var cambb = camera.getBB();
            for (Entity e : Collections.unmodifiableList(entities)) {
                if (e.updateEnabled) {
                    e.update(timescale * globalTimescale);
                }
                if (e.removeOffscreen && cambb.boxIsLeftOf(e.boundingBox, e.position, camera.position)) {
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

            DoCameraFocus(timescale);
        }
    }

    public void draw(double timescale, PGraphics g) {
        renderedEnts = 0;
        for (Entity e : entities) {
            if ((EntOnscreen(e) || e.renderingOffscreen) && e.renderingEnabled) {
                DrawEntity(e, timescale * globalTimescale, g);
                renderedEnts++;
            }
        }
    }

    public Entity AddEntity(Entity ent) {
        if (isUpdating) {
            pendingAdd.add(ent);
            return ent;
        }
        if (ent.world != null) {
            ent.world.RemoveEntity(ent);
        }
        ent.world = this;
        entities.add(ent);
        return ent;
    }

    public void RemoveEntity(Entity ent) {
        if (isUpdating) {
            ent.queueRemove = true;
        } else {
            ent.world = null;
            if (entities.contains(ent)) {
                entities.remove(ent);
            }
        }

    }

    public void DrawEntity(Entity entity, double timescale, PGraphics g) {
        g.push();
        RenderOffsets(entity, g);
        entity.draw(timescale, g);
        if (ShroomRun.debugOBB) {
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
            g.translate(g.width / 2, g.height / 2);
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
            return true;
        } else {
            var w = (float) (Display.width() * (1 / camera.getScale())) + 0;
            var h = (float) (Display.height() * (1 / camera.getScale())) + 0;
            //return ent.touching(new BoundingBox(w, h), PVector.sub(CameraPos(), new PVector(w/2, h/2)));
            return ent.touching(new BoundingBox(w, h), CameraPos());
        }
    }
}
