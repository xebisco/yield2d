package com.xebisco.yield2d.engine;

public class MeshDrawer extends Script implements Drawer {

    public enum DefaultMeshes {
        RECTANGLE(new Mesh2f(new Vector2f[]{
                new Vector2f(-.5f, .5f),
                new Vector2f(-.5f, -.5f),
                new Vector2f(.5f, -.5f),
                new Vector2f(.5f, .5f)
        }, new Vector2f[]{
                new Vector2f(0, 0),
                new Vector2f(0, 1),
                new Vector2f(1, 1),
                new Vector2f(1, 0)
        }, new int[]{
                0, 1, 3, 3, 1, 2
        }));
        private final Mesh2f value;

        DefaultMeshes(Mesh2f value) {
            this.value = value;
        }

        public Mesh2f getValue() {
            return value;
        }
    }

    @Editable
    @Options(DefaultMeshes.class)
    @CantBeNull
    private Mesh2f mesh;
    @Editable
    private TextureFile textureFile;
    @Editable
    private TextureAtlasFile.Clip[] clips;
    @Editable
    private boolean scaleToTextureSize = true;
    @Editable
    @CantBeNull
    private Color color = new Color(1f, 1f, 1f, 1f);
    @Editable
    @CantBeNull
    private Vector2f extraScale = new Vector2f(1f, 1f);
    @Editable
    @CantBeNull
    private Center center = Center.MIDDLE;

    private int clipIndex;

    public MeshDrawer() {
        this(DefaultMeshes.RECTANGLE.getValue());
    }

    public MeshDrawer(Mesh2f mesh) {
        this.mesh = mesh;
    }

    public MeshDrawer(Mesh2f mesh, Vector2f extraScale) {
        this.mesh = mesh;
        this.extraScale = extraScale;
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.start();
        graphics.scale(extraScale.getX(), extraScale.getY());

        TextureFile tx = textureFile;

        if (clips != null) {
            tx = clips[clipIndex].getTexture();
        }

        if (scaleToTextureSize && tx != null) {
            Vector2i texSize = getTextureSize(tx);
            graphics.scale(texSize.getX(), texSize.getY());
        }
        adjustCenter(graphics, new Vector2f(1, 1), center);
        if (tx == null)
        graphics.drawMesh(mesh, color);
        else graphics.drawMesh(mesh, tx, color);
        graphics.end();
    }

    public Vector2f getExtraScale() {
        return extraScale;
    }

    public MeshDrawer setExtraScale(Vector2f extraScale) {
        this.extraScale = extraScale;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public MeshDrawer setColor(Color color) {
        this.color = color;
        return this;
    }

    public boolean isScaleToTextureSize() {
        return scaleToTextureSize;
    }

    public MeshDrawer setScaleToTextureSize(boolean scaleToTextureSize) {
        this.scaleToTextureSize = scaleToTextureSize;
        return this;
    }

    public TextureFile getTextureFile() {
        return textureFile;
    }

    public MeshDrawer setTextureFile(TextureFile textureFile) {
        this.textureFile = textureFile;
        return this;
    }

    public Mesh2f getMesh() {
        return mesh;
    }

    public MeshDrawer setMesh(Mesh2f mesh) {
        this.mesh = mesh;
        return this;
    }

    public TextureAtlasFile.Clip[] getClips() {
        return clips;
    }

    public MeshDrawer setClips(TextureAtlasFile.Clip[] clips) {
        this.clips = clips;
        return this;
    }

    public int getClipIndex() {
        return clipIndex;
    }

    public MeshDrawer setClipIndex(int clipIndex) {
        this.clipIndex = clipIndex;
        return this;
    }

    public Center getCenter() {
        return center;
    }

    public MeshDrawer setCenter(Center center) {
        this.center = center;
        return this;
    }
}
