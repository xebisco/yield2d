package com.xebisco.yield2d;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

import com.xebisco.yield2d.logic.IEntityProcessor;

public final class Global {
    public static final IEntityProcessor RESET_ENTITY_PROCESSOR = e -> {
        e.setLayer((short) 0);
        e.getComponents().clear();
        e.getTransform().reset();
    };
}
