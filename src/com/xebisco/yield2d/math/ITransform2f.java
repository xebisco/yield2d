package com.xebisco.yield2d.math;

import java.io.Serializable;

public interface ITransform2f extends Serializable {
    enum Origin {
        TOP_LEFT,
        CENTER
    }

    void translate(IVector2f translation);

    void scale(IVector2f scale);

    void rotate(float angle);

    void reset();

    IVector2f getPosition();

    IVector2f getScale();

    float getRotation();

    Transform2f.Origin getOrigin();

    void setOrigin(ITransform2f.Origin origin);
}
