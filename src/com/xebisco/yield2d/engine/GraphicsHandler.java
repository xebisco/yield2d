package com.xebisco.yield2d.engine;

public abstract class GraphicsHandler extends ApplicationHandler {
    public abstract ImmutableVector2i getTextureSize(TextureFile textureFile);
    public abstract void loadTextureAtlasInfo(TextureAtlasFile.TextureAtlas info);
    public abstract ImmutableVector2i getCanvasSize();
}
