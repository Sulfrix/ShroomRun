package com.sulfrix.sulfur.lib.animation;

import com.sulfrix.sulfur.lib.GlobalManagers.AssetCache;
import processing.core.PImage;

import java.util.HashMap;

public class AtlasSprite {
    public PImage[] images;
    public int width;
    public int height;

    public static HashMap<String, AtlasSprite> cache = new HashMap<>();

    public AtlasSprite(PImage source, int width) {
        if (source.width % width != 0) {
            throw new RuntimeException("Invalid atlas sprite: " + source.width % width);
        }
        int count = source.width/width;
        images = new PImage[count];
        for (int i = 0; i < count; i++) {
            images[i] = source.get(i*width, 0, width, source.height);
        }
    }

    public static AtlasSprite use(String file, int width) {
        if (cache.containsKey(file)) {
            return cache.get(file);
        } else {
            var create = new AtlasSprite(AssetCache.getImage(file), width);
            cache.put(file, create);
            return create;
        }
    }

    public static AtlasSprite use(String file) {
        return cache.getOrDefault(file, null);
    }
}
