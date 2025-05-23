package com.xebisco.yield2d.graphics;

public class Colorf implements IColorf {

    private float red, green, blue, alpha;

    public Colorf(float red, float green, float blue, float alpha) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    public Colorf(float red, float green, float blue) {
        this(red, green, blue, 1f);
    }

    public Colorf(int argb) {
        blue = (argb & 255) / 255f;
        green = ((argb >> 8) & 255) / 255f;
        red = ((argb >> 16) & 255) / 255f;
        alpha = ((argb >> 24) & 255) / 255f;
    }

    public Colorf(Colorf color) {
        this(color.getArgb());
    }

    public static int getIntFrom8BitColor(int red, int green, int blue, int alpha) {
        int a = (alpha << 24) & 0xFF000000;
        int r = (red << 16) & 0x00FF0000;
        int g = (green << 8) & 0x0000FF00;
        int b = blue & 0x000000FF;

        return a | r | g | b;
    }

    public void setRed(float red) {
        this.red = Math.min(1, Math.max(0, red));
    }

    public void setGreen(float green) {
        this.green = Math.min(1, Math.max(0, green));
    }

    public void setBlue(float blue) {
        this.blue = Math.min(1, Math.max(0, blue));
    }

    public void setAlpha(float alpha) {
        this.alpha = Math.min(1, Math.max(0, alpha));
    }

    @Override
    public float getRed() {
        return red;
    }

    @Override
    public float getGreen() {
        return green;
    }

    @Override
    public float getBlue() {
        return blue;
    }

    @Override
    public float getAlpha() {
        return alpha;
    }

    @Override
    public int getArgb() {
        return getIntFrom8BitColor((int) (getRed() * 255), (int) (getGreen() * 255), (int) (getBlue() * 255), (int) (getAlpha() * 255));
    }

}
