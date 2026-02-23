package com.xebisco.yield2d.engine;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

public class FontFile extends File {
    private InputStream inputStream;
    private final float size;
    private final boolean antiAliasing;
    private final TextureFile.TextureFilter filter;

    public FontFile(String pathname, float size, boolean antiAliasing, TextureFile.TextureFilter filter) {
        super(pathname);
        this.size = size;
        this.antiAliasing = antiAliasing;
        this.filter = filter;
    }

    public FontFile(String pathname, float size) {
        this(pathname, size, false, TextureFile.TextureFilter.NEAREST);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FontFile fontFile = (FontFile) o;
        return Float.compare(size, fontFile.size) == 0 && antiAliasing == fontFile.antiAliasing && filter == fontFile.filter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), size, antiAliasing, filter);
    }

    public TextureFile.TextureFilter getFilter() {
        return filter;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public float getSize() {
        return size;
    }

    public boolean isAntiAliasing() {
        return antiAliasing;
    }
}
