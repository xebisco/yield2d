package com.xebisco.yield2d.engine.physics;

import com.xebisco.yield2d.engine.*;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

public class CircleCollider extends Collider {

    @Editable
    private float radius = .5f;
    @Editable
    @CantBeNull
    private Vector2f center = new Vector2f();

    @Override
    public Shape shape() {
        return updateShape(new CircleShape());
    }

    @Override
    public Shape updateShape(Shape s) {
        CircleShape shape = (CircleShape) s;
        Utils.updateB2Vec2(shape.m_p, getWorldPos(center));
        shape.setRadius(getWorldPos(radius));
        return shape;
    }

    public float getRadius() {
        return radius;
    }

    public CircleCollider setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public Vector2f getCenter() {
        return center;
    }

    public CircleCollider setCenter(Vector2f center) {
        this.center = center;
        return this;
    }
}
