package com.xebisco.yield2d.engine;

import java.io.Serializable;

public interface SceneFactory extends Serializable {
    Scene create();

    static SceneFactory empty() {
        return () -> null;
    }
}
