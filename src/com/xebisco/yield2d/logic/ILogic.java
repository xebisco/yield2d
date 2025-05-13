package com.xebisco.yield2d.logic;

import com.xebisco.yield2d.mem.ILoadable;

public interface ILogic extends ILoadable {
    default void start() {
        onStart();
    }
    void onStart();

    default void firstUpdate(float deltaTime) {
        onFirstUpdate(deltaTime);
    }

    void onFirstUpdate(float deltaTime);

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
