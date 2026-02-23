package com.xebisco.yield2d.engine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xebisco.yield2d.engine.physics.PhysicsHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Application extends HandlerCollection {

    public enum Type {
        GAME(new Class[]{PhysicsHandler.class}),
        EMPTY(new Class[]{});
        private final Class<? extends Handler>[] defaultHandlers;


        Type(Class<? extends Handler>[] defaultHandlers) {
            this.defaultHandlers = defaultHandlers;
        }

        public Class<? extends Handler>[] getDefaultHandlers() {
            return defaultHandlers;
        }
    }

    private ApplicationLoop loop, fixedLoop;

    private final Type type;
    private String title = "Yield App";
    private final TimeSpan createdTime = new TimeSpan(System.nanoTime());

    @Override
    public void init() {
        super.init();
        if (getSceneHandler().getActualScene() == null) {
            Debug.println("WARNING: ActualScene is null.");
        }

        Debug.println("Starting application " + this + "...");
        loop = new ApplicationLoop(false, new TimeSpan(16_666_667));
        fixedLoop = new ApplicationLoop(true, new TimeSpan(16_666_667));
        Debug.println(this + " started.");
    }
    private Map<String, Axis> axisMap;
    private boolean destroyed;

    public Application(Type type) {
        this.type = type;
    }

    public boolean checkIfHasGraphicsHandler() {
        return getGraphicsHandler() != null;
    }

    public boolean checkIfHasInputHandler() {
        return getInputHandler() != null;
    }

    public boolean checkIfHasAudioHandler() {
        return getAudioHandler() != null;
    }

    public boolean checkIfHasFileHandler() {
        return getFileHandler() != null;
    }

    @Override
    public void setContext(Object context) {

    }

    @Override
    public void load() {
        Debug.println("Loading application " + this + "...");
        getHandlers().add(new SceneHandler());

        for (Class<? extends Handler> defHandlerType : getType().getDefaultHandlers()) {
            try {
                getHandlers().add(defHandlerType.getConstructor().newInstance());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        super.load();

        axisMap = new HashMap<>();

        try {
            java.lang.reflect.Type mapType = new TypeToken<Map<String, Axis>>() {
            }.getType();
            Gson gson = new Gson();
            Map<String, Axis> axis = gson.fromJson(Utils.readStringFromClasspath("default_axis.json"), mapType);
            axisMap.putAll(axis);

            try {
                InputStream customAxisInputStream = getFileHandler().openInputStream("axis.json");
                axis = gson.fromJson(Utils.readStringFromInputStream(customAxisInputStream), mapType);
                axisMap.putAll(axis);
                getFileHandler().closeInputStream(customAxisInputStream);
            } catch (NullPointerException e) {
                Debug.println("WARNING: Couldn't find 'axis.json'.");
            }

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        Debug.println(this + " loaded.");
    }

    @Override
    public void destroy() {
        loop.running.set(false);
        fixedLoop.running.set(false);
        if (!destroyed) {
            destroyed = true;
            super.destroy();
        }
    }

    public TextureAtlasFile loadTextureAtlas(String textureAtlasFilePath) {
        TextureAtlasFile textureAtlasFile = new TextureAtlasFile(textureAtlasFilePath);
        Gson gson = new Gson();
        FileHandler fileHandler = getFileHandler();
        InputStream is = fileHandler.openInputStream(textureAtlasFilePath);
        textureAtlasFile.setTextureAtlasInfo(gson.fromJson(new InputStreamReader(is), TextureAtlasFile.TextureAtlas.class));
        fileHandler.closeInputStream(is);

        getGraphicsHandler().loadTextureAtlasInfo(textureAtlasFile.getTextureAtlasInfo());
        return textureAtlasFile;
    }

    public float getAxisValue(String axisKey) {
        if (!getAxisMap().containsKey(axisKey)) throw new IllegalArgumentException("Axis doesn't exist.");
        return getAxisMap().get(axisKey).getValue(getInputHandler());
    }

    public TimeSpan getTimeSinceCreation() {
        return new TimeSpan(System.nanoTime() - getCreatedTime().getNanoSeconds());
    }

    public ApplicationLoop getFixedLoop() {
        return fixedLoop;
    }

    public TimeSpan getCreatedTime() {
        return createdTime;
    }

    public SceneHandler getSceneHandler() {
        return getHandler(SceneHandler.class);
    }

    public GraphicsHandler getGraphicsHandler() {
        return getHandler(GraphicsHandler.class);
    }

    public void setGraphicsHandler(GraphicsHandler graphicsHandler) {
        if (checkIfHasGraphicsHandler()) {
            throw new IllegalStateException("Application already has a GraphicsHandler.");
        }
        getHandlers().add(graphicsHandler);
    }

    public InputHandler getInputHandler() {
        return getHandler(InputHandler.class);
    }

    public void setInputHandler(InputHandler inputHandler) {
        if (checkIfHasInputHandler()) {
            throw new IllegalStateException("Application already has a InputHandler.");
        }
        getHandlers().add(inputHandler);
    }

    public AudioHandler getAudioHandler() {
        return getHandler(AudioHandler.class);
    }

    public void setAudioHandler(AudioHandler AudioHandler) {
        if (checkIfHasAudioHandler()) {
            throw new IllegalStateException("Application already has a AudioHandler.");
        }
        getHandlers().add(AudioHandler);
    }

    public FileHandler getFileHandler() {
        return getHandler(FileHandler.class);
    }

    public void setFileHandler(FileHandler FileHandler) {
        if (checkIfHasFileHandler()) {
            throw new IllegalStateException("Application already has a FileHandler.");
        }
        getHandlers().add(FileHandler);
    }

    public Map<String, Axis> getAxisMap() {
        return axisMap;
    }

    public void setAxisMap(Map<String, Axis> axisMap) {
        this.axisMap = axisMap;
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Application setFixedLoop(ApplicationLoop fixedLoop) {
        this.fixedLoop = fixedLoop;
        return this;
    }

    public class ApplicationLoop implements Runnable {
        private final Thread thread;
        private final AtomicBoolean running = new AtomicBoolean();
        private final boolean fixedDelta;
        private TimeSpan updateInterval;

        public ApplicationLoop(boolean fixedDelta, TimeSpan updateInterval) {
            this.fixedDelta = fixedDelta;
            this.updateInterval = updateInterval;
            thread = new Thread(this, "Yield Application Loop");
            thread.start();
        }

        @Override
        public void run() {
            running.set(true);
            long last = System.nanoTime(), actual;
            long value = 0;
            while (running.get()) {
                if (value > 0) {
                    do {
                        actual = System.nanoTime();
                    } while (actual - last < updateInterval.getNanoSeconds());
                } else {
                    actual = System.nanoTime();
                }

                if (fixedDelta)
                    fixedUpdate(updateInterval);
                else {
                    update(new TimeSpan(actual - last));

                    render();
                }

                last = actual;
                actual = System.nanoTime();

                value = updateInterval.getNanoSeconds() - 1_000_000 - ((actual - last) / 1_000_000);
                if (value > 0) {
                    try {
                        //noinspection BusyWait
                        Thread.sleep(value / 1_000_000, (int) (value - (value / 1_000_000 * 1_000_000)));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            destroy();
            thread.interrupt();
        }

        public Thread getThread() {
            return thread;
        }

        public AtomicBoolean getRunning() {
            return running;
        }

        public boolean isFixedDelta() {
            return fixedDelta;
        }

        public TimeSpan getUpdateInterval() {
            return updateInterval;
        }

        public ApplicationLoop setUpdateInterval(TimeSpan updateInterval) {
            this.updateInterval = updateInterval;
            return this;
        }
    }

    public ApplicationLoop getLoop() {
        return loop;
    }

    public void setLoop(ApplicationLoop loop) {
        this.loop = loop;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Application setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
        return this;
    }
}
