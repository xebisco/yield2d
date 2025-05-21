package com.xebisco.yield2d.logic;

import java.util.ArrayList;
import java.util.Comparator;

import com.xebisco.yield2d.Global;

public abstract class EntityHandler extends AbstractLogic {
    private final ArrayList<GameEntity> entities = new ArrayList<>(), toRemoveEntities = new ArrayList<>();

    @Override
    public void onLoad() {
        Global.parallelFor(entities, e -> {
            updateHandlerOnEntity(e);
            e.load();
        });
    }

    @Override
    public void onUnload() {
        Global.parallelFor(entities, e -> {
            updateHandlerOnEntity(e);
            e.unload();
        });
    }

    @Override
    public void onStart() {
        Global.parallelFor(entities, e -> {
            updateHandlerOnEntity(e);
            e.start();
        });
    }

    @Override
    public void onFirstUpdate(float deltaTime) {
        entities.sort(Comparator.naturalOrder());
        Global.parallelFor(entities, e -> {
            updateHandlerOnEntity(e);
            e.firstUpdate(deltaTime);
        });
    }

    @Override
    public void onUpdate(float deltaTime) {
        Global.parallelFor(entities, e -> {
            updateHandlerOnEntity(e);
            e.update(deltaTime);
        });
    }

    @Override
    public void onLastUpdate(float deltaTime) {
        Global.parallelFor(entities, e -> {
            updateHandlerOnEntity(e);
            e.lastUpdate(deltaTime);
        });

        entities.removeAll(toRemoveEntities);
        toRemoveEntities.clear();
    }

    @Override
    public void onFixedUpdate(float fixedDeltaTime) {
        Global.parallelFor(entities, e -> {
            updateHandlerOnEntity(e);
            e.fixedUpdate(fixedDeltaTime);
        });
    }

    public void removeEntity(GameEntity e) {
        toRemoveEntities.add(e);
    }

    public void removeEntity(int index) {
        removeEntity(getEntities().get(index));
    }

    public void updateHandlerOnEntity(GameEntity entity) {
        entity.setHandler(this);
    }

    public ArrayList<GameEntity> getEntities() {
        return entities;
    }
}
