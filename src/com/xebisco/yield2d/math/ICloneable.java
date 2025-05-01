package com.xebisco.yield2d.math;

import java.io.ObjectOutputStream;

public interface ICloneable {
    default Object getClone() {
        new ObjectOutputStream(null)
    }
}
