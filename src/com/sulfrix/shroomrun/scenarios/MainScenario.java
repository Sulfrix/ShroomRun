package com.sulfrix.shroomrun.scenarios;

import com.sulfrix.shroomrun.entities.entityTypes.DamageInfo;
import com.sulfrix.shroomrun.entities.entityTypes.DamageTeam;
import com.sulfrix.sulfur.Scenario;
import com.sulfrix.shroomrun.entities.*;
import com.sulfrix.shroomrun.entities.ui.HUDEntity;
import com.sulfrix.sulfur.entity.Camera;
import com.sulfrix.sulfur.lib.GlobalManagers.Display;
import com.sulfrix.sulfur.lib.GlobalManagers.FontManager;
import com.sulfrix.sulfur.lib.GlobalManagers.RNG;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class MainScenario extends Scenario {
    public Camera camera;
    public Actor player;
    public TerrainGen terrainGen;
    public ScoreManager score;
    public boolean gameOver = false;

    public MainScenario() {
    }

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
        score = new ScoreManager(player);
        world.AddEntity(score);
    }

    @Override
    public void update(double timescale) {
        super.update(timescale);


        if (world.time > 2) {
            if (player.position.y > ((terrainGen.genY+20)*30)) {
                if (player.health > 0) {
                    player.damage(new DamageInfo(100, DamageTeam.ENEMY, new PVector(0, 0), null, null, player));
                }
            }
            if (player.getHealth() <= 0 && !gameOver) {
                score.setScore();
                gameOver = true;
                camera.focus = null;

            }
            if (gameOver) {
                if (input.getActionPressed("jump")) {
                    applet.setCurrentScenario(new MainScenario());
                }
            }
        }
    }

    @Override
    public void draw(double timescale, PGraphics g) {
        super.draw(timescale, g);
        if (gameOver) {
            g.push();
            var scale = (float)Display.getOptimalScale(480, 360);
            g.translate(0, 0, 10);
            g.noStroke();
            g.fill(0, 128);
            g.rect(0, 0, g.width, g.height);
            g.translate(g.width/2f, g.height/2f);
            g.scale(scale);
            g.fill(255);
            g.textAlign(PConstants.CENTER);
            {
                g.push();
                g.translate(0, -30);
                FontManager.quickUse(g, "Arial", 20f*scale);
                g.textSize(20);
                g.text("Game Over!", 0, 0);
                g.pop();
            }
            {
                g.push();
                g.translate(0, 30);
                FontManager.quickUse(g, "Arial", 30f*scale);
                g.textSize(30);
                g.textAlign(PConstants.CENTER, PConstants.TOP);
                g.text((int)score.getScore(), 0, 0);
                g.pop();
            }
            g.pop();
        }
    }
}
