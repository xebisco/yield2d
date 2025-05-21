package com.xebisco.yield2d.graphics.sprite;

import com.xebisco.yield2d.graphics.paint.IPaint;
import com.xebisco.yield2d.math.Vector2f;

public abstract class AbstractSprite<P extends IPaint> implements ISprite  {
    private final P paint;
    private final Vector2f size;
    private short index;

    public AbstractSprite(P paint, Vector2f size) {
        this.paint = paint;
        this.size = size;
    }

    @Override
    public short getIndex() {
        return index;
    }

    @Override
    public Vector2f getSize() {
        return size;
    }

    public P getPaint() {
        return paint;
    }
}
