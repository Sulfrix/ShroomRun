package com.sulfrix.sulfur.debug.console.commands;

import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.debug.console.ConCommand;
import com.sulfrix.sulfur.debug.console.ConVar;
import com.sulfrix.sulfur.debug.console.Console;
import com.sulfrix.sulfur.lib.GlobalManagers.Display;
import com.sulfrix.sulfur.lib.GlobalManagers.FontManager;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.Arrays;

public class HelpCommand extends ConCommand {
    public HelpCommand() {
        super("help", (game, args) -> {}, "test");
    }

    @Override
    public void exec(SulfurGame game, String[] args) {
        if (args.length == 0) {
            System.out.println("Valid convars:");
            String[] convarArr = Console.convars.keySet().toArray(new String[0]);
            Arrays.sort(convarArr);
            for (String name : convarArr) {
                System.out.println(name);
                //Console.runCommand(name);
            }
            System.out.println("\nValid commands: ");
            String[] commandArr = Console.concommands.keySet().toArray(new String[0]);
            Arrays.sort(commandArr);
            for (String name : commandArr) {
                System.out.println(name);
            }
        } else {
            if (args[0].equalsIgnoreCase("gui")) {
                game.postDraw = this::drawHelpScreen;
            }
        }

    }

    public PGraphics helpRender;
    public float scroll;

    public void drawHelpScreen(SulfurGame game) {
        PGraphics g = game.g;
        int boxWidth = 300;
        int boxHeight = 300;
        if (helpRender == null) {
            helpRender = game.createGraphics(boxWidth-6, boxHeight-22, PConstants.P3D);

        }
        float scale = (float)Display.getOptimalScale(480, 360);
        g.push();
        // bg box
        g.translate(g.width/2, g.height/2);
        g.scale(scale);
        g.translate(-boxWidth/2, -boxHeight/2);
        g.fill(0);
        g.stroke(0, 255, 0);
        g.strokeWeight(1);
        g.rect(0, 0, boxWidth, boxHeight);
        // title
        g.noStroke();
        g.fill(0, 255, 0);
        FontManager.quickUse(g, "Arial", 10*scale);
        g.textSize(10);
        g.translate(1, 1);
        boxWidth--;
        boxHeight--;
        g.text("Super Secret Sulfur Console Help GUI", 0, 10);
        helpRender.beginDraw();
        helpRender.noSmooth();
        g.getMatrix();
        helpRender.background(0, 255, 0, 50);
        float mouseX = game.mouseX;
        float mouseY = game.mouseY+scroll;
        helpRender.translate(0, -scroll);
        for (ConVar c : Console.convars.values()) {
            helpRender.text(c.name, 0, 10);
            helpRender.translate(0, 10);
        }
        helpRender.endDraw();
        g.image(helpRender, 1, 10);
        g.pop();
    }
}
