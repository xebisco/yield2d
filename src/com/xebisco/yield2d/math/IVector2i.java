package com.xebisco.yield2d.math;

import java.io.Serializable;

public interface IVector2i extends Serializable {
    int getX();

    int getY();

    void reset();

    IVector2i add(IVector2i vector);

    void addLocal(IVector2i vector);

    IVector2i subtract(IVector2i vector);
    
    void subtractLocal(IVector2i vector);

    IVector2i multiply(int scalar);

    void multiplyLocal(int scalar);

    IVector2i divide(int scalar);

    void divideLocal(int scalar);

    int dot(IVector2i vector);

    float angle(IVector2i vector);

    default float angle() {
        return angle(new Vector2i(1, 0));
    }

    float distance(IVector2i vector);

    default float norm() {
        return distance(new Vector2i(0, 0));
    }
}