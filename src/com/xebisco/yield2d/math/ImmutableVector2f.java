package com.xebisco.yield2d.math;

public class ImmutableVector2f extends Vector2f {
    
    public ImmutableVector2f(float x, float y) {
        super(x, y);
    }

    public ImmutableVector2f() {
        super();
    }

    public ImmutableVector2f(IVector2f vector) {
        super(vector);
    }

    public ImmutableVector2f(IVector2i vector) {
        super(vector);
    }

    @Override
    public void setX(float x) {
        throw new UnsupportedOperationException("Cannot modify an immutable vector");
    }

    @Override
    public void setY(float y) {
        throw new UnsupportedOperationException("Cannot modify an immutable vector");
    }
}
