package com.xebisco.yield2d.engine;

import java.io.Serializable;

public class ParticleEmitterScript extends Script {

    public enum SimulationOptions {
        INTERNAL,
        RIGID_BODY
    }

    @Editable
    @CantBeNull
    private Vector2f point = new Vector2f(), rectSize = new Vector2f();

    @Editable
    @CantBeNull
    private float emissionRatePerSecond = 8f, emissionRateNoise = 0f;

    @Options(SimulationOptions.class)
    @Editable
    @CantBeNull
    private SimulationOptions simulationOpt = SimulationOptions.INTERNAL;

    @Editable
    private TextureFile textureFile = new TextureFile("default_particle.png");

    public SimulationOptions getSimulationOpt() {
        return simulationOpt;
    }

    public void setSimulationOpt(SimulationOptions simulationOpt) {
        this.simulationOpt = simulationOpt;
    }

    public TextureFile getTextureFile() {
        return textureFile;
    }

    public void setTextureFile(TextureFile textureFile) {
        this.textureFile = textureFile;
    }

    @Editable
    @CantBeNull
    private float maxLifeSeconds = 3f, gravityMultiplier = 0f, direction, directionNoise = 180, rotation, rotationNoise = 180;

    @Editable
    @CantBeNull
    private Color startColor = new Color(Colors.WHITE), endColor = new Color(Colors.WHITE);

    @Editable
    @CantBeNull
    private Vector2f startSpeed = new Vector2f(80f, 0), startSpeedNoise = new Vector2f(), speedNoise = new Vector2f(), size = new Vector2f(100, 100), sizeNoise = new Vector2f();

    @Editable
    @CantBeNull
    private boolean invertColorTransition = false;

    private float timeToCreateParticle;

    private float emissionRate() {
        return getEmissionRatePerSecond() + Utils.randomFloat(-getEmissionRateNoise(), getEmissionRateNoise());
    }

    private void createNewParticle() {
        Container part = new Container(new Script[]{
                new ParticleBehaviorScript(this),
                new MeshDrawerScript(MeshDrawerScript.DefaultMeshes.RECTANGLE.getValue())
        });
        part.getTransform().translate(getPoint());
        part.getTransform().translate(new Vector2f(Utils.randomFloat(-getRectSize().getX() / 2, getRectSize().getX() / 2), Utils.randomFloat(-getRectSize().getY() / 2, getRectSize().getY())));

        getContainer().getChildren().add(part);
    }

    @Override
    public void update(TimeSpan elapsed) {
        float er = 1f / emissionRate();
        while (timeToCreateParticle >= 1f / emissionRate()) {
            timeToCreateParticle -= er;
            createNewParticle();
        }
        timeToCreateParticle += elapsed.getSeconds();
    }

    public Vector2f getPoint() {
        return point;
    }

    public ParticleEmitterScript setPoint(Vector2f point) {
        this.point = point;
        return this;
    }

    public Vector2f getRectSize() {
        return rectSize;
    }

    public ParticleEmitterScript setRectSize(Vector2f rectSize) {
        this.rectSize = rectSize;
        return this;
    }

    public float getEmissionRatePerSecond() {
        return emissionRatePerSecond;
    }

    public ParticleEmitterScript setEmissionRatePerSecond(float emissionRatePerSecond) {
        this.emissionRatePerSecond = emissionRatePerSecond;
        return this;
    }

    public float getEmissionRateNoise() {
        return emissionRateNoise;
    }

    public ParticleEmitterScript setEmissionRateNoise(float emissionRateNoise) {
        this.emissionRateNoise = emissionRateNoise;
        return this;
    }

    public float getMaxLifeSeconds() {
        return maxLifeSeconds;
    }

    public ParticleEmitterScript setMaxLifeSeconds(float maxLifeSeconds) {
        this.maxLifeSeconds = maxLifeSeconds;
        return this;
    }

    public float getGravityMultiplier() {
        return gravityMultiplier;
    }

    public ParticleEmitterScript setGravityMultiplier(float gravityMultiplier) {
        this.gravityMultiplier = gravityMultiplier;
        return this;
    }

    public float getDirection() {
        return direction;
    }

    public ParticleEmitterScript setDirection(float direction) {
        this.direction = direction;
        return this;
    }

    public float getDirectionNoise() {
        return directionNoise;
    }

    public ParticleEmitterScript setDirectionNoise(float directionNoise) {
        this.directionNoise = directionNoise;
        return this;
    }

    public float getRotation() {
        return rotation;
    }

    public ParticleEmitterScript setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public float getRotationNoise() {
        return rotationNoise;
    }

    public ParticleEmitterScript setRotationNoise(float rotationNoise) {
        this.rotationNoise = rotationNoise;
        return this;
    }

    public Color getStartColor() {
        return startColor;
    }

    public ParticleEmitterScript setStartColor(Color startColor) {
        this.startColor = startColor;
        return this;
    }

    public Color getEndColor() {
        return endColor;
    }

    public ParticleEmitterScript setEndColor(Color endColor) {
        this.endColor = endColor;
        return this;
    }

    public Vector2f getStartSpeed() {
        return startSpeed;
    }

    public ParticleEmitterScript setStartSpeed(Vector2f startSpeed) {
        this.startSpeed = startSpeed;
        return this;
    }

    public Vector2f getStartSpeedNoise() {
        return startSpeedNoise;
    }

    public ParticleEmitterScript setStartSpeedNoise(Vector2f startSpeedNoise) {
        this.startSpeedNoise = startSpeedNoise;
        return this;
    }

    public Vector2f getSpeedNoise() {
        return speedNoise;
    }

    public ParticleEmitterScript setSpeedNoise(Vector2f speedNoise) {
        this.speedNoise = speedNoise;
        return this;
    }

    public Vector2f getSize() {
        return size;
    }

    public ParticleEmitterScript setSize(Vector2f size) {
        this.size = size;
        return this;
    }

    public Vector2f getSizeNoise() {
        return sizeNoise;
    }

    public ParticleEmitterScript setSizeNoise(Vector2f sizeNoise) {
        this.sizeNoise = sizeNoise;
        return this;
    }

    public boolean isInvertColorTransition() {
        return invertColorTransition;
    }

    public ParticleEmitterScript setInvertColorTransition(boolean invertColorTransition) {
        this.invertColorTransition = invertColorTransition;
        return this;
    }

    public float getTimeToCreateParticle() {
        return timeToCreateParticle;
    }

    public ParticleEmitterScript setTimeToCreateParticle(float timeToCreateParticle) {
        this.timeToCreateParticle = timeToCreateParticle;
        return this;
    }
}
