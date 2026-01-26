package com.xebisco.yield2d.engine.physics;

import com.xebisco.yield2d.engine.*;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Distance;
import org.jbox2d.collision.DistanceInput;
import org.jbox2d.collision.DistanceOutput;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsHandler extends ApplicationHandler implements ContactListener {

    private int positionsSubStepCount = 3, velocitySubStepCount = 8;
    private final World b2World = new World(new Vec2());
    private Vector2f gravity = new Vector2f(0, -10);
    private float pixelsPerMeter = 16f;

    @Override
    public void load() {

    }

    @Override
    public void init() {
        b2World.setContactListener(this);
    }

    @Override
    public void update(TimeSpan elapsed) {
        b2World.setGravity(Utils.toB2Vec2(gravity));
        b2World.step(elapsed.getSeconds(), velocitySubStepCount, positionsSubStepCount);
    }

    @Override
    public void destroy() {

    }

    public Distance.DistanceProxy containerDistanceProxy(Container c) {
        Distance.DistanceProxy p = new Distance.DistanceProxy();
        int i = 0;
        for (Script s : c.getScripts()) {
            if (s instanceof Collider cl) p.set(cl.shape(), i++);
        }
        return p;
    }

    public com.xebisco.yield2d.engine.physics.Distance distance(Container a, Container b) {
        return distance(containerDistanceProxy(a), a.getTransform(), b);
    }

    public com.xebisco.yield2d.engine.physics.Distance distance(Distance.DistanceProxy pA, Transform2f tA, Container b) {
        DistanceInput input = new DistanceInput();
        input.proxyA = pA;
        input.proxyB = containerDistanceProxy(b);
        float ppm = getApplication().getHandler(PhysicsHandler.class).getPixelsPerMeter();
        input.transformA = Utils.toB2Transform(tA, ppm);
        input.transformB = Utils.toB2Transform(b.getTransform(), ppm);
        DistanceOutput output = new DistanceOutput();
        new Distance().distance(output, new Distance.SimplexCache(), input);
        return new com.xebisco.yield2d.engine.physics.Distance(Utils.toVector2f(output.pointA), Utils.toVector2f(output.pointB), output.distance);
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact.m_fixtureA.m_userData instanceof Collider cA && contact.m_fixtureB.m_userData instanceof Collider cB) {
            cA.getContainer().callOnAllScripts(script -> script.onCollisionEnter(cB));
            cB.getContainer().callOnAllScripts(script -> script.onCollisionEnter(cA));
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.m_fixtureA.m_userData instanceof Collider cA && contact.m_fixtureB.m_userData instanceof Collider cB) {
            cA.getContainer().callOnAllScripts(script -> script.onCollisionExit(cB));
            cB.getContainer().callOnAllScripts(script -> script.onCollisionExit(cA));
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }


    public int getPositionsSubStepCount() {
        return positionsSubStepCount;
    }

    public PhysicsHandler setPositionsSubStepCount(int positionsSubStepCount) {
        this.positionsSubStepCount = positionsSubStepCount;
        return this;
    }

    public int getVelocitySubStepCount() {
        return velocitySubStepCount;
    }

    public PhysicsHandler setVelocitySubStepCount(int velocitySubStepCount) {
        this.velocitySubStepCount = velocitySubStepCount;
        return this;
    }

    public World getB2World() {
        return b2World;
    }

    public Vector2f getGravity() {
        return gravity;
    }

    public PhysicsHandler setGravity(Vector2f gravity) {
        this.gravity = gravity;
        return this;
    }

    public float getPixelsPerMeter() {
        return pixelsPerMeter;
    }

    public PhysicsHandler setPixelsPerMeter(float pixelsPerMeter) {
        this.pixelsPerMeter = pixelsPerMeter;
        return this;
    }
}
