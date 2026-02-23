package com.xebisco.yield2d.engine;

public class Vector2i {

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

    public Vector2i(Vector2i vector) {
        this(vector.getX(), vector.getY());
    }

    public int getX() {
        return x;
    }

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

    public void reset() {
        setX(getInitialX());
        setY(getInitialY());
    }

    public Vector2i add(Vector2i vector) {
        Vector2i result = new Vector2i(this);
        result.addLocal(vector);
        return result;
    }

    public void addLocal(Vector2i vector) {
        setX(getX() + vector.getX());
        setY(getY() + vector.getY());
    }

    public Vector2i subtract(Vector2i vector) {
        Vector2i result = new Vector2i(this);
        result.addLocal(vector);
        return result;
    }

    public void subtractLocal(Vector2i vector) {
        setX(getX() - vector.getX());
        setY(getY() - vector.getY());
    }

    public Vector2i multiply(int scalar) {
        Vector2i result = new Vector2i(this);
        result.multiplyLocal(scalar);
        return result;
    }

    public void multiplyLocal(int scalar) {
        setX(getX() * scalar);
        setY(getY() * scalar);
    }

    public Vector2i divide(int scalar) {
        Vector2i result = new Vector2i(this);
        result.divideLocal(scalar);
        return result;
    }

    public void divideLocal(int scalar) {
        if (scalar == 0) {
            throw new ArithmeticException("Division by zero");
        }
        setX(getX() / scalar);
        setY(getY() / scalar);
    }

    public float norm() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY());
    }

    public int dot(Vector2i vector) {
        return getX() * vector.getX() + getY() * vector.getY();
    }

    public float distance(Vector2i vector) {
        int dx = getX() - vector.getX();
        int dy = getY() - vector.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float angle(Vector2i vector) {
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
