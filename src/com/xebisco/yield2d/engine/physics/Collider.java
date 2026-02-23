package com.xebisco.yield2d.engine.physics;

import com.xebisco.yield2d.engine.*;
import org.jbox2d.collision.RayCastInput;
import org.jbox2d.collision.RayCastOutput;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Transform;
import org.jbox2d.dynamics.Fixture;

import java.util.ArrayList;
import java.util.List;

public abstract class Collider extends Script {

    @Editable
    private float density, restitution, friction = .2f;

    @Editable
    private boolean sensor;

    @Editable
    private String collisionCategory = "default";

    @Editable
    private List<String> maskCategories = new ArrayList<>();

    public abstract Shape shape();

    public abstract Shape updateShape(Shape shape);

    @Override
    public void init() {
        updateFixture(getScript(PhysicsBody.class).getB2Body().createFixture(shape(), density));
    }

    public void updateFixture(Fixture fixture) {
        fixture.m_shape = shape();
        fixture.m_density = density;
        fixture.m_friction = friction;
        fixture.m_isSensor = sensor;
        fixture.m_restitution = restitution;
        fixture.m_userData = this;

        fixture.m_filter.categoryBits = collisionCategory.hashCode();
        for (String m : maskCategories)
            fixture.m_filter.maskBits &= ~m.hashCode();
    }

    public float getWorldPos(float p) {
        return p / getApplication().getHandler(PhysicsHandler.class).getPixelsPerMeter();
    }

    public Vector2f getWorldPos(Vector2f v) {
        float ppm = getApplication().getHandler(PhysicsHandler.class).getPixelsPerMeter();
        return v.divide(ppm);
    }

    public boolean testPoint(Vector2f p) {
        Transform b2t = Utils.toB2Transform(getTransform(), getApplication().getHandler(PhysicsHandler.class).getPixelsPerMeter());
        return shape().testPoint(b2t, Utils.toB2Vec2(p));
    }

    public RayCastInfo rayCast(Vector2f origin, Vector2f translation) {
        RayCastInput input = new RayCastInput();
        input.p1.set(Utils.toB2Vec2(origin));
        input.p2.set(Utils.toB2Vec2(translation));
        RayCastOutput output = new RayCastOutput();
        return shape().raycast(output, input, Utils.toB2Transform(getTransform(), getApplication().getHandler(PhysicsHandler.class).getPixelsPerMeter()), 0) ? Utils.toRayCastInfo(output) : null;
    }

    public com.xebisco.yield2d.engine.physics.Distance distance(Container c) {
        org.jbox2d.collision.Distance.DistanceProxy pA = new org.jbox2d.collision.Distance.DistanceProxy();
        pA.set(shape(), 0);
        return getApplication().getHandler(PhysicsHandler.class).distance(pA, getTransform(), c);
    }

    public float getDensity() {
        return density;
    }

    public Collider setDensity(float density) {
        this.density = density;
        return this;
    }

    public float getRestitution() {
        return restitution;
    }

    public Collider setRestitution(float restitution) {
        this.restitution = restitution;
        return this;
    }

    public float getFriction() {
        return friction;
    }

    public Collider setFriction(float friction) {
        this.friction = friction;
        return this;
    }

    public boolean isSensor() {
        return sensor;
    }

    public Collider setSensor(boolean sensor) {
        this.sensor = sensor;
        return this;
    }

    public String getCollisionCategory() {
        return collisionCategory;
    }

    public Collider setCollisionCategory(String collisionCategory) {
        this.collisionCategory = collisionCategory;
        return this;
    }

    public List<String> getMaskCategories() {
        return maskCategories;
    }

    public Collider setMaskCategories(List<String> maskCategories) {
        this.maskCategories = maskCategories;
        return this;
    }
}
