package com.sulfrix.shroomrun.lib.GlobalManagers;

import processing.core.PImage;

import java.util.HashMap;

public abstract class AssetCache extends GlobalManager {
    static HashMap<String, PImage> images = new HashMap<String, PImage>();

    public static PImage getImage(String name) {
        if (images.containsKey(name)) {
            return images.get(name);
        } else {
            PImage load = owner.loadImage(name);
            images.put(name, load);
            return load;
        }
    }
}
