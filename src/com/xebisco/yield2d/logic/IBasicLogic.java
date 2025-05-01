package com.xebisco.yield2d.logic;

import com.xebisco.yield2d.mem.ILoadable;

public interface IBasicLogic extends ILoadable {
    default void start() {
        onStart();
    }
    void onStart();

    default void update(float deltaTime) {
        onUpdate(deltaTime);
    }
    void onUpdate(float deltaTime);

    default void lastUpdate(float deltaTime) {
        onLastUpdate(deltaTime);
    }
    void onLastUpdate(float deltaTime);

    default void fixedUpdate(float fixedDeltaTime) {
        onFixedUpdate(fixedDeltaTime);
    }
    void onFixedUpdate(float fixedDeltaTime);
}
