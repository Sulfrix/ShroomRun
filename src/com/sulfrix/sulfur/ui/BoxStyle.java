package com.sulfrix.sulfur.ui;

import com.sulfrix.sulfur.lib.Color;

public class BoxStyle {
    public Color backgroundColor = Color.white;
    public Color borderColor = Color.black;
    public float cornerRadius = 0;
    public float borderWidth = 2;

    public BoxStyle(Color backgroundColor, Color borderColor, float cornerRadius, float borderWidth) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.cornerRadius = cornerRadius;
        this.borderWidth = borderWidth;
    }

    public BoxStyle(Color backgroundColor, Color borderColor) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    public BoxStyle() {}
}
