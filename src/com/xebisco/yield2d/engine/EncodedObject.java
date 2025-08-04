package com.xebisco.yield2d.engine;

import java.io.IOException;
import java.io.Serializable;

public record EncodedObject<T extends Serializable>(String value, Class<T> type) implements Serializable {
    public T getNew(Decoder decoder) throws ClassNotFoundException, IOException {
        return decoder.decodeObject(this);
    }

    public T getNew() throws ClassNotFoundException, IOException {
        return getNew(new Decoder());
    }
}
