package com.xebisco.yield2d.engine;

import java.util.ArrayList;
import java.util.List;

public class ParticleEmitter extends Script implements Drawer {
    @Editable
    @CantBeNull
    private Vector2f point = new Vector2f(), pointNoise = new Vector2f(), rectSize = new Vector2f();

    @Editable
    @CantBeNull
    private float emissionRatePerSecond = 8f, emissionRateNoise = 0f;

    @Editable
    private TextureFile textureFile = new TextureFile("default_particle.png");

    @Editable
    @CantBeNull
    private float maxLifeSeconds = 3f, direction, directionNoise = 180, rotation, rotationNoise = 180;

    @Editable
    @CantBeNull
    private Color startColor = new Color(Colors.WHITE), endColor = new Color(Colors.WHITE);

    @Editable
    @CantBeNull
    private Vector2f gravity = new Vector2f(0, -10), startSpeed = new Vector2f(80f, 0), startSpeedNoise = new Vector2f(), speedNoise = new Vector2f(), size = new Vector2f(100, 100), startSizeNoise = new Vector2f();

    @Editable
    @CantBeNull
    private boolean invertColorTransition = false, populate;

    private final List<Particle> particleList = new ArrayList<>();
    private ObjectPool<Particle> particlePool;

    private float timeToCreateParticle;

    // Helper for fast random
    private float rand(float min, float max) {
        return Utils.randomFloat(min, max);
    }

    private float emissionRate() {
        return getEmissionRatePerSecond() + rand(-getEmissionRateNoise(), getEmissionRateNoise());
    }

    private Particle createNewParticle() {
        Particle part = particlePool.acquire();

        particleList.add(part);

        float px = point.getX() + rand(-pointNoise.getX(), pointNoise.getY());
        float py = point.getY() + rand(-pointNoise.getY(), pointNoise.getY());

        part.getTransform().getPosition().setX(px);
        part.getTransform().getPosition().setY(py);

        part.getTransform().translate(new Vector2f(
                rand(-getRectSize().getX() / 2, getRectSize().getX() / 2),
                rand(-getRectSize().getY() / 2, getRectSize().getY())
        ));

        return part;
    }

    @Override
    public void init() {
        timeToCreateParticle = 0;

        particlePool = new ArrayListObjectPool<>(() -> new Particle(this), 1000);
    }

    @Override
    public void update(TimeSpan elapsed) {
        if(getContainer().getFrames() == 2) {
            if(populate) {
                int lasting = (int) (maxLifeSeconds * emissionRatePerSecond);
                for(int i = 0; i < lasting; i++) {
                    Particle part = createNewParticle();
                    part.update(new TimeSpan((long) (i / emissionRatePerSecond * 1_000_000_000)));
                }
            }
        }
        float er = 1f / emissionRate();
        while (timeToCreateParticle >= er) {
            timeToCreateParticle -= er;
            createNewParticle().update(new TimeSpan(0));
        }
        timeToCreateParticle += elapsed.getSeconds();

        for (int i = particleList.size() - 1; i >= 0; i--) {
            Particle p = particleList.get(i);
            boolean isAlive = p.update(elapsed);

            if (!isAlive) {
                particlePool.release(p);
                int lastIndex = particleList.size() - 1;
                if (i != lastIndex) {
                    particleList.set(i, particleList.get(lastIndex));
                }
                particleList.remove(lastIndex);
            }
        }
    }

    @Override
    public void draw(Graphics graphics) {
        for (Particle particle : particleList) {
            if (particle == null) continue;
            graphics.start();
            Transform2f trans = particle.getTransform();
            graphics.translate(trans.getPosition().getX(), trans.getPosition().getY());
            graphics.rotate(particle.getRotation());

            Vector2f size = particle.getSize();
            graphics.scale(trans.getScale().getX() * size.getX(), trans.getScale().getY() * size.getY());

            graphics.drawMesh(MeshDrawer.DefaultMeshes.RECTANGLE.getValue(), textureFile, particle.getColor());
            graphics.end();
        }
    }

    public Vector2f getPoint() {
        return point;
    }

    public ParticleEmitter setPoint(Vector2f point) {
        this.point = point;
        return this;
    }

    public Vector2f getPointNoise() {
        return pointNoise;
    }

    public ParticleEmitter setPointNoise(Vector2f pointNoise) {
        this.pointNoise = pointNoise;
        return this;
    }

    public Vector2f getRectSize() {
        return rectSize;
    }

    public ParticleEmitter setRectSize(Vector2f rectSize) {
        this.rectSize = rectSize;
        return this;
    }

    public float getEmissionRatePerSecond() {
        return emissionRatePerSecond;
    }

    public ParticleEmitter setEmissionRatePerSecond(float emissionRatePerSecond) {
        this.emissionRatePerSecond = emissionRatePerSecond;
        return this;
    }

    public float getEmissionRateNoise() {
        return emissionRateNoise;
    }

    public ParticleEmitter setEmissionRateNoise(float emissionRateNoise) {
        this.emissionRateNoise = emissionRateNoise;
        return this;
    }

    public TextureFile getTextureFile() {
        return textureFile;
    }

    public ParticleEmitter setTextureFile(TextureFile textureFile) {
        this.textureFile = textureFile;
        return this;
    }

    public float getMaxLifeSeconds() {
        return maxLifeSeconds;
    }

    public ParticleEmitter setMaxLifeSeconds(float maxLifeSeconds) {
        this.maxLifeSeconds = maxLifeSeconds;
        return this;
    }

    public float getDirection() {
        return direction;
    }

    public ParticleEmitter setDirection(float direction) {
        this.direction = direction;
        return this;
    }

    public float getDirectionNoise() {
        return directionNoise;
    }

    public ParticleEmitter setDirectionNoise(float directionNoise) {
        this.directionNoise = directionNoise;
        return this;
    }

    public float getRotation() {
        return rotation;
    }

    public ParticleEmitter setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public float getRotationNoise() {
        return rotationNoise;
    }

    public ParticleEmitter setRotationNoise(float rotationNoise) {
        this.rotationNoise = rotationNoise;
        return this;
    }

    public Color getStartColor() {
        return startColor;
    }

    public ParticleEmitter setStartColor(Color startColor) {
        this.startColor = startColor;
        return this;
    }

    public Color getEndColor() {
        return endColor;
    }

    public ParticleEmitter setEndColor(Color endColor) {
        this.endColor = endColor;
        return this;
    }

    public Vector2f getGravity() {
        return gravity;
    }

    public ParticleEmitter setGravity(Vector2f gravity) {
        this.gravity = gravity;
        return this;
    }

    public Vector2f getStartSpeed() {
        return startSpeed;
    }

    public ParticleEmitter setStartSpeed(Vector2f startSpeed) {
        this.startSpeed = startSpeed;
        return this;
    }

    public Vector2f getStartSpeedNoise() {
        return startSpeedNoise;
    }

    public ParticleEmitter setStartSpeedNoise(Vector2f startSpeedNoise) {
        this.startSpeedNoise = startSpeedNoise;
        return this;
    }

    public Vector2f getSpeedNoise() {
        return speedNoise;
    }

    public ParticleEmitter setSpeedNoise(Vector2f speedNoise) {
        this.speedNoise = speedNoise;
        return this;
    }

    public Vector2f getSize() {
        return size;
    }

    public ParticleEmitter setSize(Vector2f size) {
        this.size = size;
        return this;
    }

    public Vector2f getStartSizeNoise() {
        return startSizeNoise;
    }

    public ParticleEmitter setStartSizeNoise(Vector2f startSizeNoise) {
        this.startSizeNoise = startSizeNoise;
        return this;
    }

    public boolean isInvertColorTransition() {
        return invertColorTransition;
    }

    public ParticleEmitter setInvertColorTransition(boolean invertColorTransition) {
        this.invertColorTransition = invertColorTransition;
        return this;
    }

    public boolean isPopulate() {
        return populate;
    }

    public ParticleEmitter setPopulate(boolean populate) {
        this.populate = populate;
        return this;
    }

    public List<Particle> getParticleList() {
        return particleList;
    }

    public ObjectPool<Particle> getParticlePool() {
        return particlePool;
    }

    public float getTimeToCreateParticle() {
        return timeToCreateParticle;
    }

    public ParticleEmitter setTimeToCreateParticle(float timeToCreateParticle) {
        this.timeToCreateParticle = timeToCreateParticle;
        return this;
    }
}