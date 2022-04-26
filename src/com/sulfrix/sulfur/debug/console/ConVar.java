package com.sulfrix.sulfur.debug.console;

public class ConVar {
    public String name;
    private String value;
    public boolean saveValue;
    public boolean userMutable = true;
    public String preferredType;
    public String defaultValue;
    public String desc;

    public int cachedInt;
    public float cachedFloat;
    public double cachedDouble;
    public boolean cachedBoolean;

    public boolean cacheValid;

    public ConVar(String name, String defaultValue, String preferredType, String desc) {
        this.name = name;
        this.defaultValue = defaultValue;
        value = defaultValue;
        this.preferredType = preferredType;
        this.desc = desc;
    }

    public ConVar save() {
        saveValue = true;
        return this;
    }

    public int getInt() {
        if (cacheValid) {
            return cachedInt;
        } else {
            int out = 0;
            try {
                out = Integer.parseInt(value);
            } catch (NumberFormatException e) {}
            cachedInt = out;
            cacheValid = true;
            return out;
        }
    }
    public float getFloat() {
        if (cacheValid) {
            return cachedFloat;
        } else {
            float out = 0f;
            try {
                out = Float.parseFloat(value);
            } catch (NumberFormatException e) {}
            cachedFloat = out;
            cacheValid = true;
            return out;
        }

    }
    public double getDouble() {
        if (cacheValid) {
            return cachedDouble;
        } else {
            double out = 0d;
            try {
                out = Double.parseDouble(value);
            } catch (NumberFormatException e) {}
            cacheValid = true;
            cachedDouble = out;
            return out;
        }
    }
    public boolean getBoolean() {
        if (cacheValid) {
            return cachedBoolean;
        } else {
            boolean out = false;
            if (getInt() >= 1) {
                out = true;
            }
            if (value.equalsIgnoreCase("true")) {
                out = true;
            }
            if (getDouble() >= 1) {
                out =  true;
            }
            cachedBoolean = out;
            cacheValid = true;
            return out;
        }

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
        cacheValid = false;
        System.out.println("Convar " + name + " written to " + value);
    }
}
