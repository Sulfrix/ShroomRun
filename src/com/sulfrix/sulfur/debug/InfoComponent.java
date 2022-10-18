package com.sulfrix.sulfur.debug;

import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.lib.GlobalManagers.FontManager;
import processing.core.PGraphics;

public abstract class InfoComponent {

    public boolean expandedOnly;
    public boolean clearOnScenario = false;
    public static final float FONT_SIZE = 10;

    public static final float EXPANDED_FONT_SIZE = 20;
    public static final String FONT_NAME = "Arial";

    public InfoComponent(boolean expandedOnly) {
        this.expandedOnly = expandedOnly;
    }

    public abstract float draw(PGraphics g, SulfurGame game, boolean exp);

    public float basicDrawText(PGraphics g, String text, boolean exp) {
        g.push();
        g.fill(0);
        var fsize = exp ? EXPANDED_FONT_SIZE : FONT_SIZE;
        g.textSize(fsize);
        g.textFont(FontManager.useFont(FONT_NAME, fsize));
        var size = g.textAscent();
        g.text(text, 0, 0);
        g.pop();
        return size;
    }
}
