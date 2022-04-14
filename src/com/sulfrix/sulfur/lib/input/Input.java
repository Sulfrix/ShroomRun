package com.sulfrix.sulfur.lib.input;

import com.sulfrix.shroomrun.ShroomRunOld;
import processing.core.PApplet;

import java.util.HashMap;

public class Input {
    public int mouseX = 0;
    public int mouseY = 0;
    public int pmouseX = 0;
    public int pmouseY = 0;
    public boolean mousePressed = false;

    public HashMap<String, InputAction> actions = new HashMap<>();

    public PApplet owner;

    public Input() {
        addAction(new InputAction("jump", () -> KeyPressed(32)).addBinding(() -> mousePressed));
    }

    public boolean getActionPressed(String name) {
        return getAction(name).isPressed();
    }

    public InputAction getAction(String name) {
        return actions.get(name);
    }

    public InputAction addAction(InputAction action) {
        actions.put(action.name, action);
        return action;
    }

    public HashMap<Integer, Boolean> keys = new HashMap<>();

    public void update(PApplet applet) {
        mouseX = applet.mouseX;
        mouseY = applet.mouseY;
        pmouseX = applet.pmouseX;
        pmouseY = applet.pmouseY;
        mousePressed = applet.mousePressed;
        owner = applet;
    }

    public void PressKey(int key) {
        keys.put(key, true);
        // hard coded keys, fight me.
        if (key == 99) {
            ShroomRunOld.debugText = !ShroomRunOld.debugText;
        }
        if (key == 100) {
            ShroomRunOld.framerateGraph.clear();
            ShroomRunOld.frameGraph = !ShroomRunOld.frameGraph;
        }
    }

    public void ReleaseKey(int key) {
        keys.put(key, false);
    }

    public boolean KeyPressed(int key) {
        if (!keys.containsKey(key)) {
            return false;
        } else {
            return keys.get(key);
        }
    }
}
