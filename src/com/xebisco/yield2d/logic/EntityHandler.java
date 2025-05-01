package com.xebisco.yield2d.logic;

import java.util.ArrayList;

public abstract class EntityHandler extends BasicLogic {
    private ArrayList<GameEntity> components;

    @Override
    public void onLoad() {
        components = new ArrayList<>();
    }
}
