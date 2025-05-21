package com.xebisco.yield2d.graphics.impl;

import com.xebisco.yield2d.graphics.Colorf;
import com.xebisco.yield2d.impl.Connection;

public class ImplO1 {
    public static void fillScreen(Connection c, Colorf color) {
        c.callMethod("fillScreen", color);
    }
}
