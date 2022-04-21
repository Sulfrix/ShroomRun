package com.sulfrix.sulfur.lib.input;

import com.sulfrix.shroomrun.Main;
import com.sulfrix.sulfur.DisplayMode;
import com.sulfrix.sulfur.SulfurGame;
import processing.core.PApplet;

import java.util.HashMap;

public class Input {
    public int mouseX = 0;
    public int mouseY = 0;
    public int pmouseX = 0;
    public int pmouseY = 0;
    public boolean mousePressed = false;
    public int lastKey = 0;

    public HashMap<String, InputAction> actions = new HashMap<>();

    public SulfurGame owner;

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

    public void update(SulfurGame applet) {
        mouseX = applet.mouseX;
        mouseY = applet.mouseY;
        pmouseX = applet.pmouseX;
        pmouseY = applet.pmouseY;
        mousePressed = applet.mousePressed;
        owner = applet;
    }

    public void PressKey(int key) {
        keys.put(key, true);
        lastKey = key;
        // hard coded keys, fight me.

        if (key == 96) {
            owner.drawConsole = !owner.drawConsole;
        }

        if (key == 99) {
            owner.debugText = !owner.debugText;
        }
        if (key == 100) {
            owner.framerateGraph.clear();
            owner.frameGraph = !owner.frameGraph;
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
