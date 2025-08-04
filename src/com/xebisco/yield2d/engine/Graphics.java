package com.xebisco.yield2d.engine;

public interface Graphics {
    void start();
    void end();

    void drawMesh(Mesh2f mesh, Color color);
    void drawMesh(Mesh2f mesh, TextureFile textureFile, Color color);
    void drawText(String text, FontFile fontFile, Color color, float charRotation);

    void rotate(float angle);
    void scale(float x, float y);
    void translate(float x, float y);
}
