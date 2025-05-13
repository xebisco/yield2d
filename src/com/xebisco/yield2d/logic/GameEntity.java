package com.xebisco.yield2d.logic;

import java.util.ArrayList;

import com.xebisco.yield2d.Global;
import com.xebisco.yield2d.math.ITransform2f;
import com.xebisco.yield2d.math.Transform2f;

public class GameEntity extends EntityHandler implements ILayerable {

    private short layer;
    private final IEntityProcessor entityProcessor;
    private final ArrayList<GameComponent> components = new ArrayList<>();
    private ITransform2f transform = new Transform2f();

    public GameEntity(IEntityProcessor entityProcessor) {
        this.entityProcessor = entityProcessor;
    }

    @Override
    public void onLoad() {
        Global.RESET_ENTITY_PROCESSOR.process(this);
        if(entityProcessor != null) {
            entityProcessor.process(this);
        }
        Global.parallelFor(components, c -> c.load());
        super.onLoad();
    }

    @Override
    public void onUnload() {
        Global.parallelFor(components, c -> c.unload());
        super.onUnload();
    }

    @Override
    public void onStart() {
        Global.parallelFor(components, c -> c.start());
        super.onStart();
    }

    @Override
    public void onFirstUpdate(float deltaTime) {
        Global.parallelFor(components, c -> c.firstUpdate(deltaTime));
        super.onFirstUpdate(deltaTime);
    }

    @Override
    public void onUpdate(float deltaTime) {
        Global.parallelFor(components, c -> c.update(deltaTime));
        super.onUpdate(deltaTime);
    }

    @Override
    public void onLastUpdate(float deltaTime) {
        Global.parallelFor(components, c -> c.lastUpdate(deltaTime));
        super.onLastUpdate(deltaTime);
    }

    @Override
    public void onFixedUpdate(float fixedDeltaTime) {
        Global.parallelFor(components, c -> c.onFixedUpdate(fixedDeltaTime));
        super.onFixedUpdate(fixedDeltaTime);
    }

    public <T extends GameComponent> T[] getComponents(Class<T> componentsType) {
        ArrayList<T> result = new ArrayList<>();
        T act = null, comp;
        
        int i = 0;
        do {
            comp = act;
            act = getComponent(componentsType, i++);
            if(act != null) result.add(act);
        } while(act != comp);

        return (T[]) result.toArray();
    }

    public <T extends GameComponent> boolean contains(Class<T> componentType) {
        return getComponent(componentType) != null;
    }

    public <T extends GameComponent> T getComponent(Class<T> componentType) {
        return getComponent(componentType, 0);
    }

    public <T extends GameComponent> T getComponent(Class<T> componentType, int index) {
        T result = null;

        int i = 0;
        for(GameComponent c : getComponents()) {
            if(componentType.isInstance(c)) {
                result = (T) c;
                if(index == i) break;
                i++;
            }
        }

        return result;
    }

    @Override
    public short getLayer() {
        return layer;
    }

    public void setLayer(short layer) {
        this.layer = layer;
    }

    public ArrayList<GameComponent> getComponents() {
        return components;
    }

    public IEntityProcessor getEntityProcessor() {
        return entityProcessor;
    }

    public ITransform2f getTransform() {
        return transform;
    }

    public void setTransform(ITransform2f transform) {
        this.transform = transform;
    }
}
