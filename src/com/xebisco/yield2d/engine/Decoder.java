package com.xebisco.yield2d.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Base64;

public final class Decoder {
     /** Read the object from Base64 string. */
    public <T extends Serializable> T decodeObject(EncodedObject<T> e) throws IOException,ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(e.getValue());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return e.getType().cast(o);
    }
}
