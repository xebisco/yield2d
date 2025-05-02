package com.xebisco.yield2d.graphics;

public class ImmutableColorf extends Colorf {

    public ImmutableColorf(float red, float green, float blue, float alpha) {
        super(red, green, blue, alpha);
    }

    public ImmutableColorf(float red, float green, float blue) {
        super(red, green, blue);
    }

    public ImmutableColorf(int argb) {
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
