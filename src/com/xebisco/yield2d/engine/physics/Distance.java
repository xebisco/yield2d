package com.xebisco.yield2d.engine.physics;

import com.xebisco.yield2d.engine.Vector2f;

public class Distance {
    private final Vector2f pointA;
    private final Vector2f pointB;
    private final float distance;

    public Distance(Vector2f pointA, Vector2f pointB, float distance) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.distance = distance;
    }

    public Vector2f getPointA() {
        return pointA;
    }

    public Vector2f getPointB() {
        return pointB;
    }

    public float getDistance() {
        return distance;
    }
}
