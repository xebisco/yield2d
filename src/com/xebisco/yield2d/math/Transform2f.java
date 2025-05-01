package com.xebisco.yield2d.math;

public final class Transform2f implements ITransform2f {

    private final IVector2f position, scale;

    private float rotation;
    private final float initialRotation;

    private Origin origin;
    private final Origin initialOrigin;

    public Transform2f(IVector2f position, IVector2f scale, float rotation, Origin origin) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        initialRotation = rotation;
        this.origin = origin;
        initialOrigin = origin;
    }

    public Transform2f(IVector2f position, IVector2f scale, float rotation) {
        this(position, scale, rotation, Origin.CENTER);
    }

    public Transform2f(ITransform2f transform) {
        this(transform.getPosition(), transform.getScale(), transform.getRotation(), transform.getOrigin());
    }

    public Transform2f() {
        this(new Vector2f(), new Vector2f(1, 1), 0);
    }

    @Override
    public void translate(IVector2f translation) {
        position.addLocal(translation);
    }

    @Override
    public void scale(IVector2f scale) {
        this.scale.addLocal(scale);
    }

    @Override
    public void reset() {
        position.reset();
        scale.reset();
        rotation = initialRotation;
        setOrigin(initialOrigin);
    }

    @Override
    public void rotate(float angle) {
        rotation += angle;
    }

    @Override
    public IVector2f getPosition() {
        return position;
    }

    @Override
    public IVector2f getScale() {
        return scale;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public Origin getOrigin() {
        return origin;
    }

    @Override
    public void setOrigin(Origin origin) {
        this.origin = origin;
    }
}
