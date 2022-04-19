package com.sulfrix.sulfur;


import com.sulfrix.sulfur.lib.input.Input;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Scenario is like a 'world manager.' A world can function on its own and let all its entities work independently but
 * a scenario will provide top-level management. For example, a Scenario is what tells the game to pause when the player
 * dies.
 */
public abstract class Scenario {
    public World world;
    public Input input;
    public SulfurGame applet;

    public boolean updatedOnce;

    public boolean initialized;

    public Scenario() {
        world = new World();
    }

    public abstract void init();

    public void update(double timescale) {
        world.update(timescale);
        updatedOnce = true;
    }

    public void draw(double timescale, PGraphics g) {
        if (updatedOnce) {
            world.draw(timescale, g);
        }

    }

    public void linkInput(Input input) {
        world.input = input;
        this.input = input;
    }

    public void unlinkInput() {
        world.input = null;
        input = null;
    }
}
