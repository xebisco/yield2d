package com.xebisco.yield2d.engine;

public class Camera extends Script {
    @Editable
    private Color background = new Color(.2f, .2f, .2f);

    public Color getBackground() {
        return background;
    }

    public Camera setBackground(Color background) {
        this.background = background;
        return this;
    }
}
