package com.xebisco.yield2d.engine.physics;

import com.xebisco.yield2d.engine.*;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.dynamics.*;

public class PhysicsBody extends Script {

    public enum Type {
        STATIC(BodyType.STATIC),
        DYNAMIC(BodyType.DYNAMIC),
        KINEMATIC(BodyType.KINEMATIC);
        private final BodyType b2Type;

        Type(BodyType b2Type) {
            this.b2Type = b2Type;
        }
    }

    @Editable
    @Options(Type.class)
    private Type type = Type.DYNAMIC;

    @Editable
    private float angularDamping, linearDamping, gravityScale = 1, mass = 1f, inertia;

    @Editable
    private boolean enableSleep, fixedRotation, bullet, enabled = true;

    private volatile Body b2Body;

    @Override
    public void load() {
        BodyDef def = new BodyDef();
        def.type = type.b2Type;
        Utils.updateB2Vec2(def.position, getWorldPos(getTransform().getPosition()));
        def.angle = getTransform().getRotation();
        World world = getApplication().getHandler(PhysicsHandler.class).getB2World();
        b2Body = world.createBody(def);
    }

    @Override
    public void fixedUpdate(TimeSpan elapsed) {
        updateBody();
        updateFixtures();
    }

    @Override
    public void update(TimeSpan elapsed) {
        updateTransform();
    }

    public float getWorldPos(float p) {
        return p / getApplication().getHandler(PhysicsHandler.class).getPixelsPerMeter();
    }

    private ImmutableVector2f getLinearVelocity() {
        return new ImmutableVector2f(Utils.toVector2f(b2Body.getLinearVelocity()));
    }

    private float getAngularVelocity() {
        return b2Body.getAngularVelocity();
    }

    public Vector2f getWorldPos(Vector2f v) {
        float ppm = getApplication().getHandler(PhysicsHandler.class).getPixelsPerMeter();
        return v.divide(ppm);
    }

    public void applyForce(Vector2f force, Vector2f worldPoint) {
        b2Body.applyForce(Utils.toB2Vec2(force), Utils.toB2Vec2(getWorldPos(worldPoint)));
    }

    public void applyForce(Vector2f force) {
        b2Body.applyForceToCenter(Utils.toB2Vec2(force));
    }

    public void applyTorque(float torque) {
        b2Body.applyTorque(torque);
    }

    public void applyAngularImpulse(float aImpulse) {
        b2Body.applyAngularImpulse(aImpulse);
    }

    public void applyLinearImpulse(Vector2f impulse, Vector2f worldPoint) {
        b2Body.applyLinearImpulse(Utils.toB2Vec2(impulse), Utils.toB2Vec2(getWorldPos(worldPoint)));
    }

    public void applyLinearImpulse(Vector2f impulse) {
        b2Body.applyLinearImpulse(Utils.toB2Vec2(impulse), b2Body.getWorldCenter());
    }

    private void updateBody() {
        b2Body.setType(type.b2Type);
        b2Body.setUserData(this);
        b2Body.setAngularDamping(angularDamping);
        b2Body.setLinearDamping(linearDamping);
        b2Body.setGravityScale(gravityScale);
        b2Body.m_I = inertia;
        MassData massData = new MassData();
        b2Body.getMassData(massData);
        massData.mass = mass;
        b2Body.setMassData(massData);
    }

    private void updateFixtures() {
        for (Fixture f = b2Body.getFixtureList(); f != null; f = f.getNext()) {
            if (f.m_userData instanceof Collider) {
                Collider c = (Collider) f.m_userData;
                c.updateFixture(f);
            }
        }
    }

    private void updateTransform() {
        getContainer().setTransform(Utils.toTransform2f(b2Body.getTransform(), getTransform().getScale(), getApplication().getHandler(PhysicsHandler.class).getPixelsPerMeter()));
    }

    public Type getType() {
        return type;
    }

    public PhysicsBody setType(Type type) {
        this.type = type;
        return this;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public PhysicsBody setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
        return this;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public PhysicsBody setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
        return this;
    }

    public float getGravityScale() {
        return gravityScale;
    }

    public PhysicsBody setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
        return this;
    }

    public float getMass() {
        return mass;
    }

    public PhysicsBody setMass(float mass) {
        this.mass = mass;
        return this;
    }

    public boolean isEnableSleep() {
        return enableSleep;
    }

    public PhysicsBody setEnableSleep(boolean enableSleep) {
        this.enableSleep = enableSleep;
        return this;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public PhysicsBody setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
        return this;
    }

    public boolean isBullet() {
        return bullet;
    }

    public PhysicsBody setBullet(boolean bullet) {
        this.bullet = bullet;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public PhysicsBody setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Body getB2Body() {
        return b2Body;
    }

    public PhysicsBody setB2Body(Body b2Body) {
        this.b2Body = b2Body;
        return this;
    }

    public float getInertia() {
        return inertia;
    }

    public PhysicsBody setInertia(float inertia) {
        this.inertia = inertia;
        return this;
    }
}
