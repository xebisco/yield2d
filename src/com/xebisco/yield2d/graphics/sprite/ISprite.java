package com.xebisco.yield2d.graphics.sprite;

import com.xebisco.yield2d.impl.Connection;
import com.xebisco.yield2d.math.Vector2f;
import com.xebisco.yield2d.utils.IIndexable;

public interface ISprite extends IIndexable {
    void draw(Connection graphConn);
    Vector2f getSize();
}
