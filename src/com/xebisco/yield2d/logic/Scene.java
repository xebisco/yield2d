package com.xebisco.yield2d.logic;

import java.io.Serializable;
import java.util.ArrayList;

import com.xebisco.yield2d.graphics.Colorf;
import com.xebisco.yield2d.mem.EncodedObject;

public final class Scene extends BasicLogic implements Serializable {
    private final ArrayList<GameEntity> entities = new ArrayList<>();
    private Colorf background;

    public ArrayList<GameEntity> getEntities() {
        return entities;
    }

    public Colorf getBackground() {
        return background;
    }

    public void setBackground(Colorf background) {
        this.background = background;
    }
}
