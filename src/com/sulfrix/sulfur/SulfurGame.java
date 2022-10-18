package com.sulfrix.sulfur;

import com.sulfrix.sulfur.debug.console.ConCommand;
import com.sulfrix.sulfur.debug.console.ConVar;
import com.sulfrix.sulfur.debug.console.Console;
import com.sulfrix.sulfur.debug.DebugInfo;
import com.sulfrix.sulfur.debug.InfoComponent;
import com.sulfrix.sulfur.debug.components.BasicText;
import com.sulfrix.sulfur.debug.components.FPS;
import com.sulfrix.sulfur.debug.console.commands.HelpCommand;
import com.sulfrix.sulfur.entity.Entity;
import com.sulfrix.sulfur.lib.EntityLayer;
import com.sulfrix.sulfur.lib.GlobalManagers.*;
import com.sulfrix.sulfur.lib.input.Input;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.opengl.PGraphicsOpenGL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class SulfurGame extends PApplet {

    public static boolean isAndroid = false;


    public boolean debugText = false;
    public boolean frameGraph = false;
    public boolean drawConsole = false;
    public float consoleAnim = 0;
    public int maxFrameRate = 0;

    public ArrayList<Double> framerateGraph = new ArrayList<>();

    public boolean paused;

    public DisplayMode displayMode;

    Scenario startingScenario;
    public Scenario currentScenario;

    public Input input = new Input();

    public DebugInfo debugInfo;

    public Console console;

    public ArrayList<String> packages = new ArrayList<>();

    public Consumer<SulfurGame> postDraw;

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
        console = new Console(this);
        System.out.println("Sulfur dev");
        System.out.println("Run help_console for help on the console itself");
        System.out.println("Run help for commands/convars");
        engineInitConVars();
        try {
            Console.loadFromFile("config");
        } catch (IOException e) {
            System.out.println("Could not read config.cfg" + e);
        }
        packages.add("com.sulfrix.sulfur");
        packages.add("com.sulfrix.sulfur.entity");
    }

    private void engineInitConVars() {
        Console.addConVar(new ConVar("timescale", "1", "double", "Multiply time by this value."));
        Console.addConVar(new ConVar("cfg_autowrite", "1", "boolean", "Write config on exit."));
        Console.addConVar(new ConVar("console_pause", "0", "boolean", "Pauses the game while the console is open.").save());
        Console.addConVar(new ConVar("console_fontsize", "10", "int", "Console font size.").save());
        Console.addConVar(new ConVar("fps_max", "300", "int", "Maximum FPS.").save());
        Console.addConVar(new ConVar("fullscreen", "0", "boolean", "Enables fullscreen. Applies at launch, saving is required.").save());
        Console.addConVar(new ConVar("ent_culling", "1", "boolean", "Hides entities when offscreen"));
        Console.addConVar(new ConVar("ent_leftdelete", "1", "boolean", "Deletes tiles when offscreen."));
        Console.addConVar(new ConVar("rng_setseed", "0", "int", "When != 0, sets the seed for RNG to use."));
        Console.addConVar(new ConVar("cam_forcescale", "0", "float", "Forces the camera to scale to this value. (when nonzero)"));
        Console.addConVar(new ConVar("cam_debugscale", "1", "float", "Scales everything, shows outside camera bounds."));
        Console.addConCommand(new HelpCommand());
        Console.addConCommand(new ConCommand("clear", (game, args) -> {
            game.console.lines.clear();
        }, "Clears the console"));
        Console.addConCommand(new ConCommand("repeat", (game, args) -> {
            if (args.length >= 2) {
                var command = args[0];
                int repetitions = ConCommand.getInt(args[1]);
                for (int i = 0; i < repetitions; i++) {
                    Console.runCommand(command, false);
                }
            }
        }, "repeats a command n times", new String[]{"string command", "int n"}));
        Console.addConCommand(new ConCommand("console_toggle", (game, args) -> {
            game.drawConsole = !game.drawConsole;
            if (Console.getConVar("console_pause").getBoolean()) {
                if (game.drawConsole) {
                    Console.runCommand("pause");
                } else {
                    Console.runCommand("unpause");
                }
            }

        }, "toggles the console"));
        Console.addConCommand(new ConCommand("pause", (game, args) -> {
            game.paused = true;
        }, "Pauses the game"));
        Console.addConCommand(new ConCommand("unpause", (game, args) -> {
            game.paused = false;
        }, "Unpauses the game"));
        Console.addConCommand(new ConCommand("echo", (game, args) -> {

        }, "writes to console"));
        Console.addConCommand(new ConCommand("console_help", (game, args) -> {
            System.out.println("Console tips:");
            System.out.println("Ctrl+D to clear input box");
            System.out.println("Up & Down Arrows for command history");
        }, "Console usage"));
        Console.addConCommand(new ConCommand("exit", (game, args) -> {
            game.exit();
        }, "Exit the game."));
        Console.addConCommand(new ConCommand("cfg_write", (game, args) -> {
            if (args.length > 0) {
                int forceLevel = 0;
                if (args.length > 1) {
                    forceLevel = ConCommand.getInt(args[1]);
                }
                try {
                    Console.writeToFile(args[0], forceLevel);
                } catch (IOException e) {
                    System.out.println("Could not write. " + e);
                }

            }
        }, "Writes all savable ConVars to a file."));
        Console.addConCommand(new ConCommand("exec", (game, args) -> {
            ConVar autoWrite = Console.getConVar("cfg_autowrite");
            if (autoWrite.getBoolean()) {
                Console.runCommand("cfg_autowrite 0");
                System.out.println("Notice: auto config saving disabled");
            }
            if (args.length > 0) {
                try {
                    Console.loadFromFile(args[0]);
                } catch (IOException e) {
                    System.out.println("File not found. " + e);
                }

            }
        }, "Writes all savable ConVars to a file."));
        Console.addConCommand(new ConCommand("resetall", (game, args) -> {
            Console.resetAll();
        }, "Writes all savable ConVars to a file."));
        Console.addConCommand(new ConCommand("listpackages", (game, args) -> {
            System.out.println("Packages that can be used in console:");
            for (String str : packages) {
                System.out.println(str);
            }
        }, "Lists all packages that can be used in the console."));
        Console.addConCommand(new ConCommand("scenario_load", (game, args) -> {
            if (args.length > 0) {
                var scenarioName = args[0];
                Class scenario = findClassByName(scenarioName, packages.toArray(new String[0]));
                if (scenario!= null && scenario.getSuperclass() == Scenario.class) {
                    try {
                        Scenario scenario1 = (Scenario) scenario.getDeclaredConstructor().newInstance();
                        game.setCurrentScenario(scenario1);
                    } catch (Exception e) {
                        System.out.println("Could not load scenario.");
                    }
                }
            }
        }, "Loads a scenario."));
        Console.addConCommand(new ConCommand("ent_remove", (game, args) -> {
            if (args.length > 0) {
                var entName = args[0].toLowerCase();
                var world = game.currentScenario.world;
                var layers = world.layers;
                for (EntityLayer l : layers.values()) {
                    var entList = l.entities;
                    for (int i = entList.size()-1; i >= 0; i--) {
                        var ent = entList.get(i);
                        if (ent.getClass().getSimpleName().toLowerCase().equals(entName)) {
                            l.RemoveEntityActual(ent);
                        }
                    }
                }

            }
        }, "Deletes all entities of a type."));
        Console.addConCommand(new ConCommand("layer_list", (game, args) -> {
            System.out.println("Current layers: ");
            for (EntityLayer l : currentScenario.world.layers.values()) {
                String attribString = (l.drawEnabled ? "R" : "_") + (l.updateEnabled ? "U" : "_");
                System.out.println(l.drawPriority + " " + l.name + ": " + l.entities.size() + " entities | " + l.flags.toString());
            }
        }, "Lists current layers"));
        Console.addConCommand(new ConCommand("layer_updtoggle", (game, args) -> {
            if (args.length > 0) {
                EntityLayer l = currentScenario.world.GetLayer(args[0]);
                if (l != null) {
                    l.updateEnabled = !l.updateEnabled;
                }
            }
        }, "toggles update for specific layer"));
        Console.addConCommand(new ConCommand("layer_drawtoggle", (game, args) -> {
            if (args.length > 0) {
                EntityLayer l = currentScenario.world.GetLayer(args[0]);
                if (l != null) {
                    l.drawEnabled = !l.drawEnabled;
                }
            }
        }, "toggles drawing for specific layer"));
        initConVars();
    }

    @Override
    public void exit() {
        if (Console.getConVar("cfg_autowrite").getBoolean()) {
            try {
                Console.writeToFile("config", 0);
            } catch (IOException e) {
                System.out.println("Could not write config.cfg");
            }
        }
        super.exit();
    }

    public void initConVars() {}

    private boolean queueWindowChange;
    private DisplayMode targetDisplayMode;

    public double instanceTime = 0;

    public final void settings() {
        if (!Console.getConVar("fullscreen").getBoolean()) {
            if (displayMode != null) {
                displayMode.use(this);
            } else {
                size(480, 360);
            }
        } else {
            fullScreen(P3D);
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
        debugInfo.components.add(new BasicText((game) -> {
            var acc = 0;
            var onscreenAcc = 0;
            for (EntityLayer layer : game.currentScenario.world.layers.values()) {
                acc += layer.entities.size();
                onscreenAcc += layer.entities.stream().filter((entity -> entity.onScreen)).count();
            }
            return "Entities: " + Integer.toString(onscreenAcc) + "/" + Integer.toString(acc);
        }, true));
        setCurrentScenario(startingScenario);
        gameSetup();
    }

    public abstract void gameSetup();

    public final void draw() {
        var fpsconvar = Math.max(30, Console.getConVar("fps_max").getInt());

        if (fpsconvar != maxFrameRate) {
            frameRate(fpsconvar);
            maxFrameRate = fpsconvar;
        }
        background(176, 252, 255);
        g.push();
        var debugscale = Console.getConVar("cam_debugscale").getFloat();
        g.translate((width-(width*debugscale))/2, (height-(height*debugscale))/2);
        g.scale(debugscale);
        ortho();
        input.update(this);
        if (!paused) {
            var ts = TimeManager.calcTimesteps();
            var timescale = Console.getConVar("timescale").getDouble();
            for (int step = 0; step < ts.timesteps; step++) {
                currentScenario.update(ts.deltaTimePerStep*timescale);
                instanceTime += ts.deltaTimePerStep*timescale;
            }
        }

        currentScenario.draw(TimeManager.deltaTime, g);
        g.pop();
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
        if (postDraw != null) {
            postDraw.accept(this);
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
                Console.runCommand("console_toggle");
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

    public static Class<?> findClassByName(String classname, String[] searchPackages) {
        for (String searchPackage : searchPackages) {
            try {
                return Class.forName(searchPackage + "." + classname);
            } catch (ClassNotFoundException e) {
                //not in this package, try another
            }
        }
        //nothing found: return null or throw ClassNotFoundException
        return null;
    }

}
