package com.xebisco.yield2d.mem;

public interface ILoadable {
    default void load() {
        onLoad();
    }
    void onLoad();

    default void unload() {
        onUnload();
    }
    void onUnload();
}
