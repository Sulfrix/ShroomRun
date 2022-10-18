package com.sulfrix.sulfur.lib;

import processing.core.PVector;

public class BoundingBox {
    public float width;
    public float height;

    public PVector offset = new PVector(0, 0);

    public BoundingBox(float w, float h) {
        width = w;
        height = h;
    }

    public static boolean touching(BoundingBox b1, PVector p1, BoundingBox b2, PVector p2) {
        /*double x1 = p1.x + b1.offset.x;
        double y1 = p1.y + b1.offset.y;
        double x2 = p2.x + b2.offset.x;
        double y2 = p2.y + b2.offset.y;
        return (x1 < x2 + b2.width &&
                x1 + b1.width > x2 &&
                y1 < y2 + b2.height &&
                y1 + b1.height > y2);*/
        PVector mins1 = b1.getMins(p1);
        PVector maxs1 = b1.getMaxs(p1);
        PVector mins2 = b2.getMins(p2);
        PVector maxs2 = b2.getMaxs(p2);


        return ((mins1.x < maxs2.x && maxs1.x > mins2.x) && (mins1.y < maxs2.y && maxs1.y > mins2.y));

    }

    public static BoundingBox zero() {
        return new BoundingBox(0, 0);
    }

    public static boolean pointTouch(BoundingBox bb, PVector pos, PVector point) {
        PVector mins = bb.getMins(pos);
        PVector maxs = bb.getMaxs(pos);

        return ((point.x > mins.x && point.x < maxs.x) &&
                (point.y > mins.y && point.y < maxs.y));
    }

    public boolean boxIsLeftOf(BoundingBox other, PVector otherPos, PVector thisPos) {
        var rightBox = getMins(thisPos);
        var leftBox = other.getMaxs(otherPos);
        return leftBox.x < rightBox.x;
    }

    public boolean pointTouch(PVector pos, PVector point) {
        return BoundingBox.pointTouch(this, pos, point);
    }

    public boolean touching(PVector p1, BoundingBox b2, PVector p2) {
        return BoundingBox.touching(this, p1, b2, p2);
    }

    public PVector getMins(PVector pos) {
        float xmax = (pos.x + offset.x) - width / 2;
        float ymax = (pos.y + offset.y) - height / 2;
        return new PVector(xmax, ymax);
    }

    public PVector getMaxs(PVector pos) {
        float xmax = (pos.x + offset.x) + width / 2;
        float ymax = (pos.y + offset.y) + height / 2;
        return new PVector(xmax, ymax);
    }

    public BoundingBox copy() {
        var out = new BoundingBox(width, height);
        out.offset = this.offset;
        return out;
    }

    public BoundingBox scale(float mult) {
        width *= mult;
        height *= mult;
        return this;
    }
}
