package com.sulfrix.sulfur.debug.console.commands;

import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.debug.console.ConCommand;
import com.sulfrix.sulfur.debug.console.Console;

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
        }
    }
}
