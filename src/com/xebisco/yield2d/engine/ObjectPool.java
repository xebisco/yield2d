package com.xebisco.yield2d.engine;

public interface ObjectPool<T extends Poolable> {
    T acquire();

    void release(T obj);
}
