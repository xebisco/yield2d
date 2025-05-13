package com.xebisco.yield2d.logic.proc;

import java.io.IOException;
import java.io.Serializable;

import com.xebisco.yield2d.graphics.Colorf;
import com.xebisco.yield2d.logic.GameEntity;
import com.xebisco.yield2d.logic.Scene;
import com.xebisco.yield2d.mem.Decoder;
import com.xebisco.yield2d.mem.EncodedObject;

public record SceneFactory(
        EncodedObject<Colorf> preBkg,
        EncodedObject<EntityFactory[]> preEntities
    ) implements ISceneProcessor, Serializable {

    @Override
    public void process(Scene scene) {
        Decoder decoder = new Decoder();
        try {
            scene.setBackground(preBkg.getNew(decoder));
            EntityFactory[] facs = preEntities.getNew(decoder);
            for (EntityFactory fac : facs)
                scene.getEntities().add(new GameEntity(fac));
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
