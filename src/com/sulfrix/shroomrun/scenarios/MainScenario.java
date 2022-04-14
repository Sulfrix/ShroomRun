package com.sulfrix.shroomrun.scenarios;

import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.Scenario;
import com.sulfrix.shroomrun.ShroomRunOld;
import com.sulfrix.shroomrun.entities.*;
import com.sulfrix.shroomrun.entities.entityTypes.Damageable;
import com.sulfrix.shroomrun.entities.ui.HUDEntity;
import com.sulfrix.sulfur.entity.Camera;
import com.sulfrix.sulfur.lib.GlobalManagers.RNG;
import processing.core.PGraphics;
import processing.core.PVector;

public class MainScenario extends Scenario {
    public Camera camera;
    public Entity player;
    public TerrainGen terrainGen;

    @Override
    public void init() {
        RNG.Scramble();
        camera = new Camera(new PVector(0, 0));

        world.AddEntity(camera);
        world.camera = camera;
        var bg1 = new Background(4);
        var bg2 = new Background(8);
        world.AddEntity(bg1);
        world.AddEntity(bg2);
        bg1.genTile();
        bg2.genTile();
        player = new RunnerPlayer(new PVector(0, -30));
        world.AddEntity(player);
        camera.focus = player;
        camera.focusDragX = 1;
        camera.focusDragY = 1;
        camera.focusOffset = new PVector(0, -30);
        terrainGen = new TerrainGen();
        world.AddEntity(terrainGen);
        terrainGen.trackedEntity = player;
        terrainGen.generate();
        var hud = new HUDEntity();
        world.AddEntity(hud);
    }

    @Override
    public void update(double timescale) {
        super.update(timescale);


        if (player instanceof Damageable) {
            Damageable ply = (Damageable) player;
            if (player.position.y > ((terrainGen.genY+20)*30)) {
                ply.setHealth(0);
            }
            if (ply.getHealth() <= 0) {
                if (applet instanceof ShroomRunOld) {
                    var shrRun = (ShroomRunOld)applet;
                    shrRun.setCurrentScenario(new MainScenario());
                }
            }
        }
    }

    @Override
    public void draw(double timescale, PGraphics g) {
        super.draw(timescale, g);
    }
}
