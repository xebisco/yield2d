package com.xebisco.yield2d.engine;

public class ImmutableColor extends Color {

    public ImmutableColor(float red, float green, float blue, float alpha) {
        super(red, green, blue, alpha);
    }

    public ImmutableColor(float red, float green, float blue) {
        super(red, green, blue);
    }

    public ImmutableColor(int argb) {
        super(argb);
    }

    @Override
    public void setAlpha(float alpha) {
        throw new UnsupportedOperationException("Cannot modify an immutable color");
    }

    @Override
    public void setBlue(float blue) {
        throw new UnsupportedOperationException("Cannot modify an immutable color");
    }

    @Override
    public void setGreen(float green) {
        throw new UnsupportedOperationException("Cannot modify an immutable color");
    }

    @Override
    public void setRed(float red) {
        throw new UnsupportedOperationException("Cannot modify an immutable color");
    }

}
