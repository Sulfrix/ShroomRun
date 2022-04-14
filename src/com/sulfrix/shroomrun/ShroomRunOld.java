package com.sulfrix.shroomrun;

import com.sulfrix.sulfur.entity.Camera;
import com.sulfrix.sulfur.lib.GlobalManagers.*;
import com.sulfrix.sulfur.lib.input.Input;
import com.sulfrix.shroomrun.scenarios.MainScenario;
import com.sulfrix.sulfur.Scenario;
import processing.core.*;
import processing.event.KeyEvent;
import processing.opengl.PGraphicsOpenGL;
//import processing.sound.SoundFile;

import java.util.ArrayList;

@Deprecated
public class ShroomRunOld extends PApplet {

    // this is shit, remember to remove
    public static boolean debugOBB = false;

    // slightly less shit
    public static boolean debugText = false;

    public static boolean frameGraph = false;

    public static ArrayList<Double> framerateGraph = new ArrayList<>();

    public Scenario currentScenario;
    public Input input = new Input();

    public Camera testCam;

    public void settings() {
        size(480, 360, P3D);
        //fullScreen(P3D);
        noSmooth();
        System.out.println("settings");
    }

    public void setup() {
        surface.setResizable(true);
        if (g instanceof PGraphicsOpenGL) {
            PGraphicsOpenGL ogl = ((PGraphicsOpenGL) g);
            ogl.textureSampling(3);
        }
        frameRate(300);
        //hint(ENABLE_DEPTH_SORT);
        //hint(DISABLE_DEPTH_TEST);

        FontManager.init(this);
        Display.init(this);
        AssetCache.init(this);
        TimeManager.init(this);
        RNG.init(this);
        setCurrentScenario(new MainScenario());
        //var music = new SoundFile(this, "music/music.wav");
        //music.loop();
        //debugPanel = new DebugPanel();

    }

    public void draw() {
        background(176, 252, 255);
        ortho();
        float debugScale = 1f;
        if (debugScale != 1f) {
            translate(width /2, height /2);
            scale(debugScale);

        }
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
        } else {
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

    public void setCurrentScenario(Scenario scenario) {
        if (currentScenario != null) {
            currentScenario.unlinkInput();
        }
        currentScenario = scenario;
        scenario.applet = this;
        currentScenario.linkInput(input);
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
