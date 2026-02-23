package com.xebisco.yield2d.engine;

import java.util.Arrays;
import java.util.Objects;

public final class Mesh2f {
    private final Vector2f[] vertices, textureCoords;
    private final int[] indices;

    public Mesh2f(Vector2f[] vertices, Vector2f[] textureCoords, int[] indices) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.indices = indices;
    }

    public Mesh2f(Vector2f[] vertices, int[] indices) {
        this(vertices, null, indices);
    }

    public Mesh2f(Vector2f[] vertices, Vector2f[] textureCoords) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.indices = new int[vertices.length];
        for(int i = 0; i < indices.length; i++) indices[i] = i;
    }

    public Mesh2f(Vector2f[] vertices) {
        this(vertices, (Vector2f[]) null);
    }

    public Vector2f getVertexCopy(int pos) {
        return new ImmutableVector2f(vertices[pos]);
    }

    public Vector2f getVertexTextureCoord(int pos) {
        return new ImmutableVector2f(textureCoords[pos]);
    }

    public int getVertexIndex(int pos) {
        return indices[pos];
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mesh2f mesh2f = (Mesh2f) o;
        return Objects.deepEquals(vertices, mesh2f.vertices) && Objects.deepEquals(indices, mesh2f.indices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(vertices), Arrays.hashCode(indices));
    }

    public Vector2f[] getVertices() {
        return vertices;
    }

    public Vector2f[] getTextureCoords() {
        return textureCoords;
    }

    public int[] getIndices() {
        return indices;
    }
}
