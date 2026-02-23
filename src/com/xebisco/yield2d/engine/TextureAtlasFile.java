package com.xebisco.yield2d.engine;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;

public class TextureAtlasFile extends File {

    public final static Pattern CLIPS_PATTERN = Pattern.compile("^(.*?)([0-9]+)$");

    public class Clip implements Serializable {
        private final int x, y, width, height;
        private transient TextureFile texture;

        public Clip(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public TextureFile getTexture() {
            return texture;
        }

        public void setTexture(TextureFile texture) {
            this.texture = texture;
        }
    }

    TextureAtlasFile(String pathname) {
        super(pathname);
    }

    private InputStream inputStream;
    private TextureAtlas textureAtlas;

    public class TextureAtlas implements Serializable {

        private final String imagePath;
        private final HashMap<String, Clip[]> clipMap;

        public TextureAtlas(String imagePath, HashMap<String, Clip[]> clipMap) {
            this.imagePath = imagePath;
            this.clipMap = clipMap;
        }

        public TextureFile getTexture(String key) {
            return getClipMap().get(key)[0].getTexture();
        }

        public Clip[] getClips(String anim) {
            return getClipMap().get(anim);
        }

        public String getImagePath() {
            return imagePath;
        }

        public HashMap<String, Clip[]> getClipMap() {
            return clipMap;
        }
    }

    public TextureAtlas getTextureAtlasInfo() {
        return textureAtlas;
    }

    public void setTextureAtlasInfo(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}