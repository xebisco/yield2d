package com.xebisco.yield2d.engine;

import java.util.concurrent.atomic.AtomicBoolean;

public class AWaitCall {

    private final Object lock = new Object();
    private AtomicBoolean done = new AtomicBoolean(false);

    public void await(long timeout) {
        synchronized (lock) {
            if (!done.get()) {
                try {
                    lock.wait(timeout);
                } catch (InterruptedException e) {
                   throw new RuntimeException("Thread interrupted", e);
                }
            }
        }
    }

    public void call() {
        done.set(true);
        
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void reset() {
        synchronized (lock) {
            done.set(false);
        }
    }

    public boolean isDone() {
        return done.get();
    }
}
