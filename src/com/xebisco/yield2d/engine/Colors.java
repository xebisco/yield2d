package com.xebisco.yield2d.engine;

public final class Colors {
    private Colors() {
    }

    public static final Color
        BLACK = new ImmutableColor(0xFF000000),
        WHITE = new ImmutableColor(0xFFFFFFFF),
        RED = new ImmutableColor(0xFF880000),
        CYAN = new ImmutableColor(0xFFAAFFEE),
        VIOLET = new ImmutableColor(0xFFCC55CC),
        GREEN = new ImmutableColor(0xFF00CC55),
        BLUE = new ImmutableColor(0xFF0000AA),
        YELLOW = new ImmutableColor(0xFFEEEE77),
        ORANGE = new ImmutableColor(0xFFDD8855),
        BROWN = new ImmutableColor(0xFF664400),
        LIGHT_RED = new ImmutableColor(0xFFFF7777),
        DARK_GRAY = new ImmutableColor(0xFF333333),
        GRAY = new ImmutableColor(0xFF777777),
        LIGHT_GREEN = new ImmutableColor(0xFFAAFF66),
        LIGHT_BLUE = new ImmutableColor(0xFF0088FF),
        LIGHT_GRAY = new ImmutableColor(0xFFBBBBBB),
        TRANSPARENT = new ImmutableColor(0x00000000);
}
