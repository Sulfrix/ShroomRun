package com.sulfrix.sulfur.lib.animation;

import com.sulfrix.sulfur.lib.GlobalManagers.AssetCache;
import processing.core.PGraphics;
import processing.core.PImage;

import java.util.HashMap;

public class AnimatedSprite {
    protected PImage[] imageFrames;
    public int width;
    public int height;

    public int currentFrame;
    public String currentAnimationName;

    protected HashMap<String, AnimationSequence> animations = new HashMap<>();

    public AnimatedSprite(int width, int height, PImage img) {
        if (img.height == height && img.width % width == 0) {
            int frameCount = img.width/width;
            imageFrames = new PImage[frameCount];
            for (int i = 0; i < frameCount; i++) {
                imageFrames[i] = img.get(i*width, 0, width, height);
            }
        }
    }

    public AnimationSequence addSequence(String name, float rate, PImage[] frames, boolean loop) {
        AnimationSequence seq;
        if (!animations.containsKey(name)) {
            seq = AnimationSequence.addToSprite(this, name, rate, frames, loop);
            animations.put(name, seq);
        } else {
            seq = getAnim(name);
        }
        return seq;
    }

    public AnimationSequence addSequence(String name, float rate, int[] frameIDs, boolean loop) {
        PImage[] frames = new PImage[frameIDs.length];
        for (int i = 0; i < frameIDs.length; i++) {
            frames[i] = imageFrames[frameIDs[i]];
        }
        return addSequence(name, rate, frames, loop);
    }

    public AnimationSequence getAnim(String name) {
        return animations.get(name);
    }

    public AnimationSequence switchAnimation(String name, boolean resetTimers) {
        if (name != currentAnimationName) {
            if (animations.containsKey(name)) {
                var oldAnim = getCurrentAnimation();
                currentAnimationName = name;
                var curr = getCurrentAnimation();
                if (resetTimers) {
                    if (oldAnim != null) {
                        oldAnim.resetTimer();
                    }
                    curr.resetTimer();
                }
                return curr;
            } else {
                System.out.println("Error: attempted to switch to nonexistent animation " + name);
                return getCurrentAnimation();
            }
        } else {
            return getCurrentAnimation();
        }
    }

    public AnimationSequence switchAnimation(String name) {
        return switchAnimation(name, true);
    }

    public AnimationSequence getCurrentAnimation() {
        return getAnim(currentAnimationName);
    }

    public void progress(float time) {
        getCurrentAnimation().progress(time);
    }

    public void setTimer(float value) {
        getCurrentAnimation().setTimer(value);
    }

    public AnimatedSprite(int width, int height, String tex) {
        this(width, height, AssetCache.getImage(tex));
    }

    public void draw(PGraphics g, float x, float y) {
        g.image(getCurrentAnimation().getCurrent(), x, y);
    }
}
