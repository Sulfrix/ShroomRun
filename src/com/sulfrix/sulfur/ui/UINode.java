package com.sulfrix.sulfur.ui;

import java.util.List;

public class UINode {
    public float innerWidth;
    public float innerHeight;

    public PositionType positionType = PositionType.Layout;

    public UINode parent;

    public List<UINode> children;


}
