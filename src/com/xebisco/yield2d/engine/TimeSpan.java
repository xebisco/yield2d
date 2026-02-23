package com.xebisco.yield2d.engine;

import java.util.Objects;

public class TimeSpan {

    private final long nanoSeconds;

    public TimeSpan(long nanoSeconds) {
        this.nanoSeconds = nanoSeconds;
    }

    public float getHours() {
        return getNanoSeconds() / 3.6E+12f;
    }

    public float getMinutes() {
        return getNanoSeconds() / 6.E+10f;
    }

    public float getSeconds() {
        return getNanoSeconds() / 1.E+9f;
    }

    public float getMilliSeconds() {
        return getNanoSeconds() / 1.E+6f;
    }

    public float getMicroSeconds() {
        return getNanoSeconds() / 1000f;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TimeSpan timeSpan = (TimeSpan) o;
        return nanoSeconds == timeSpan.nanoSeconds;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nanoSeconds);
    }

    public long getNanoSeconds() {
        return nanoSeconds;
    }
}
