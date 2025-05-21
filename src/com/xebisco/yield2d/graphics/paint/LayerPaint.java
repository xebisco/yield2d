package com.xebisco.yield2d.graphics.paint;

import com.xebisco.yield2d.graphics.Colorf;
import com.xebisco.yield2d.graphics.Colors;

public class LayerPaint implements IPaint {

    private final Colorf background = new Colorf(Colors.TRANSPARENT);

    @Override
    public Colorf getForeground() {
        return null;
    }

    @Override
    public Colorf getBackground() {
        return background;
    }
    
}
