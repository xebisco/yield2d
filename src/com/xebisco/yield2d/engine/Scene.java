package com.xebisco.yield2d.engine;

public final class Scene extends Container {
    private Application application;

    public Scene() {
        super(new Script[]{new CameraScript()});
    }

    @Override
    public void setContext(Object context) {
        setApplication((Application) context);
    }

    public CameraScript getCameraScript() {
        return getScript(CameraScript.class);
    }

    public Application getApplication() {
        return application;
    }

    private void setApplication(Application application) {
        this.application = application;
    }
}
