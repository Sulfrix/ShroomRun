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
    private final List<Entity> pendingAdd = new ArrayList<>();
    private boolean queueSort;
    public Camera camera;
    public Input input;

    public double time;

    public HashMap<String, EntityLayer> layers;

    public int renderedEnts;

    public boolean updateEnabled = true;

    public String[] layerOrder;

    public World() {
        entities = new LinkedList<>();
        layers = new HashMap<>();
        AddLayer(new EntityLayer("default"));
        var ndLayer = new EntityLayer("nodraw");
        ndLayer.drawEnabled = false;
        AddLayer(ndLayer);
        var guilayer = new EntityLayer("gui");
        guilayer.drawPriority = 10;
        AddLayer(guilayer);
    }

    public void update(double timescale) {
        if (updateEnabled) {
            boolean isUpdating = true;
            var cambb = camera.getBB();
            for (String layername : layerOrder) {
                var layer = GetLayer(layername);
                layer.CleanEntities();
                if (layer.updateEnabled) {
                    Entity[] ents = layer.entities.toArray(new Entity[0]);
                    for (Entity e : ents) {
                        if (e.updateEnabled && (e.updateOffscreen || e.onScreen)) {
                            e.update(timescale * globalTimescale);
                        }
                        if (Console.getConVar("ent_leftdelete").getBoolean() && e.removeOffscreen && cambb.copy().scale(4).boxIsLeftOf(e.boundingBox, e.position, camera.position)) {
                            RemoveEntity(e);
                        }
                    }
                }
            }
            isUpdating = false;

            for (Entity newEnt : pendingAdd) {
                AddEntity(newEnt);
            }
            pendingAdd.clear();

            for (Entity e : entities) {
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
        for (String layername : layerOrder) {
            var layer = GetLayer(layername);
            if (layer.drawEnabled) {
                for (Entity e : layer.entities) {
                    if ((!doCulling || e.renderingOffscreen || EntOnscreen(e)) && e.renderingEnabled) {
                        DrawEntity(e, timescale * globalTimescale, g);
                        renderedEnts++;
                    }
                }
            }
        }
    }

    public Entity AddEntity(Entity ent, String layer) {
        // oh yeah we doin this
        EntityLayer l = GetLayer(layer);
        if (l != null) {
            l.AddEntity(ent);
            ent.world = this;
        } else {
            System.out.println("Nonfatal: " + ent.getClass().getSimpleName() + " attempted add to nonexistent layer " + layer);
        }
        return ent;
    }

    public Entity AddEntity(Entity ent) {
        return AddEntity(ent, "default");
    }

    public void AddEntitySort(Entity ent) {
        AddEntity(ent);
        //queueSort = true;
    }

    public void RemoveEntity(Entity ent) {
        ent.layer.RemoveEntity(ent);
    }

    public void AddLayer(EntityLayer layer) {
        layers.put(layer.name, layer);
        UpdateLayers();
    }

    public EntityLayer GetLayer(String layerName) {
        return layers.get(layerName);
    }

    public void UpdateLayers() {
        var list = layers.values();
        var sorted = list.stream().sorted(Comparator.comparingInt(o -> o.drawPriority));
        layerOrder = new String[layers.size()];
        int i = 0;
        for (EntityLayer layer : sorted.toList()) {
            layerOrder[i] = layer.name;
            i++;
        }
    }

    private final boolean obbdebug = false; // may be broken, idk

    public void DrawEntity(Entity entity, double timescale, PGraphics g) {
        g.push();
        RenderOffsets(entity, g);
        entity.draw(timescale, g);
        if (obbdebug) {
            g.push();
            g.stroke(255);
            g.strokeWeight(1);
            g.fill(0, 0, 0, 40);
            g.translate(entity.boundingBox.offset.x, entity.boundingBox.offset.y);
            g.rect(0, 0, entity.boundingBox.width, entity.boundingBox.height);
            g.stroke(255, 0, 0);
            g.noFill();
            g.strokeWeight(2);
            g.point(0, 0);
            g.pop();
        }
        g.pop();
    }

    public void RenderOffsets(Entity entity, PGraphics g) {
        if (entity.renderPosType == RenderPosType.LOCAL_SPACE || entity.renderPosType == RenderPosType.WORLD_SPACE) {
            PVector camPos = CameraPos();
            //g.translate(-camPos.x + (g.width / 2) + entity.position.x, -camPos.y + (g.height / 2) + entity.position.y);
            g.translate((float) (-camPos.x * camera.getScale()) / (float) entity.parallax, (float) (-camPos.y * camera.getScale()) / (float) entity.parallax);
            g.translate(g.width / 2f, g.height / 2f);
            g.scale((float) camera.getScale());
            //g.translate(entity.position.x*(float)entity.parallax, entity.position.y*(float)entity.parallax, entity.ZPos);
            if (entity.renderPosType == RenderPosType.LOCAL_SPACE) {
                g.translate(entity.position.x / (float) entity.parallax, entity.position.y / (float) entity.parallax, entity.ZPos*10);
            }

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

    public boolean EntOnscreen(Entity ent, float outMult) {

        if (ent.renderPosType == RenderPosType.SCREEN_SPACE || ent.parallax != 1) {
            ent.onScreen = true;
            return true;
        } else {
            var w = (float) (Display.width() * outMult * (1 / camera.getScale())) + 0;
            var h = (float) (Display.height() * outMult * (1 / camera.getScale())) + 0;
            //return ent.touching(new BoundingBox(w, h), PVector.sub(CameraPos(), new PVector(w/2, h/2)));
            ent.onScreen = ent.touching(new BoundingBox(w, h), CameraPos());
            return ent.onScreen;
        }
    }

    public boolean EntOnscreen(Entity ent) {
        return EntOnscreen(ent, 1);
    }
}
