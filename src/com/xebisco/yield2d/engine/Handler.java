package com.xebisco.yield2d.engine;

public interface Handler {
    void setContext(Object context);
    void load();
    void init();
    void update(TimeSpan elapsed);

    void fixedUpdate(TimeSpan elapsed);
    void destroy();
}
