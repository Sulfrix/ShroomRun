package com.sulfrix.sulfur.debug.console;

public class ConVar {
    public String name;
    private String value;
    public boolean saveValue;
    public boolean userMutable = true;
    public String preferredType;
    public String defaultValue;
    public String desc;

    public ConVar(String name, String defaultValue, String preferredType, String desc) {
        this.name = name;
        this.defaultValue = defaultValue;
        value = defaultValue;
        this.preferredType = preferredType;
        this.desc = desc;
    }

    public int getInt() {
        int out = 0;
        try {
            out = Integer.parseInt(value);
        } catch (NumberFormatException e) {}
        return out;
    }
    public float getFloat() {
        float out = 0f;
        try {
            out = Float.parseFloat(value);
        } catch (NumberFormatException e) {}
        return out;
    }
    public double getDouble() {
        double out = 0d;
        try {
            out = Double.parseDouble(value);
        } catch (NumberFormatException e) {}
        return out;
    }
    public boolean getBoolean() {
        if (getInt() >= 1) {
            return true;
        }
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        if (getDouble() >= 1) {
            return true;
        }
        return false;
    }
    public String getString() {
        return value;
    }

    public String writeCommand() {
        if (!value.contains(" ")) {
            return name + " " + value;
        } else {
            return name + " \"" + value + "\"";
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

}
