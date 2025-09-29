package com.xebisco.yield2d.engine;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Container implements Handler, Comparable<Container>, Drawer {

    private short layer;
    private Object context;
    private final List<Container> children = new ArrayList<>(), toRemove = new ArrayList<>(), toAdd = new ArrayList<>();
    private final Script[] scripts;
    private int frames;
    private Transform2f transform = new Transform2f();
    private boolean loaded;

    public Container(Script[] scripts) {
        this.scripts = scripts;
    }

    @Override
    public void setContext(Object context) {
        this.context = context;
    }

    @Override
    public int compareTo(Container o) {
        return Integer.compare(layer, o.layer);
    }

    @Override
    public void load() {
        frames = 0;
        loaded = true;
        callOnAllScripts(Script::load);
        callOnAllChildren(child -> {
            if (!getToRemove().contains(child))
                child.load();
        });
    }

    @Override
    public void init() {
        if(!loaded) load();
        sortChildren();
        callOnAllScripts(Script::init);
    }

    @Override
    public void update(TimeSpan elapsed) {
        sortChildren();
        callOnAllScripts(script -> script.update(elapsed));
        children.addAll(toAdd);
        toAdd.clear();
        callOnAllChildren(child -> {
            if (!getToRemove().contains(child))
                child.tick(elapsed);
        });
        frames++;
    }

    public void addChild(Container child) {
        toAdd.add(child);
    }

    public void tick(TimeSpan elapsed) {
        if (frames == 0) init();
        update(elapsed);
        getChildren().removeAll(getToRemove());
    }

    @Override
    public void draw(Graphics g) {
        g.start();
        g.translate(getTransform().getPosition().getX(), getTransform().getPosition().getY());
        g.rotate(getTransform().getRotation());
        g.scale(getTransform().getScale().getX(), getTransform().getScale().getY());
        callOnAllScripts(script -> {
            if (script instanceof Drawer drawer) drawer.draw(g);
        });
        callOnAllChildren(child -> {
            if (!getToRemove().contains(child))
                child.draw(g);
        });
        g.end();
    }

    @Override
    public void destroy() {
        loaded = false;
        frames = 0;
        if (getParent() != null) {
            getParent().getToRemove().add(this);
        }
        callOnAllScriptsParallel(Script::destroy);
        callOnAllChildren(Container::destroy);
    }

    public void callOnAllScriptsParallel(Consumer<Script> action) {
        if (scripts != null)
            Stream.of(scripts).parallel().forEach(script -> {
                script.setContext(Container.this);
                action.accept(script);
            });
    }

    public void callOnAllScripts(Consumer<Script> action) {
        if (scripts != null)
            for (Script curr : scripts) {
                curr.setContext(Container.this);
                action.accept(curr);
            }
    }

    public void callOnAllChildren(Consumer<Container> action) {
        try {
            children.forEach(child -> {
                child.setContext(Container.this);
                action.accept(child);
            });
        } catch (ConcurrentModificationException ignore) {
        }
    }

    public void sortChildren() {
        children.sort(Comparator.naturalOrder());
    }

    public <T extends Script> T[] getScripts(Class<T> scriptType, int arraySize) {
        //noinspection unchecked
        T[] result = (T[]) Array.newInstance(scriptType, arraySize);

        int act = 0;
        for (Script curr : scripts) {
            if (scriptType.isAssignableFrom(curr.getClass())) {
                //noinspection unchecked
                result[act] = (T) curr;
                if (++act == arraySize) break;
            }
        }

        return result;
    }

    public <T extends Script> T getScript(Class<T> scriptType, int index) {
        return getScripts(scriptType, index + 1)[index];
    }

    public <T extends Script> T getScript(Class<T> scriptType) {
        return getScript(scriptType, 0);
    }

    public Script[] getScripts() {
        return scripts;
    }

    public Container getParent() {
        if (getContext() instanceof Container) return (Container) getContext();
        else return null;
    }

    public Scene getScene() {
        Object act = getContext();
        while (act instanceof Container && !(act instanceof Scene)) {
            act = ((Container) act).getContext();
        }

        if (act instanceof Scene)
            return (Scene) act;
        else return null;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(short layer) {
        this.layer = layer;
    }

    private Object getContext() {
        return context;
    }

    public List<Container> getChildren() {
        return children;
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public Transform2f getTransform() {
        return transform;
    }

    public Container setTransform(Transform2f transform) {
        this.transform = transform;
        return this;
    }

    public List<Container> getToRemove() {
        return toRemove;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Container setLoaded(boolean loaded) {
        this.loaded = loaded;
        return this;
    }
}
