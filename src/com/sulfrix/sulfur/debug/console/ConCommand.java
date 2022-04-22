package com.sulfrix.sulfur.debug.console;

import com.sulfrix.sulfur.SulfurGame;

public class ConCommand {
    public String name;
    public String[] arguments;
    public boolean userAvailable = true;
    public CommandAction action;
    public String desc;

    public ConCommand(String name, CommandAction action, String desc, String[] arguments) {
        this.name = name;
        this.action = action;
        this.desc = desc;
        this.arguments = arguments;
    }

    public ConCommand(String name, CommandAction action, String desc) {
        this(name, action, desc, new String[] {""});
    }

    public void exec(SulfurGame game, String[] args) {
        action.exec(game, args);
    }

    public interface CommandAction {
        void exec(SulfurGame game, String[] args);
    }

    public static int getInt(String value) {
        int out = 0;
        try {
            out = Integer.parseInt(value);
        } catch (NumberFormatException e) {}
        return out;
    }
    public static float getFloat(String value) {
        float out = 0f;
        try {
            out = Float.parseFloat(value);
        } catch (NumberFormatException e) {}
        return out;
    }
    public static double getDouble(String value) {
        double out = 0d;
        try {
            out = Double.parseDouble(value);
        } catch (NumberFormatException e) {}
        return out;
    }
    public static boolean getBoolean(String value) {
        if (getInt(value) >= 1) {
            return true;
        }
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        if (getDouble(value) >= 1) {
            return true;
        }
        return false;
    }
}
