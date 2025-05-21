package com.xebisco.yield2d.graphics.sprite;

import java.util.ArrayList;

import com.xebisco.yield2d.graphics.impl.ImplO1;
import com.xebisco.yield2d.graphics.paint.LayerPaint;
import com.xebisco.yield2d.impl.Connection;
import com.xebisco.yield2d.math.Vector2f;

public final class Layer extends AbstractSprite<LayerPaint> {
    private final ArrayList<ISprite> sprites = new ArrayList<>();
    private boolean fillScreen = true;

    public Layer(LayerPaint paint, Vector2f size) {
        super(paint, size);
    }

    @Override
    public void draw(Connection conn) {
        if(isFillScreen()) ImplO1.fillScreen(conn, getPaint().getBackground());
        for(ISprite s : sprites) {
            s.draw(conn);
        }
    }

    public boolean isFillScreen() {
        return fillScreen;
    }

    public void setFillScreen(boolean fillScreen) {
        this.fillScreen = fillScreen;
    }
}
