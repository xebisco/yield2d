package com.xebisco.yield2d.engine;

public class FontDrawerScript extends Script implements Drawer {
    @Editable
    @CantBeNull
    private String contents = "";

    @Editable
    @CantBeNull
    private FontFile fontFile;

    @Override
    public void draw(Graphics g) {

    }
}
