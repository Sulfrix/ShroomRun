package com.sulfrix.shroomrun;

import com.sulfrix.shroomrun.scenarios.MainScenario;
import com.sulfrix.sulfur.DisplayMode;
import com.sulfrix.sulfur.Scenario;
import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.lib.input.InputAction;

public class ShroomRun extends SulfurGame {
    public ShroomRun(DisplayMode displayMode, Scenario startingScenario) {
        super(displayMode, startingScenario);
    }

    @Override
    public void gameSetup() {
        input.addAction(new InputAction("jump", () -> input.KeyPressed(32)).addBinding(() -> mousePressed));
    }
}
