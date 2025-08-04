package com.xebisco.yield2d.engine;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.function.Consumer;

public abstract class HandlerCollection implements Handler {
    private final HashSet<Handler> handlers = new HashSet<>();

    @Override
    public void load() {
        callAllHandlers(Handler::load);
    }

    @Override
    public void init() {
        callAllHandlers(Handler::init);
    }

    @Override
    public void update(TimeSpan elapsed) {
        callAllHandlers(handler -> handler.update(elapsed));
    }

    @Override
    public void destroy() {
        callAllHandlers(Handler::destroy);
    }

    public void callAllHandlers(Consumer<? super Handler> action) {
        handlers.forEach(handler -> {
            handler.setContext(HandlerCollection.this);
            action.accept(handler);
        });
    }

    public <T extends Handler> T[] getHandlers(Class<T> handlerType, int arraySize) {
        //noinspection unchecked
        T[] result = (T[]) Array.newInstance(handlerType, arraySize);

        int act = 0;
        for(Handler curr : handlers) {
            if(handlerType.isAssignableFrom(curr.getClass())) {
                //noinspection unchecked
                result[act] = (T) curr;
                if(++act == arraySize) break;
            }
        }

        return result;
    }

    public <T extends Handler> T getHandler(Class<T> handlerType, int index) {
        return getHandlers(handlerType, index + 1)[index];
    }

    public <T extends Handler> T getHandler(Class<T> handlerType) {
        return getHandler(handlerType, 0);
    }

    public HashSet<Handler> getHandlers() {
        return handlers;
    }
}
