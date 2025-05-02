package com.xebisco.yield2d;

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
}
