package com.sulfrix.sulfur;

import com.sulfrix.sulfur.debug.console.ConVar;
import com.sulfrix.sulfur.debug.console.Console;
import com.sulfrix.sulfur.debug.DebugInfo;
import com.sulfrix.sulfur.debug.InfoComponent;
import com.sulfrix.sulfur.debug.components.BasicText;
import com.sulfrix.sulfur.debug.components.FPS;
import com.sulfrix.sulfur.lib.GlobalManagers.*;
import com.sulfrix.sulfur.lib.input.Input;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.opengl.PGraphicsOpenGL;

import java.util.ArrayList;

public abstract class SulfurGame extends PApplet {

    public static boolean isAndroid = false;

    public boolean debugText = false;
    public boolean frameGraph = false;
    public boolean drawConsole = false;
    public float consoleAnim = 0;

    public ArrayList<Double> framerateGraph = new ArrayList<>();

    public DisplayMode displayMode;

    Scenario startingScenario;
    public Scenario currentScenario;

    public Input input = new Input();

    public DebugInfo debugInfo;

    public Console console;

    public SulfurGame(DisplayMode displayMode, Scenario startingScenario) {
        this.displayMode = displayMode;
        this.startingScenario = startingScenario;
        initConsole();
    }

    public SulfurGame(DisplayMode displayMode) {
        this.displayMode = displayMode;
        initConsole();
    }

    public void initConsole() {
        console = new Console();
        Console.addConVar(new ConVar("sulfur_timescale", "1", "double", "Multiply time by this value."));
    }

    private boolean queueWindowChange;
    private DisplayMode targetDisplayMode;

    public double instanceTime = 0;

    public final void settings() {
        if (displayMode != null) {
            displayMode.use(this);
        } else {
            size(480, 360);
        }
        noSmooth();
    }

    public final void setup() {
        if (!isAndroid) {
            surface.setResizable(true);
        }
        if (g instanceof PGraphicsOpenGL) {
            PGraphicsOpenGL ogl = ((PGraphicsOpenGL) g);
            ogl.textureSampling(3);
        }
        hint(DISABLE_DEPTH_TEST);
        frameRate(300);
        FontManager.init(this);
        Display.init(this);
        AssetCache.init(this);
        TimeManager.init(this);
        RNG.init(this);
        debugInfo = new DebugInfo();
        debugInfo.components.add(new FPS());
        debugInfo.components.add(new BasicText((game) -> "Last Key: " + Integer.toString(game.input.lastKey), true));
        setCurrentScenario(startingScenario);
        gameSetup();
    }

    public abstract void gameSetup();

    public final void draw() {
        background(176, 252, 255);
        ortho();
        input.update(this);
        var ts = TimeManager.calcTimesteps();
        var timescale = Console.getConVar("sulfur_timescale").getDouble();
        for (int step = 0; step < ts.timesteps; step++) {
            currentScenario.update(ts.deltaTimePerStep*timescale);
            instanceTime += ts.deltaTimePerStep*timescale;
        }

        currentScenario.draw(TimeManager.deltaTime, g);
        drawDebugText();

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
        if (drawConsole) {
            if (consoleAnim < 1) {
                consoleAnim+=TimeManager.deltaTime/5;
            }
            if (consoleAnim > 1) {
                consoleAnim = 1;
            }
        }
        else {
            if (consoleAnim > 0) {
                consoleAnim-=TimeManager.deltaTime/5;
            }
            if (consoleAnim < 0) {
                consoleAnim = 0;
            }
        }
        if (consoleAnim > 0) {
            push();
            translate(0, -height*(1-consoleAnim));
            console.draw(g);
            pop();
        }
        TimeManager.sync();
        if (frameGraph) {
            framerateGraph.add(TimeManager.frameTime);
            while (framerateGraph.size() > width) {
                framerateGraph.remove(0);
            }
        }
    }

    public void drawDebugText() {
        if (currentScenario.world.time > 1) {
            debugInfo.drawDebugInfo(this, debugText);
        }
    }

    public void drawFramerateCounter() {
        g.push();
        g.fill(0);
        g.noStroke();
        FontManager.quickUse(g, "Arial", 10);
        g.textAlign(PConstants.RIGHT, PConstants.TOP);
        g.text(Math.ceil(1000 / TimeManager.avgFrameTime) + " FPS", g.width, 0);
        g.pop();
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
            scenario.initialized = true;
            drawConsole = false;
        }
        for (int i = debugInfo.components.size()-1; i >= 0; i--) {
            InfoComponent c = debugInfo.components.get(i);
            if (c.clearOnScenario) {
                debugInfo.components.remove(c);
            }
        }
        initializeDebug();
    }

    public void initializeDebug() {}

    public void keyPressed(KeyEvent event) {
        if (key == ESC) {
            if (drawConsole) {
                drawConsole = false;
            }
            key = 0;
        }
        if (drawConsole) {
            console.inputKey(event);
            return;
        }
        super.keyPressed(event);
        input.PressKey(event.getKeyCode());
    }

    public void keyReleased(KeyEvent event) {
        super.keyReleased(event);
        input.ReleaseKey(event.getKeyCode());
    }

}
