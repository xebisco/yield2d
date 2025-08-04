package com.xebisco.yield2d.engine;

public record TimeSpan(long nanoSeconds) {
    public float getHours() {
        return nanoSeconds() / 3.6E+12f;
    }

    public float getMinutes() {
        return nanoSeconds() / 6.E+10f;
    }

    public float getSeconds() {
        return nanoSeconds() / 1.E+9f;
    }

    public float getMilliSeconds() {
        return nanoSeconds() / 1.E+6f;
    }

    public float getMicroSeconds() {
        return nanoSeconds() / 1000f;
    }
}
