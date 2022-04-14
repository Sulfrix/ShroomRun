package com.sulfrix.shroomrun;

import com.sulfrix.shroomrun.scenarios.MainScenario;
import com.sulfrix.sulfur.DisplayMode;
import com.sulfrix.sulfur.SulfurGame;

public class ShroomRun extends SulfurGame {
    public ShroomRun() {
        super(new DisplayMode.Windowed(), new MainScenario());
    }
}
