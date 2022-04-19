package com.sulfrix.sulfur.debug;

import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.lib.GlobalManagers.Display;
import com.sulfrix.sulfur.lib.GlobalManagers.FontManager;
import com.sulfrix.sulfur.lib.GlobalManagers.TimeManager;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class DebugInfo implements PConstants {

    public ArrayList<InfoComponent> components = new ArrayList<>();

    public void drawDebugInfo(SulfurGame game, boolean expanded) {
        PGraphics g = game.g;
        g.textAlign(RIGHT, TOP);
        g.translate(g.width, 0);
        for (InfoComponent c : components) {
            if (expanded || !c.expandedOnly) {
                g.translate(0, c.draw(g, game));
            }
        }
    }
}
