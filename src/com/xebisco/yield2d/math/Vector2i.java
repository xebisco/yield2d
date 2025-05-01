package com.xebisco.yield2d.math;

public class Vector2i implements IVector2i {

    public static final ImmutableVector2i ZERO = new ImmutableVector2i(0, 0);

    private int x, y;

    private final int initialX, initialY;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
        initialX = x;
        initialY = y;
    }

    public Vector2i() {
        this(0, 0);
    }

    public Vector2i(IVector2i vector) {
        this(vector.getX(), vector.getY());
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    @Override
    public void reset() {
        setX(getInitialX());
        setY(getInitialY());
    }

    @Override
    public IVector2i add(IVector2i vector) {
        IVector2i result = new Vector2i(this);
        result.addLocal(vector);
        return result;
    }

    @Override
    public void addLocal(IVector2i vector) {
        setX(getX() + vector.getX());
        setY(getY() + vector.getY());
    }

    @Override
    public IVector2i subtract(IVector2i vector) {
        IVector2i result = new Vector2i(this);
        result.addLocal(vector);
        return result;
    }

    @Override
    public void subtractLocal(IVector2i vector) {
        setX(getX() - vector.getX());
        setY(getY() - vector.getY());
    }

    @Override
    public IVector2i multiply(int scalar) {
        IVector2i result = new Vector2i(this);
        result.multiplyLocal(scalar);
        return result;
    }

    @Override
    public void multiplyLocal(int scalar) {
        setX(getX() * scalar);
        setY(getY() * scalar);
    }

    @Override
    public IVector2i divide(int scalar) {
        IVector2i result = new Vector2i(this);
        result.divideLocal(scalar);
        return result;
    }

    @Override
    public void divideLocal(int scalar) {
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
    public int dot(IVector2i vector) {
        return getX() * vector.getX() + getY() * vector.getY();
    }

    @Override
    public float distance(IVector2i vector) {
        int dx = getX() - vector.getX();
        int dy = getY() - vector.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public float angle(IVector2i vector) {
        float dotProduct = this.dot(vector);
        float normsProduct = this.norm() * vector.norm();
        if (normsProduct == 0) {
            throw new ArithmeticException("Cannot calculate angle with zero vector");
        }
        return (float) Math.acos(dotProduct / normsProduct);
    }

    @Override
    public String toString() {
        return "Vector2i(" + getX() + ", " + getY() + ")";
    }
}
