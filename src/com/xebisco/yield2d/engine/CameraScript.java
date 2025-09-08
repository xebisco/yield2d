package com.xebisco.yield2d.engine;

public class CameraScript extends Script {
    @Editable
    private Color background = new Color(.2f, .2f, .2f);

    public Color getBackground() {
        return background;
    }

    public CameraScript setBackground(Color background) {
        this.background = background;
        return this;
    }
}
