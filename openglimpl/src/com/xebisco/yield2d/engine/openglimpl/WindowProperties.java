package com.xebisco.yield2d.engine.openglimpl;

import com.xebisco.yield2d.engine.Vector2i;

import java.io.Serializable;

public class WindowProperties implements Serializable {

    public enum WindowStyle {
        PLAIN,
        FULLSCREEN,
        UNDECORATED,
        MAXIMIZED
    }

    public enum ViewportStyle {
        STRETCH_TO_FIT,
        SCALE_TO_FIT
    }

    private Vector2i windowSize = new Vector2i(1280, 720), viewportSize = new Vector2i(1280, 720);
    private WindowStyle windowStyle = WindowStyle.PLAIN;
    public ViewportStyle viewportStyle = ViewportStyle.SCALE_TO_FIT;
    private boolean doubleBuffered, verticalSync, resizable = true, createWindow = true, windowIcon = true;

    public Vector2i getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Vector2i windowSize) {
        this.windowSize = windowSize;
    }

    public Vector2i getViewportSize() {
        return viewportSize;
    }

    public void setViewportSize(Vector2i viewportSize) {
        this.viewportSize = viewportSize;
    }

    public WindowStyle getWindowStyle() {
        return windowStyle;
    }

    public void setWindowStyle(WindowStyle windowStyle) {
        this.windowStyle = windowStyle;
    }

    public ViewportStyle getViewportStyle() {
        return viewportStyle;
    }

    public void setViewportStyle(ViewportStyle viewportStyle) {
        this.viewportStyle = viewportStyle;
    }

    public boolean isDoubleBuffered() {
        return doubleBuffered;
    }

    public void setDoubleBuffered(boolean doubleBuffered) {
        this.doubleBuffered = doubleBuffered;
    }

    public boolean isVerticalSync() {
        return verticalSync;
    }

    public void setVerticalSync(boolean verticalSync) {
        this.verticalSync = verticalSync;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isCreateWindow() {
        return createWindow;
    }

    public void setCreateWindow(boolean createWindow) {
        this.createWindow = createWindow;
    }

    public boolean isWindowIcon() {
        return windowIcon;
    }

    public void setWindowIcon(boolean windowIcon) {
        this.windowIcon = windowIcon;
    }
}
