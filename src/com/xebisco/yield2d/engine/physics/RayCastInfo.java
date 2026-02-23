package com.xebisco.yield2d.engine.physics;

import com.xebisco.yield2d.engine.Vector2f;

import java.util.Objects;

public class RayCastInfo {
    private final Vector2f normal;
    private final float fraction;

    public RayCastInfo(Vector2f normal, float fraction) {
        this.normal = normal;
        this.fraction = fraction;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RayCastInfo that = (RayCastInfo) o;
        return Float.compare(fraction, that.fraction) == 0 && Objects.equals(normal, that.normal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(normal, fraction);
    }

    public Vector2f getNormal() {
        return normal;
    }

    public float getFraction() {
        return fraction;
    }
}
