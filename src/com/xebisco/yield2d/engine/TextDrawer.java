package com.xebisco.yield2d.engine;

public class TextDrawer extends Script implements Drawer {

    @Editable
    @CantBeNull
    private String contents = "Sample Text";
    @Editable
    @CantBeNull
    private FontFile fontFile = new FontFile("default.ttf", 48f);
    @Editable
    @CantBeNull
    private Color color = new Color(1f, 1f, 1f, 1f);
    @Editable
    private float charactersRotation;

    public TextDrawer() {
    }

    public TextDrawer(String contents) {
        this.contents = contents;
    }

    @Override
    public void draw(Graphics g) {
        g.drawText(contents, fontFile, color, charactersRotation);
    }

    public String getContents() {
        return contents;
    }

    public TextDrawer setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public FontFile getFontFile() {
        return fontFile;
    }

    public TextDrawer setFontFile(FontFile fontFile) {
        this.fontFile = fontFile;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public TextDrawer setColor(Color color) {
        this.color = color;
        return this;
    }

    public float getCharactersRotation() {
        return charactersRotation;
    }

    public TextDrawer setCharactersRotation(float charactersRotation) {
        this.charactersRotation = charactersRotation;
        return this;
    }
}
