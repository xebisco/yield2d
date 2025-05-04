package com.xebisco.yield2d;

import java.util.List;
import java.util.function.Consumer;

import com.xebisco.yield2d.async.ParallelFor;
import com.xebisco.yield2d.graphics.Colorf;
import com.xebisco.yield2d.graphics.Colors;
import com.xebisco.yield2d.logic.IEntityProcessor;
import com.xebisco.yield2d.logic.ISceneProcessor;

public final class Global {
    private Global() {
    }

    private static Colorf
        defSceneBkg = new Colorf((Colors.BLUE.getArgb() + Colors.LIGHT_GRAY.getArgb()) / 2),
        defPaintBkg = new Colorf(Colors.TRANSPARENT),
        defPaintFrg = new Colorf(Colors.WHITE);

    public static final IEntityProcessor RESET_ENTITY_PROCESSOR = e -> {
        e.setLayer((short) 0);
        e.getComponents().clear();
        e.getTransform().reset();
    };

    public static final ISceneProcessor RESET_SCENE_PROCESSOR = s -> {
        s.setBackground(defSceneBkg);
    };

    private static volatile boolean parallelFors = true;

    public static <T> void parallelFor(List<T> list, Consumer<T> consumer) {
        if(parallelFors) {
            new ParallelFor(list, consumer).await(0);
        } else {
            list.forEach(consumer);
        }
    }

    public static Colorf getDefSceneBkg() {
        return defSceneBkg;
    }

    public static void setDefSceneBkg(Colorf defSceneBkg) {
        Global.defSceneBkg = defSceneBkg;
    }

    public static Colorf getDefPaintBkg() {
        return defPaintBkg;
    }

    public static void setDefPaintBkg(Colorf defPaintBkg) {
        Global.defPaintBkg = defPaintBkg;
    }

    public static Colorf getDefPaintFrg() {
        return defPaintFrg;
    }

    public static void setDefPaintFrg(Colorf defPaintFrg) {
        Global.defPaintFrg = defPaintFrg;
    }

    public static boolean isParallelFors() {
        return parallelFors;
    }

    public static void setParallelFors(boolean parallelFors) {
        Global.parallelFors = parallelFors;
    }

    
}
