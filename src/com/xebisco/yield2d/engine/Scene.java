package com.xebisco.yield2d.engine;

public final class Scene extends Container {
    private Application application;

    public Scene() {
        super(new Script[]{new Camera()});
    }

    @Override
    public void setContext(Object context) {
        setApplication((Application) context);
    }

    public Camera getCameraScript() {
        return getScript(Camera.class);
    }

    public Application getApplication() {
        return application;
    }

    private void setApplication(Application application) {
        this.application = application;
    }
}
