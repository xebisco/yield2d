package com.xebisco.yield2d.logic;

import java.util.ArrayList;

public abstract class EntityHandler extends BasicLogic {
    private ArrayList<GameEntity> entities;

    @Override
    public void onLoad() {
        setEntities(new ArrayList<>());
    }

    public ArrayList<GameEntity> getEntities() {
        return entities;
    }

    private void setEntities(ArrayList<GameEntity> entities) {
        this.entities = entities;
    }
}
