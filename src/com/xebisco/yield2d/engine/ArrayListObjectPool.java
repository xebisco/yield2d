package com.xebisco.yield2d.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ArrayListObjectPool<T extends Poolable> implements ObjectPool<T> {
    private final List<T> freeObjects;
    private final Supplier<T> factory;
    private final int maxSize;

    public ArrayListObjectPool(Supplier<T> factory, int initialSize) {
        this.factory = factory;
        this.freeObjects = new ArrayList<>(initialSize);
        this.maxSize = initialSize * 2;

        for (int i = 0; i < initialSize; i++) {
            freeObjects.add(factory.get());
        }
    }

    @Override
    public T acquire() {
        if (freeObjects.isEmpty()) {
            return factory.get(); 
        }
        return freeObjects.remove(freeObjects.size() - 1);
    }

    @Override
    public void release(T obj) {
        if (obj == null) return;

        if (freeObjects.size() < maxSize) {
            obj.reset();
            freeObjects.add(obj);
        }
    }
}