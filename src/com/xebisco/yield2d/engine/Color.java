package com.xebisco.yield2d.engine;

public class Color {

    private float red, green, blue, alpha;

    public Color(float red, float green, float blue, float alpha) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    public Color(float red, float green, float blue) {
        this(red, green, blue, 1f);
    }

    public Color(int argb) {
        blue = (argb & 255) / 255f;
        green = ((argb >> 8) & 255) / 255f;
        red = ((argb >> 16) & 255) / 255f;
        alpha = ((argb >> 24) & 255) / 255f;
    }

    public Color(Color color) {
        this(color.getArgb());
    }

    public static int getIntFrom8BitColor(int red, int green, int blue, int alpha) {
        int a = (alpha << 24) & 0xFF000000;
        int r = (red << 16) & 0x00FF0000;
        int g = (green << 8) & 0x0000FF00;
        int b = blue & 0x000000FF;

        return a | r | g | b;
    }

    public Color setRed(float red) {
        this.red = red;
        return this;
    }

    public Color setGreen(float green) {
        this.green = green;
        return this;
    }

    public Color setBlue(float blue) {
        this.blue = blue;
        return this;
    }

    public Color setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public void set(float r, float g, float b, float a) {
        setRed(r);
        setGreen(g);
        setBlue(b);
        setAlpha(a);
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public int getArgb() {
        return getIntFrom8BitColor((int) (getRed() * 255), (int) (getGreen() * 255), (int) (getBlue() * 255), (int) (getAlpha() * 255));
    }
}
