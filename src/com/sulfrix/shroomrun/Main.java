package com.sulfrix.shroomrun;

import com.sulfrix.shroomrun.scenarios.MainScenario;
import com.sulfrix.sulfur.DisplayMode;
import com.sulfrix.sulfur.debug.console.Console;
import processing.core.PApplet;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        runSketch(new ShroomRun(new DisplayMode.Windowed(), new MainScenario()));
        //PApplet.main("com.sulfrix.shroomrun.ShroomRun");
        Scanner input = new Scanner(System.in);

        while (true) {
            String line = input.nextLine();
            Console.runCommand(line, true);
        }
    }

    public static void runSketch(PApplet sketch) {
        PApplet.runSketch(new String[] {"ShroomRun"}, sketch);
    }
}
