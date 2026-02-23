package com.xebisco.yield2d.engine.system;

import java.io.Serializable;

public class DataPackage implements Serializable {
    private final int id;
    private final long revision;
    private final Class<?> clazz;

    public DataPackage(int id, long revision, Class<?> clazz) {
        this.id = id;
        this.revision = revision;
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public long getRevision() {
        return revision;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
