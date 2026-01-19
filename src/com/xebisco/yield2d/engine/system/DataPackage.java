package com.xebisco.yield2d.engine.system;

import java.io.Serializable;

public record DataPackage(int id, long revision, Class<?> clazz) implements Serializable {
}
