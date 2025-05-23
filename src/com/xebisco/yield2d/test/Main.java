package com.xebisco.yield2d.test;

import com.xebisco.yield2d.Global;
import com.xebisco.yield2d.async.LoopContext;
import com.xebisco.yield2d.logic.GameComponent;
import com.xebisco.yield2d.logic.GameEntity;
import com.xebisco.yield2d.logic.Scene;

class Main {
    public static void main(String[] args) {
        Global.setParallelFors(false);
        Scene scene = new Scene(null);

        scene.getEntities().add(new GameEntity(e -> {
            e.getComponents().add(new GameComponent() {
                @Override
                public void onStart() {
                    System.out.println("starty");
                }
                @Override
                public void onUpdate(float deltaTime) {
                    System.out.println((1 / deltaTime) + ", " + getScene().getEntities().size());
                    getScene().removeEntity(getScene().getEntities().size() - 1);
                }
            });
        }));

        for(int i = 0; i < 10000; i++) scene.getEntities().add(new GameEntity(e -> {
            e.getComponents().add(new GameComponent() {
                @Override
                public void onStart() {
                    System.out.println("starty");
                }
                @Override
                public void onUpdate(float deltaTime) {
                    
                }
            });
        }));

        scene.load();
        LoopContext c = new LoopContext("null", scene);
        c.run();
        scene.unload();
    }
}
