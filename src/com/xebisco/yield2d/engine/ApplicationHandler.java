package com.xebisco.yield2d.engine;

public abstract class ApplicationHandler implements Handler {
    private Application application;

    @Override
    public final void setContext(Object context) {
        setApplication((Application) context);
    }

    public final Application getApplication() {
        return application;
    }

    private void setApplication(Application application) {
        this.application = application;
    }
}
