package com.sulfrix.shroomrun.entities.ui;

import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.shroomrun.entities.RunnerPlayer;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.GlobalManagers.Display;
import com.sulfrix.sulfur.lib.GlobalManagers.FontManager;
import com.sulfrix.sulfur.lib.RenderPosType;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class HUDEntity extends Entity {
    RunnerPlayer player;

    public HUDEntity() {
        super(new PVector(0, 0), BoundingBox.zero());
        renderingEnabled = true;
        renderPosType = RenderPosType.SCREEN_SPACE;
        OBBCenter = false;
    }

    @Override
    public void update(double timescale) {

    }

    @Override
    public void draw(double timescale, PGraphics g) {
        float scale = (float)Display.getOptimalScale(480, 460);

        DrawHealthBar(g);
        /*g.push();
        g.texture(hg);
        g.rect(0, 0, g.width/2, g.height/2);
        g.pop();*/
    }

    public void DrawHealthBar(PGraphics g) {
        if (player == null) {
            player = findPlayer();
        }
        g.push();
        float scale = (float)Display.getOptimalScale(480, 460);
        g.scale(scale);
        g.translate(15, 10);
        var hpTextSize = 23;
        g.textFont(FontManager.useFont("sourcesanspro.ttf", hpTextSize*scale));
        g.textSize(hpTextSize);
        g.noStroke();
        g.fill(0);
        g.textAlign(PConstants.LEFT, PConstants.TOP);
        g.text((String.format("%1.0f", player.position.x/30)), 0, 0);
        g.push();
        var hpBarThick = 8;
        var hpBarWide = 200;
        g.translate(0, hpTextSize+10);
        g.fill(0, 127);
        g.rect(0, 0, hpBarWide, hpBarThick, hpBarThick);
        g.fill(30, 255, 60);
        g.rect(0, 0, hpBarWide*(player.health/100f), hpBarThick, hpBarThick);
        g.pop();
        g.pop();
    }

    public RunnerPlayer findPlayer() {
        for (Entity e : world.entities) {
            if (e instanceof RunnerPlayer) {
                return (RunnerPlayer) e;
            }
        }
        return null;
    }
}
