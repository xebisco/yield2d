package com.xebisco.yield2d.math;

import java.io.Serializable;

public interface IVector2f extends Serializable {
    float getX();

    float getY();

    void reset();

    IVector2f add(IVector2f vector);

    void addLocal(IVector2f vector);

    IVector2f subtract(IVector2f vector);
    
    void subtractLocal(IVector2f vector);

    IVector2f multiply(float scalar);

    void multiplyLocal(float scalar);

    IVector2f divide(float scalar);

    void divideLocal(float scalar);

    float dot(IVector2f vector);

    IVector2f normalize();

    void normalizeLocal();

    float angle(IVector2f vector);

    default float angle() {
        return angle(new Vector2f(1, 0));
    }

    float distance(IVector2f vector);

    default float norm() {
        return distance(new Vector2f(0, 0));
    }

    IVector2f rotate(float angle);

    void rotateLocal(float angle);
}
