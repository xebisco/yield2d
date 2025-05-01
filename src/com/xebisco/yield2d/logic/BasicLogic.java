package com.xebisco.yield2d.logic;

public abstract class BasicLogic implements IBasicLogic {
    private boolean loaded, loading, unloading;

    @Override
    public void load() {
        setLoaded(false);
        setLoading(true);
        IBasicLogic.super.load();
        setLoaded(true);
        setLoading(false);
    }

    @Override
    public void unload() {
        setUnloading(true);
        IBasicLogic.super.unload();
        setLoaded(false);
        setUnloading(false);
    }

    public boolean isLoaded() {
        return loaded;
    }

    private void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoading() {
        return loading;
    }

    private void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isUnloading() {
        return unloading;
    }

    private void setUnloading(boolean unloading) {
        this.unloading = unloading;
    }
    
    
}
