package com.xebisco.yield2d.engine.physics;

import com.xebisco.yield2d.engine.CantBeNull;
import com.xebisco.yield2d.engine.Editable;
import com.xebisco.yield2d.engine.Utils;
import com.xebisco.yield2d.engine.Vector2f;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.Shape;

public class ChainCollider extends Collider {

    @Editable
    @CantBeNull
    private Vector2f[] vertices = new Vector2f[] {
            new Vector2f(-.5f, 0),
            new Vector2f(.5f, 0)
    };

    private Vector2f[] cacheVerts;

    @Override
    public Shape shape() {
        return updateShape(new ChainShape());
    }

    @Override
    public Shape updateShape(Shape s) {
        ChainShape shape = (ChainShape) s;

        if(cacheVerts == null || cacheVerts.length != vertices.length) {
            cacheVerts = new Vector2f[vertices.length];
            for(int i = 0; i < vertices.length; i++) {
                cacheVerts[i] = new Vector2f(getWorldPos(vertices[i]));
            }
        }

        shape.createChain(Utils.toB2Vec2(cacheVerts), cacheVerts.length);
        return shape;
    }

    public Vector2f[] getVertices() {
        return vertices;
    }

    public ChainCollider setVertices(Vector2f[] vertices) {
        this.vertices = vertices;
        return this;
    }
}
