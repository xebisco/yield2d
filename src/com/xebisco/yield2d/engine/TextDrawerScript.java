package com.xebisco.yield2d.engine;

public class TextDrawerScript extends Script implements Drawer {

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

    public TextDrawerScript() {
    }

    public TextDrawerScript(String contents) {
        this.contents = contents;
    }

    @Override
    public void draw(Graphics g) {
        g.drawText(contents, fontFile, color, charactersRotation);
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public FontFile getFontFile() {
        return fontFile;
    }

    public void setFontFile(FontFile fontFile) {
        this.fontFile = fontFile;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getCharactersRotation() {
        return charactersRotation;
    }

    public void setCharactersRotation(float charactersRotation) {
        this.charactersRotation = charactersRotation;
    }
}
