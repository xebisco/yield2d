package com.xebisco.yield2d.math;

public class Vector2f implements IVector2f {

    public static IVector2f createVector(float angle, float norm) {
        return new Vector2f(norm, 0).rotate(angle);
    }

    public static IVector2f createImmutableVector(float angle, float norm) {
        return new ImmutableVector2f(new Vector2f(norm, 0).rotate(angle));
    }

    public static final ImmutableVector2f ZERO = new ImmutableVector2f(0, 0);

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

    public Vector2f(IVector2f vector) {
        this(vector.getX(), vector.getY());
    }

    public Vector2f(IVector2i vector) {
        this(vector.getX(), vector.getY());
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
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

    @Override
    public void reset() {
        setX(getInitialX());
        setY(getInitialY());
    }

    @Override
    public IVector2f add(IVector2f vector) {
        Vector2f result = new Vector2f(this);
        result.addLocal(vector);
        return result;
    }

    @Override
    public void addLocal(IVector2f vector) {
        setX(getX() + vector.getX());
        setY(getY() + vector.getY());
    }

    @Override
    public IVector2f subtract(IVector2f vector) {
        Vector2f result = new Vector2f(this);
        result.addLocal(vector);
        return result;
    }

    @Override
    public void subtractLocal(IVector2f vector) {
        setX(getX() - vector.getX());
        setY(getY() - vector.getY());
    }

    @Override
    public IVector2f multiply(float scalar) {
        Vector2f result = new Vector2f(this);
        result.multiplyLocal(scalar);
        return result;
    }

    @Override
    public void multiplyLocal(float scalar) {
        setX(getX() * scalar);
        setY(getY() * scalar);
    }

    @Override
    public IVector2f divide(float scalar) {
        Vector2f result = new Vector2f(this);
        result.divideLocal(scalar);
        return result;
    }

    @Override
    public void divideLocal(float scalar) {
        if (scalar == 0) {
            throw new ArithmeticException("Division by zero");
        }
        setX(getX() / scalar);
        setY(getY() / scalar);
    }

    @Override
    public float norm() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY());
    }

    @Override
    public float dot(IVector2f vector) {
        return getX() * vector.getX() + getY() * vector.getY();
    }

    @Override
    public float distance(IVector2f vector) {
        float dx = getX() - vector.getX();
        float dy = getY() - vector.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public IVector2f normalize() {
        Vector2f result = new Vector2f(this);
        result.normalizeLocal();
        return result;
    }

    @Override
    public void normalizeLocal() {
        float norm = this.norm();
        if (norm == 0) {
            throw new ArithmeticException("Cannot normalize a zero vector");
        }
        setX(getX() / norm);
        setY(getY() / norm);
    }

    @Override
    public IVector2f rotate(float angle) {
        Vector2f result = new Vector2f(this);
        result.rotateLocal(angle);
        return result;
    }

    @Override
    public void rotateLocal(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        setX(getX() * cos - getY() * sin);
        setY(getX() * sin + getY() * cos);
    }

    @Override
    public float angle(IVector2f vector) {
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
