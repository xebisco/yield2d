package com.xebisco.yield2d.engine;

public class ImmutableVector2i extends Vector2i {
    
    public ImmutableVector2i(int x, int y) {
        super(x, y);
    }

    public ImmutableVector2i() {
        super();
    }

    public ImmutableVector2i(Vector2i vector) {
        super(vector);
    }

    @Override
    public void setX(int x) {
        throw new UnsupportedOperationException("Cannot modify an immutable vector");
    }

    @Override
    public void setY(int y) {
        throw new UnsupportedOperationException("Cannot modify an immutable vector");
    }
}
