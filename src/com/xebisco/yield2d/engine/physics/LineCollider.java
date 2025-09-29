package com.xebisco.yield2d.engine.physics;

import com.xebisco.yield2d.engine.CantBeNull;
import com.xebisco.yield2d.engine.Editable;
import com.xebisco.yield2d.engine.Vector2f;
import org.jbox2d.collision.shapes.Shape;

public class LineCollider extends ChainCollider {

    @Editable
    @CantBeNull
    private Vector2f v1 = new Vector2f(-.5f, 0), v2 = new Vector2f(.5f, 0);

    @Override
    public Shape updateShape(Shape s) {
        if(getVertices().length != 2) setVertices(new Vector2f[2]);
        getVertices()[0] = v1;
        getVertices()[1] = v2;
        return super.updateShape(s);
    }

    public Vector2f getV1() {
        return v1;
    }

    public LineCollider setV1(Vector2f v1) {
        this.v1 = v1;
        return this;
    }

    public Vector2f getV2() {
        return v2;
    }

    public LineCollider setV2(Vector2f v2) {
        this.v2 = v2;
        return this;
    }
}
