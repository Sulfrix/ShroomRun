package com.sulfrix.shroomrun;

import com.sulfrix.shroomrun.scenarios.MainScenario;
import com.sulfrix.sulfur.DisplayMode;
import com.sulfrix.sulfur.Scenario;
import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.debug.components.BasicText;
import com.sulfrix.sulfur.debug.components.EntityPosition;
import com.sulfrix.sulfur.debug.console.ConVar;
import com.sulfrix.sulfur.debug.console.Console;
import com.sulfrix.sulfur.lib.input.InputAction;

public class ShroomRun extends SulfurGame {
    public ShroomRun(DisplayMode displayMode, Scenario startingScenario) {
        super(displayMode, startingScenario);
    }

    @Override
    public void gameSetup() {
        input.addAction(new InputAction("jump", () -> input.KeyPressed(32)).addBinding(() -> mousePressed));
        Console.addConVar(new ConVar("shroom_movespeed", "10", "double", "The speed at which the shroom runs."));
        //debugInfo.components.add(new BasicText((game -> "Testing"), true));
    }

    @Override
    public void initializeDebug() {
        debugInfo.components.add(new EntityPosition("Camera", currentScenario.world.camera, true));
        debugInfo.components.add(new EntityPosition("Player", currentScenario.world.camera.focus, true));
    }
}
