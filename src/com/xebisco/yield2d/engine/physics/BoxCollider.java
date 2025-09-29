package com.xebisco.yield2d.engine.physics;

import com.xebisco.yield2d.engine.CantBeNull;
import com.xebisco.yield2d.engine.Editable;
import com.xebisco.yield2d.engine.Utils;
import com.xebisco.yield2d.engine.Vector2f;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

public class BoxCollider extends Collider {

    @Editable
    @CantBeNull
    private Vector2f center = new Vector2f(), size = new Vector2f(1f, 1f);

    @Editable
    private float angle;

    @Override
    public Shape shape() {
        return updateShape(new PolygonShape());
    }

    @Override
    public Shape updateShape(Shape s) {
        PolygonShape shape = (PolygonShape) s;
        shape.setAsBox(getWorldPos(size.getX() / 2f), getWorldPos(size.getY() / 2f), Utils.toB2Vec2(getWorldPos(center)), angle);
        return shape;
    }

    public Vector2f getCenter() {
        return center;
    }

    public BoxCollider setCenter(Vector2f center) {
        this.center = center;
        return this;
    }

    public Vector2f getSize() {
        return size;
    }

    public BoxCollider setSize(Vector2f size) {
        this.size = size;
        return this;
    }

    public float getAngle() {
        return angle;
    }

    public BoxCollider setAngle(float angle) {
        this.angle = angle;
        return this;
    }
}
