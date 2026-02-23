package com.xebisco.yield2d.engine;

import java.io.Serializable;

public interface ContainerFactory extends Serializable {
    Container create();

    static ContainerFactory empty() {
        return () -> null;
    }
}
