package com.xebisco.yield2d.engine;

public class MouseDragAndZoomScript extends Script {
    private Vector2f lastClickedPosition = new Vector2f();

    private boolean clicking;

    @Override
    public void update(TimeSpan elapsed) {
        if(!clicking && getAxisValue("") > 0) {
            clicking = true;
        }
    }
}
