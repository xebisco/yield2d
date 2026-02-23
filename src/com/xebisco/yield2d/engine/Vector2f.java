package com.xebisco.yield2d.engine;

public class Vector2f {
    public static Vector2f createVector(float angle, float norm) {
        return new Vector2f(norm, 0).rotate(angle);
    }

    public static Vector2f createImmutableVector(float angle, float norm) {
        return new ImmutableVector2f(new Vector2f(norm, 0).rotate(angle));
    }

    private float x, y;
    
    private final float initialX, initialY;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
        initialX = x;
        initialY = y;
    }

    public Vector2f() {
        this(0, 0);
    }

    public Vector2f(Vector2f vector) {
        this(vector.getX(), vector.getY());
    }

    public Vector2f(Vector2i vector) {
        this(vector.getX(), vector.getY());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getInitialX() {
        return initialX;
    }

    public float getInitialY() {
        return initialY;
    }

    public void reset() {
        setX(getInitialX());
        setY(getInitialY());
    }

    public Vector2f add(Vector2f vector) {
        Vector2f result = new Vector2f(this);
        result.addLocal(vector);
        return result;
    }

    public void addLocal(Vector2f vector) {
        setX(getX() + vector.getX());
        setY(getY() + vector.getY());
    }

    public Vector2f subtract(Vector2f vector) {
        Vector2f result = new Vector2f(this);
        result.addLocal(vector);
        return result;
    }

    public void subtractLocal(Vector2f vector) {
        setX(getX() - vector.getX());
        setY(getY() - vector.getY());
    }

    public Vector2f multiply(float scalar) {
        Vector2f result = new Vector2f(this);
        result.multiplyLocal(scalar);
        return result;
    }

    public void multiplyLocal(float scalar) {
        setX(getX() * scalar);
        setY(getY() * scalar);
    }

    public Vector2f divide(float scalar) {
        Vector2f result = new Vector2f(this);
        result.divideLocal(scalar);
        return result;
    }

    public void divideLocal(float scalar) {
        if (scalar == 0) {
            throw new ArithmeticException("Division by zero");
        }
        setX(getX() / scalar);
        setY(getY() / scalar);
    }

    public float norm() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY());
    }

    public float dot(Vector2f vector) {
        return getX() * vector.getX() + getY() * vector.getY();
    }

    public float distance(Vector2f vector) {
        float dx = getX() - vector.getX();
        float dy = getY() - vector.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public Vector2f normalize() {
        Vector2f result = new Vector2f(this);
        result.normalizeLocal();
        return result;
    }

    public void normalizeLocal() {
        float norm = this.norm();
        if (norm == 0) {
            throw new ArithmeticException("Cannot normalize a zero vector");
        }
        setX(getX() / norm);
        setY(getY() / norm);
    }

    public Vector2f rotate(float angle) {
        Vector2f result = new Vector2f(this);
        result.rotateLocal(angle);
        return result;
    }

    public void rotateLocal(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        float px = getX(), py = getY();
        setX(px * cos - py * sin);
        setY(px * sin + py * cos);
    }

    public float angle(Vector2f vector) {
        float dotProduct = this.dot(vector);
        float normsProduct = this.norm() * vector.norm();
        if (normsProduct == 0) {
            throw new ArithmeticException("Cannot calculate angle with zero vector");
        }
        return (float) Math.acos(dotProduct / normsProduct);
    }

    @Override
    public String toString() {
        return "Vector2f(" + getX() + ", " + getY() + ")";
    }
}
