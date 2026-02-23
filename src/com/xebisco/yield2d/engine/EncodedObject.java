package com.xebisco.yield2d.engine;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

public class EncodedObject<T extends Serializable> implements Serializable {

    private final String value;
    private final Class<T> type;

    public EncodedObject(String value, Class<T> type) {
        this.value = value;
        this.type = type;
    }

    public T getNew(Decoder decoder) throws ClassNotFoundException, IOException {
        return decoder.decodeObject(this);
    }

    public T getNew() throws ClassNotFoundException, IOException {
        return getNew(new Decoder());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EncodedObject<?> that = (EncodedObject<?>) o;
        return Objects.equals(value, that.value) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    public String getValue() {
        return value;
    }

    public Class<T> getType() {
        return type;
    }
}
