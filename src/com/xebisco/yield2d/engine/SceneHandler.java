package com.xebisco.yield2d.engine;

public class SceneHandler extends ApplicationHandler {
    private Scene actualScene;

    @Override
    public void load() {

    }

    @Override
    public void init() {

    }

    @Override
    public void update(TimeSpan elapsed) {
        getActualScene().setContext(getApplication());
        if (getActualScene().getFrames() == 0) getActualScene().init();
        getActualScene().update(elapsed);
    }

    @Override
    public void destroy() {
        if (getActualScene() != null) getActualScene().destroy();
    }

    public Scene getActualScene() {
        return actualScene;
    }

    public void setActualScene(Scene actualScene) {
        if (this.actualScene != null) {
            this.actualScene.setContext(getApplication());
            Debug.println("Destroying scene " + this.actualScene + "...");
            this.actualScene.destroy();
            Debug.println(this.actualScene + " destroyed.");
        }
        this.actualScene = actualScene;
        this.actualScene.setContext(getApplication());
        Debug.println("Loading scene " + this.actualScene + "...");
        this.actualScene.load();
        Debug.println(this.actualScene + " loaded.");
    }
}
