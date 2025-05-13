package com.xebisco.yield2d.logic;

public abstract class GameComponent extends AbstractLogic {
    @Override
    public final void start() {
        super.start();
    }

    @Override
    public void onStart() {
        
    }

    @Override
    public final void load() {
        super.load();
    }

    @Override
    public void onLoad() {
        
    }

    @Override
    public final void unload() {
        super.unload();
    }

    @Override
    public void onUnload() {
        
    }

    @Override
    public final void firstUpdate(float deltaTime) {
        super.firstUpdate(deltaTime);
    }

    @Override
    public void onFirstUpdate(float deltaTime) {
        
    }

    @Override
    public final void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void onUpdate(float deltaTime) {
        
    }

    @Override
    public final void lastUpdate(float deltaTime) {
        super.lastUpdate(deltaTime);
    }

    @Override
    public void onLastUpdate(float deltaTime) {
        
    }

    @Override
    public final void fixedUpdate(float fixedDeltaTime) {
        super.fixedUpdate(fixedDeltaTime);
    }

    @Override
    public void onFixedUpdate(float fixedDeltaTime) {
        
    }
}