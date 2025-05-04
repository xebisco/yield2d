package com.xebisco.yield2d.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.xebisco.yield2d.logic.IBasicLogic;

public class LoopContext implements Runnable {

    private final Thread thread;
    private final AtomicBoolean running = new AtomicBoolean(), paused = new AtomicBoolean();
    private final Object pauseLock = new Object();
    private final List<Runnable> shutdownHooks = new ArrayList<>();
    private final IBasicLogic basicLogic;
    private long timeSinceStart, targetSleepTime = 16_666_667;
    private float deltaTime;

    /**
     * Constructs a new LoopContext object.
     *
     * @param name The name of the thread.
     */
    public LoopContext(String name, IBasicLogic basicLogic) {
        thread = new Thread(this, "Yield LoopContext: " + name);
        this.basicLogic = basicLogic;
    }

    @Override
    public void run() {
        running.set(true);
        long last = System.nanoTime(), actual;
        long value = 0;
        basicLogic.start();
        setTimeSinceStart(last);
        while (running.get()) {
            if (value > 0) {
                do {
                    actual = System.nanoTime();
                } while (actual - last < getTargetSleepTime());
            } else {
                actual = System.nanoTime();
            }

            if (paused.get()) {
                paused.set(false);
                synchronized (pauseLock) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            setDeltaTime((actual - last) / 1_000_000_000f);

            basicLogic.firstUpdate(getDeltaTime());
            basicLogic.update(getDeltaTime());
            basicLogic.lastUpdate(getDeltaTime());

            last = actual;
            actual = System.nanoTime();

            value = getTargetSleepTime() - 1_000_000 - ((actual - last) / 1_000_000);
            if (value > 0) {
                try {
                    Thread.sleep(value / 1_000_000, (int) (value - (value / 1_000_000 * 1_000_000)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Runnable shutdownHook : shutdownHooks) {
            shutdownHook.run();
        }
    }

    public void startThread() {
        getThread().start();
    }

    public void pause() {
        paused.set(true);
    }

    public void resume() {
        paused.set(false);
        synchronized (pauseLock) {
            pauseLock.notify();
        }
    }

    public Thread getThread() {
        return thread;
    }

    public AtomicBoolean getRunning() {
        return running;
    }

    public AtomicBoolean getPaused() {
        return paused;
    }

    public Object getPauseLock() {
        return pauseLock;
    }

    public List<Runnable> getShutdownHooks() {
        return shutdownHooks;
    }

    public IBasicLogic getBasicLogic() {
        return basicLogic;
    }

    public long getTimeSinceStart() {
        return timeSinceStart;
    }

    private void setTimeSinceStart(long timeSinceStart) {
        this.timeSinceStart = timeSinceStart;
    }

    public long getTargetSleepTime() {
        return targetSleepTime;
    }

    public void setTargetSleepTime(long targetSleepTime) {
        this.targetSleepTime = targetSleepTime;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    private void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }
}