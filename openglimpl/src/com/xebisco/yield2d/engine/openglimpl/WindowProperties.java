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
        FIT_ON_FRAME,
        STRETCH_TO_FIT,
        SCALE_TO_FIT
    }

    private Vector2i windowSize = new Vector2i(1280, 720), viewportSize = new Vector2i(1280, 720);
    private WindowStyle windowStyle = WindowStyle.PLAIN;
    public ViewportStyle viewportStyle = ViewportStyle.SCALE_TO_FIT;
    private boolean doubleBuffered = true, verticalSync, resizable = true, createWindow = true, windowIcon = true;

    public Vector2i getWindowSize() {
        return windowSize;
    }

    public WindowProperties setWindowSize(Vector2i windowSize) {
        this.windowSize = windowSize;
        return this;
    }

    public Vector2i getViewportSize() {
        return viewportSize;
    }

    public WindowProperties setViewportSize(Vector2i viewportSize) {
        this.viewportSize = viewportSize;
        return this;
    }

    public WindowStyle getWindowStyle() {
        return windowStyle;
    }

    public WindowProperties setWindowStyle(WindowStyle windowStyle) {
        this.windowStyle = windowStyle;
        return this;
    }

    public ViewportStyle getViewportStyle() {
        return viewportStyle;
    }

    public WindowProperties setViewportStyle(ViewportStyle viewportStyle) {
        this.viewportStyle = viewportStyle;
        return this;
    }

    public boolean isDoubleBuffered() {
        return doubleBuffered;
    }

    public WindowProperties setDoubleBuffered(boolean doubleBuffered) {
        this.doubleBuffered = doubleBuffered;
        return this;
    }

    public boolean isVerticalSync() {
        return verticalSync;
    }

    public WindowProperties setVerticalSync(boolean verticalSync) {
        this.verticalSync = verticalSync;
        return this;
    }

    public boolean isResizable() {
        return resizable;
    }

    public WindowProperties setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public boolean isCreateWindow() {
        return createWindow;
    }

    public WindowProperties setCreateWindow(boolean createWindow) {
        this.createWindow = createWindow;
        return this;
    }

    public boolean isWindowIcon() {
        return windowIcon;
    }

    public WindowProperties setWindowIcon(boolean windowIcon) {
        this.windowIcon = windowIcon;
        return this;
    }
}
