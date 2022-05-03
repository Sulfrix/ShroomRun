package com.sulfrix.sulfur.entity;

import com.sulfrix.sulfur.debug.console.Console;
import com.sulfrix.sulfur.lib.BoundingBox;
import com.sulfrix.sulfur.lib.GlobalManagers.Display;
import processing.core.PGraphics;
import processing.core.PVector;

public class Camera extends Entity {

    public Entity focus;
    public PVector focusOffset;
    public double zoom = 1;
    public boolean useOptimalScale = true;
    public double focusDragX = 1;
    public double focusDragY = 1;

    public Camera(PVector pos) {
        super(pos, BoundingBox.zero());
    }

    public Camera() {
        super();
    }

    @Override
    public void update(double timescale) {

    }

    @Override
    public void draw(double timescale, PGraphics g) {

    }

    public double getScale() {
        var forcescale = Console.getConVar("cam_forcescale").getDouble();
        if (forcescale > 0) {
            return forcescale;
        }
        if (useOptimalScale) {
            return Display.getOptimalScale(480, 360)*zoom;
        } else {
            return zoom;
        }
    }

    public BoundingBox getBB() {
        var w = (float)(Display.width()*(1/getScale()))+0;
        var h = (float)(Display.height()*(1/getScale()))+0;
        return new BoundingBox(w, h);
    }

    public void doFocus(double timescale) {
        if (focus != null) {
            PVector focusPos = PVector.add(focus.position, focusOffset);
            if (focusDragX > 1) {
                position.x -= ((position.x - focusPos.x) / focusDragX)*timescale;
            } else {
                position.x = focusPos.x;
            }
            if (focusDragY > 1) {
                position.y -= ((position.y - focusPos.y) / focusDragY)*timescale;
            } else {
                position.y = focusPos.y;
            }

        }
    }
}
