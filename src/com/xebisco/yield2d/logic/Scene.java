package com.xebisco.yield2d.logic;

import java.io.Serializable;

import com.xebisco.yield2d.Global;
import com.xebisco.yield2d.graphics.Colorf;

public final class Scene extends EntityHandler implements Serializable {
    private Colorf background;
    private final ISceneProcessor sceneProcessor;

    public Scene(ISceneProcessor sceneProcessor) {
        this.sceneProcessor = sceneProcessor;
    }

    @Override
    public void onLoad() {
        Global.RESET_SCENE_PROCESSOR.process(this);
        if (sceneProcessor != null) {
            sceneProcessor.process(this);
        }
        super.onLoad();
    }

    public Colorf getBackground() {
        return background;
    }

    public void setBackground(Colorf background) {
        this.background = background;
    }
}
