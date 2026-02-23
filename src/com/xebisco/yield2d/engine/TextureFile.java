package com.xebisco.yield2d.engine;

import java.io.File;
import java.io.InputStream;

public class TextureFile extends File {

    public static final TextureFilter DEFAULT_FILTER = TextureFilter.NEAREST;

    public enum TextureFilter {
        NEAREST,
        LINEAR
    }

    private InputStream inputStream;
    private final TextureFilter filter;

    public TextureFile(String pathname, TextureFilter filter) {
        super(pathname);
        this.filter = filter;
    }

    public TextureFile(String pathname) {
        this(pathname, DEFAULT_FILTER);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    public TextureFilter getFilter() {
        return filter;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
