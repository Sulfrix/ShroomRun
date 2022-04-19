package com.sulfrix.shroomrun.entities.ui;

import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.GlobalManagers.FontManager;
import processing.core.PGraphics;
import processing.core.PVector;

public class TextEntity extends Entity {

    String text;
    float size;

    int xAlign;
    int yAlign;

    public TextEntity(PVector pos, String text, float size, int alignx, int aligny) {
        super(pos, BoundingBox.zero());
        SetText(text, size, alignx, aligny);

        renderingEnabled = true;
        OBBCenter = false;
    }

    @Override
    public void update(double timescale) {

    }

    @Override
    public void draw(double timescale, PGraphics g) {
        g.textAlign(xAlign, yAlign);
        FontManager.quickUse(g, "Arial", size);
        g.textSize(size);
        g.text(text, 0, 0);
    }

    public void SetText(String text, float size, int alignx, int aligny) {
        xAlign = alignx;
        yAlign = aligny;
        this.text = text;
        this.size = size;
    }
}
