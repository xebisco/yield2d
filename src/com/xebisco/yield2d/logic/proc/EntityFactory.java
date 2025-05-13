package com.xebisco.yield2d.logic.proc;

import java.io.IOException;
import java.io.Serializable;

import com.xebisco.yield2d.logic.GameComponent;
import com.xebisco.yield2d.logic.GameEntity;
import com.xebisco.yield2d.math.ITransform2f;
import com.xebisco.yield2d.mem.Decoder;
import com.xebisco.yield2d.mem.EncodedObject;

public record EntityFactory(
        EncodedObject<ITransform2f> preTransform,
        EncodedObject<GameComponent[]> preComponents,
        short preLayer
    ) implements IEntityProcessor, Serializable {

    @Override
    public void process(GameEntity entity) {
        Decoder decoder = new Decoder();
        try {
            entity.setTransform(preTransform.getNew(decoder));
            GameComponent[] components = preComponents.getNew(decoder);
            for (GameComponent comp : components)
                entity.getComponents().add(comp);
            entity.setLayer(preLayer);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
