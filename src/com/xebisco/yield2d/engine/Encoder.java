package com.xebisco.yield2d.engine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public final class Encoder {
    /** Write the object to a Base64 string. */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> EncodedObject<T> encodeObject(T o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        String value = Base64.getEncoder().encodeToString(baos.toByteArray());
        return new EncodedObject<>(value, (Class<T>) o.getClass());
    }
}
