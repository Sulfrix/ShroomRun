package com.sulfrix.sulfur.lib.animation;

import processing.core.PImage;

public class AnimationSequence {
    public String name;
    protected AnimatedSprite owner;
    protected PImage[] frames;
    public float rate;
    public boolean loop;
    protected float timer;

    protected AnimationSequence(String name, float rate, PImage[] frames, boolean loop) {
        this.name = name;
        this.frames = frames;
        this.rate = rate;
        this.loop = loop;
    }

    public static AnimationSequence addToSprite(AnimatedSprite sprite, String name, float rate, PImage[] frames, boolean loop) {
        var out = new AnimationSequence(name, rate, frames, loop);
        out.owner = sprite;
        return out;
    }

    public AnimatedSprite getOwner() {
        return owner;
    }

    public void progress(float time) {
        timer += time*rate;
    }

    public PImage getCurrent() {
        if (loop) {
            return frames[(int)timer%frames.length];
        } else {
            return frames[(int)Math.min(timer, frames.length-1)];
        }

    }

    public float getTimer() {
        return timer;
    }

    public void resetTimer() {
        timer = 0;
    }

    public void setTimer(float value) {
        timer = value;
    }
}
