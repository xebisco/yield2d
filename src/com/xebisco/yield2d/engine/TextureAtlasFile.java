package com.xebisco.yield2d.engine;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextureAtlasFile extends File {

    public final static Pattern CLIPS_PATTERN = Pattern.compile("^(.*?)_([0-9]+)$");

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
    public record TextureAtlas(String imagePath, TextureFile.TextureFilter filter, HashMap<String, Clip> clipMap) implements Serializable {
        public TextureFile getTexture(String key) {
            return clipMap().get(key).getTexture();
        }

        public TextureFile[] getTextures(String beginning) {
            Map<Integer, TextureFile> textures = new HashMap<>();
            Matcher m = CLIPS_PATTERN.matcher("");
            for(String k : clipMap.keySet().stream().sorted(Comparator.reverseOrder()).toList()) {
                m.reset(k);
                if(m.find() && m.group(1).equals(beginning)) {
                    textures.put(Integer.parseInt(m.group(2)), getTexture(k));
                }
            }

            return textures.values().toArray(new TextureFile[0]);
        }
    }

    private InputStream inputStream;
    private TextureAtlas textureAtlas;

    public TextureAtlasFile(String pathname) {
        super(pathname);
    }

    public TextureAtlasFile(TextureAtlas textureAtlas) {
        super("");
        this.textureAtlas = textureAtlas;
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