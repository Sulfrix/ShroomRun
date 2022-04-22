package com.sulfrix.sulfur.debug.console.commands;

import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.debug.console.ConCommand;

public class HelpCommand extends ConCommand {
    public HelpCommand(String name) {
        super(name, (game, args) -> {}, "test");
    }

    @Override
    public void exec(SulfurGame game, String[] args) {

    }
}
