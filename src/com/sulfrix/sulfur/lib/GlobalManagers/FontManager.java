package com.sulfrix.sulfur.lib.GlobalManagers;

import processing.core.PFont;
import processing.core.PGraphics;

import java.util.HashMap;

public abstract class FontManager extends GlobalManager {

    private static HashMap<String, PFont> fontCache = new HashMap<>(); // "contains size prefix, like "Arial_32"
    public static String defaultFont;

    public static PFont useFont(String name, int size) {
        String fontName = formatSize(name, size);
        if (fontCache.containsKey(fontName)) {
            return fontCache.get(fontName);
        }
        else {
            //System.out.println("Loading font " + name + " at size " + size);
            PFont newFont = owner.createFont(name, size);
            fontCache.put(fontName, newFont);
            return newFont;
        }
    }

    public static PFont useFont(String name, float size) {
        return useFont(name, (int)size);
    }

    public static void quickUse(PGraphics g, String name, float size) {
        g.textFont(useFont(name, size), size);
    }

    public static boolean fontCached(String name, int size) {
        return fontCache.containsKey(formatSize(name, size));
    }

    public static String formatSize(String name, int size) {
        return name + "_" + size;
    }
}
