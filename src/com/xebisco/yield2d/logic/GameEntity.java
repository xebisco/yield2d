package com.xebisco.yield2d.logic;

import java.util.ArrayList;

import com.xebisco.yield2d.Global;
import com.xebisco.yield2d.math.ITransform2f;

public class GameEntity extends EntityHandler implements ILayerable {

    private short layer;
    private final IEntityProcessor entityProcessor;
    private final ArrayList<GameComponent> components = new ArrayList<>();
    private ITransform2f transform;

    public GameEntity(IEntityProcessor entityProcessor) {
        this.entityProcessor = entityProcessor;
    }

    @Override
    public void onLoad() {
        if(entityProcessor != null) {
            Global.RESET_ENTITY_PROCESSOR.process(this);
            entityProcessor.process(this);
        }
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
