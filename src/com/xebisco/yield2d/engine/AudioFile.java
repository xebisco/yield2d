package com.xebisco.yield2d.engine;

import java.io.File;
import java.io.InputStream;

public class AudioFile extends File {
    private InputStream inputStream;

    public AudioFile(String pathname) {
        super(pathname);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
