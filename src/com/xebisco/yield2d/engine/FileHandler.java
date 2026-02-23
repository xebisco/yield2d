package com.xebisco.yield2d.engine;

import java.io.InputStream;

public abstract class FileHandler extends ApplicationHandler {

    public abstract InputStream openInputStream(String pathname);
    public abstract void closeInputStream(InputStream inputStream);

    @Override
    public void load() {

    }

    @Override
    public void init() {

    }

    @Override
    public void update(TimeSpan elapsed) {

    }

    @Override
    public void fixedUpdate(TimeSpan elapsed) {

    }

    @Override
    public void destroy() {

    }
}
