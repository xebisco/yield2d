package com.xebisco.yield2d.engine;

public abstract class InputHandler extends ApplicationHandler {
    public abstract boolean isKeyPressed(Key key);
    public abstract ImmutableVector2f getMouse();
}
