package com.sulfrix.sulfur.lib;

// Allows for simple optimizations (e.g. only collision check a certain layer)
// Some of these flags don't have any real effect (static doesn't actually make it static, just helps)
public enum LayerFlag {
    STATIC,
    NOCOLLIDE,
    ACTOR
}
