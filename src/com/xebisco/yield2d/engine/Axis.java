package com.xebisco.yield2d.engine;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class Axis implements Serializable {
    @Serial
    private static final long serialVersionUID = -1981296784170855000L;

    public Key positive, altPositive, negative, altNegative;

    public Axis(Key positive, Key altPositive, Key negative, Key altNegative) {
        this.positive = positive;
        this.altPositive = altPositive;
        this.negative = negative;
        this.altNegative = altNegative;
    }

    public float getValue(InputHandler inputHandler) {
        if (inputHandler.isKeyPressed(positive) || inputHandler.isKeyPressed(altPositive)) return 1f;
        if (inputHandler.isKeyPressed(negative) || inputHandler.isKeyPressed(altNegative)) return -1f;
        return 0f;
    }

    @Override
    public String toString() {
        return "Axis{" +
                "positive=" + positive +
                ", altPositive=" + altPositive +
                ", negative=" + negative +
                ", altNegative=" + altNegative +
                '}';
    }

    public Key getPositive() {
        return positive;
    }

    public void setPositive(Key positive) {
        this.positive = positive;
    }

    public Key getAltPositive() {
        return altPositive;
    }

    public void setAltPositive(Key altPositive) {
        this.altPositive = altPositive;
    }

    public Key getNegative() {
        return negative;
    }

    public void setNegative(Key negative) {
        this.negative = negative;
    }

    public Key getAltNegative() {
        return altNegative;
    }

    public void setAltNegative(Key altNegative) {
        this.altNegative = altNegative;
    }
}
