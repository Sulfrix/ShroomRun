package com.sulfrix.sulfur.debug;

import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.lib.GlobalManagers.FontManager;
import processing.core.PGraphics;

public abstract class InfoComponent {

    public boolean expandedOnly = true;
    public static final float FONT_SIZE = 10;
    public static final String FONT_NAME = "Arial";

    public InfoComponent(boolean expandedOnly) {
        this.expandedOnly = expandedOnly;
    }

    public abstract float draw(PGraphics g, SulfurGame game);

    public float basicDrawText(PGraphics g, String text) {
        g.push();
        g.fill(0);
        g.textSize(FONT_SIZE);
        g.textFont(FontManager.useFont(FONT_NAME, FONT_SIZE));
        var size = g.textAscent();
        g.text(text, 0, 0);
        g.pop();
        return FONT_SIZE;
    }
}
