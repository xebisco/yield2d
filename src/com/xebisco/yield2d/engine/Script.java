package com.xebisco.yield2d.engine;

import com.xebisco.yield2d.engine.physics.Collider;

public abstract class Script implements Handler {
    private Container container;

    @Override
    public void load() {

    }

    @Override
    public void init() {

    }

    @Override
    public void update(TimeSpan elapsed) {

    }

    @Override
    public void destroy() {

    }

    public void onCollisionEnter(Collider colliding) {

    }

    public void onCollisionExit(Collider colliding) {

    }

    @Override
    public final void setContext(Object context) {
        setContainer((Container) context);
    }

    public final boolean isKeyPressed(Key key) {
        return getApplication().getInputHandler().isKeyPressed(key);
    }

    public final ImmutableVector2i getTextureSize(TextureFile textureFile) {
        return getApplication().getGraphicsHandler().getTextureSize(textureFile);
    }

    public final float getAxisValue(String axisKey) {
        return getApplication().getAxisValue(axisKey);
    }

    public final Vector2f getAxisValue2f(String xAxisKey, String yAxisKey) {
        return new Vector2f(getAxisValue(xAxisKey), getAxisValue(yAxisKey));
    }

    public final <T extends Script> T[] getScripts(Class<T> scriptType, int arraySize) {
        return getContainer().getScripts(scriptType, arraySize);
    }

    public final <T extends Script> T getScript(Class<T> scriptType, int index) {
        return getContainer().getScript(scriptType, index);
    }

    public final <T extends Script> T getScript(Class<T> scriptType) {
        return getContainer().getScript(scriptType);
    }

    public final void loadTextureAtlas(TextureAtlasFile textureAtlasFile) {
        getApplication().loadTextureAtlas(textureAtlasFile);
    }

    public final Scene getScene() {
        return getContainer().getScene();
    }

    public final Application getApplication() {
        return getScene().getApplication();
    }


    public final Transform2f getTransform() {
        return getContainer().getTransform();
    }

    public final Container getContainer() {
        return container;
    }

    private void setContainer(Container container) {
        this.container = container;
    }
}
