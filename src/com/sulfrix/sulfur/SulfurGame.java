package com.sulfrix.sulfur;

import com.sulfrix.shroomrun.scenarios.MainScenario;
import com.sulfrix.sulfur.lib.GlobalManagers.*;
import com.sulfrix.sulfur.lib.input.Input;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.opengl.PGraphicsOpenGL;

import java.util.ArrayList;

public abstract class SulfurGame extends PApplet {

    public boolean debugText = false;
    public boolean frameGraph = false;

    public ArrayList<Double> framerateGraph = new ArrayList<>();

    // Handoff allows the game to create a new instance of itself
    public boolean isHandoff;

    public DisplayMode displayMode;

    Scenario startingScenario;
    public Scenario currentScenario;

    public Input input = new Input();

    public SulfurGame(DisplayMode displayMode, Scenario startingScenario) {
        this.displayMode = displayMode;
        this.startingScenario = startingScenario;
    }

    public SulfurGame(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public final void settings() {
        if (displayMode != null) {
            displayMode.use(this);
        } else {
            size(480, 360);
        }
    }

    public final void setup() {
        surface.setResizable(true);
        System.out.println("settings");
        if (g instanceof PGraphicsOpenGL) {
            PGraphicsOpenGL ogl = ((PGraphicsOpenGL) g);
            ogl.textureSampling(3);
        }
        frameRate(300);
        FontManager.init(this);
        Display.init(this);
        AssetCache.init(this);
        TimeManager.init(this);
        RNG.init(this);
        if (!isHandoff) {
            setCurrentScenario(startingScenario);
        }
    }

    public final void draw() {
        background(176, 252, 255);
        ortho();
        input.update(this);
        var ts = TimeManager.calcTimesteps();
        for (int step = 0; step < ts.timesteps; step++) {
            currentScenario.update(ts.deltaTimePerStep);
        }
        currentScenario.draw(TimeManager.deltaTime, g);
        if (debugText) {
            g.push();
            g.fill(0);
            var s = 20;
            g.textSize(s);
            FontManager.quickUse(g, "Arial", s);
            g.text(currentScenario.world.entities.size() + " Entities (" + currentScenario.world.renderedEnts + " Rendered)", 0, 1 * s);
            g.text(Math.ceil(1000 / TimeManager.avgFrameTime) + " FPS", 0, 2 * s);
            g.text("Cam Pos: " + currentScenario.world.camera.position, 0, 3 * s);
            g.text("Optimal Zoom: " + Display.getOptimalScale(480, 360), 0, 4 * s);
            g.text("Key: " + keyCode, 0, 5 * s);
            g.text("Window Size: [" + width + ", " + height + "]", 0, 6 * s);
            g.text("deltaTime: " + TimeManager.deltaTime, 0, 7 * s);
            for (int i = 0; i < 50; i++) {
                g.text(currentScenario.world.entities.get(i).getClass().getSimpleName(), 0, (8+i) * s);
            }
            g.pop();
        }
        else {
            g.push();
            g.fill(0);
            g.noStroke();
            FontManager.quickUse(g, "Arial", 10);
            g.textAlign(PConstants.RIGHT, PConstants.TOP);
            g.text(Math.ceil(1000 / TimeManager.avgFrameTime) + " FPS", g.width, 0);
            g.pop();
        }
        if (frameGraph) {
            for (int i = 0; i < framerateGraph.size(); i++) {
                var t = framerateGraph.get(i);
                double scale = 5;
                g.push();
                g.noStroke();
                g.fill(255, (int) (((double) i / framerateGraph.size()) * 200) + 55);
                g.rect(i, (float) (height - (scale * t)), 1, (float) (scale * t));
                g.pop();
            }
        }
        TimeManager.sync();
        if (frameGraph) {
            framerateGraph.add(TimeManager.frameTime);
            while (framerateGraph.size() > width) {
                framerateGraph.remove(0);
            }
        }
    }

    public final void setCurrentScenario(Scenario scenario) {
        if (currentScenario != null) {
            currentScenario.unlinkInput();
        }
        currentScenario = scenario;
        scenario.applet = this;
        currentScenario.linkInput(input);
        if (!scenario.initialized) {
            scenario.init();
        }
    }

    // all this just to switch fullscreen at runtime
    public SulfurGame handoff(DisplayMode displayMode) {
        exit();
        try {
            var game = this.getClass().getConstructor().newInstance(displayMode);
            game.setCurrentScenario(this.startingScenario);
            return game;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public void keyPressed(KeyEvent event) {
        super.keyPressed(event);
        input.PressKey(event.getKeyCode());
    }

    public void keyReleased(KeyEvent event) {
        super.keyReleased(event);
        input.ReleaseKey(event.getKeyCode());
    }

}
