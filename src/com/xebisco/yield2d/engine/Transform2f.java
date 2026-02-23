package com.xebisco.yield2d.engine;

public final class Transform2f {

    public enum Origin {
        CENTER
    }

    private final Vector2f position, scale;

    private float rotation;
    private final float initialRotation;

    private Origin origin;
    private final Origin initialOrigin;

    public Transform2f(Vector2f position, Vector2f scale, float rotation, Origin origin) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        initialRotation = rotation;
        this.origin = origin;
        initialOrigin = origin;
    }

    public Transform2f(Vector2f position, Vector2f scale, float rotation) {
        this(position, scale, rotation, Origin.CENTER);
    }

    public Transform2f(Transform2f transform) {
        this(transform.getPosition(), transform.getScale(), transform.getRotation(), transform.getOrigin());
    }

    public Transform2f() {
        this(new Vector2f(), new Vector2f(1, 1), 0);
    }

    public void translate(Vector2f translation) {
        position.addLocal(translation);
    }

    public void scale(Vector2f scale) {
        this.scale.addLocal(scale);
    }

    public void reset() {
        position.reset();
        scale.reset();
        rotation = initialRotation;
        setOrigin(initialOrigin);
    }

    public void rotate(float angle) {
        rotation += angle;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Transform2f setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public float getRotation() {
        return rotation;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }
}
