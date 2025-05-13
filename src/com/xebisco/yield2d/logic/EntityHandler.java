package com.xebisco.yield2d.logic;

import java.util.ArrayList;
import java.util.Comparator;

import com.xebisco.yield2d.Global;

public abstract class EntityHandler extends AbstractLogic {
    private final ArrayList<GameEntity> entities = new ArrayList<>();

    @Override
    public void onLoad() {
        Global.parallelFor(entities, e -> e.load());
    }

    @Override
    public void onUnload() {
        Global.parallelFor(entities, e -> e.unload());
    }

    @Override
    public void onStart() {
        entities.forEach(e -> e.start());
    }

    @Override
    public void onFirstUpdate(float deltaTime) {
        entities.sort(Comparator.naturalOrder());
        entities.forEach(e -> e.firstUpdate(deltaTime));
    }

    @Override
    public void onUpdate(float deltaTime) {
        entities.forEach(e -> e.update(deltaTime));
    }

    @Override
    public void onLastUpdate(float deltaTime) {
        entities.forEach(e -> e.lastUpdate(deltaTime));
    }

    @Override
    public void onFixedUpdate(float fixedDeltaTime) {
        entities.forEach(e -> e.fixedUpdate(fixedDeltaTime));
    }

    public ArrayList<GameEntity> getEntities() {
        return entities;
    }
}
