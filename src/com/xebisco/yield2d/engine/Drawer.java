package com.xebisco.yield2d.engine;

public interface Drawer {
    default void adjustCenter(Graphics g, Vector2f size, Center c) {
        Vector2f offset = Utils.offsetCenter(size, c);
        assert offset != null;
        g.translate(offset.getX(), offset.getY());
    }

    enum Center {
        MIDDLE,
        BOTTOM,
        BOTTOM_RIGHT,
        BOTTOM_LEFT,
        TOP,
        TOP_RIGHT,
        TOP_LEFT,
        LEFT,
        RIGHT
    }

    void draw(Graphics graphics);
}
