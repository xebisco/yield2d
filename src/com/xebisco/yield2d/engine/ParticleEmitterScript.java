package com.xebisco.yield2d.engine;

import java.io.Serializable;

public class ParticleEmitterScript extends Script {
    public static class EmissionInfo implements Serializable {
        @Editable
        @CantBeNull
        private Vector2f point = new Vector2f(), rectSize = new Vector2f();

        @Editable
        @CantBeNull
        private float emissionRatePerSecond = 4f, emissionRateNoise = 0f;

        public Vector2f getPoint() {
            return point;
        }

        public void setPoint(Vector2f point) {
            this.point = point;
        }

        public Vector2f getRectSize() {
            return rectSize;
        }

        public void setRectSize(Vector2f rectSize) {
            this.rectSize = rectSize;
        }

        public float getEmissionRatePerSecond() {
            return emissionRatePerSecond;
        }

        public void setEmissionRatePerSecond(float emissionRatePerSecond) {
            this.emissionRatePerSecond = emissionRatePerSecond;
        }

        public float getEmissionRateNoise() {
            return emissionRateNoise;
        }

        public void setEmissionRateNoise(float emissionRateNoise) {
            this.emissionRateNoise = emissionRateNoise;
        }
    }

    public static class ContainerInfo implements Serializable {
        public enum SimulationOptions {
            INTERNAL,
            RIGID_BODY
        }

        @Options(SimulationOptions.class)
        @Editable
        @CantBeNull
        private SimulationOptions simulationOpt = SimulationOptions.INTERNAL;

        @Editable
        private TextureFile textureFile = new TextureFile("maxixe.jpg");

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
    }

    public static class LifetimeInfo implements Serializable {
        @Editable
        @CantBeNull
        private float maxLifeSeconds = 3f, gravityMultiplier = 0f, direction, directionNoise = 180, rotation, rotationNoise = 180;

        @Editable
        @CantBeNull
        private Color startColor = new Color(Colors.TRANSPARENT), endColor = new Color(Colors.WHITE);

        @Editable
        @CantBeNull
        private Vector2f startSpeed = new Vector2f(80f, 0), startSpeedNoise = new Vector2f(), speedNoise = new Vector2f(20, 20), size = new Vector2f(140, 100), sizeNoise = new Vector2f();

        @Editable
        @CantBeNull
        private boolean invertColorTransition = true;

        public float getMaxLifeSeconds() {
            return maxLifeSeconds;
        }

        public void setMaxLifeSeconds(float maxLifeSeconds) {
            this.maxLifeSeconds = maxLifeSeconds;
        }

        public float getGravityMultiplier() {
            return gravityMultiplier;
        }

        public void setGravityMultiplier(float gravityMultiplier) {
            this.gravityMultiplier = gravityMultiplier;
        }

        public float getDirection() {
            return direction;
        }

        public void setDirection(float direction) {
            this.direction = direction;
        }

        public float getDirectionNoise() {
            return directionNoise;
        }

        public void setDirectionNoise(float directionNoise) {
            this.directionNoise = directionNoise;
        }

        public float getRotation() {
            return rotation;
        }

        public void setRotation(float rotation) {
            this.rotation = rotation;
        }

        public float getRotationNoise() {
            return rotationNoise;
        }

        public void setRotationNoise(float rotationNoise) {
            this.rotationNoise = rotationNoise;
        }

        public Color getStartColor() {
            return startColor;
        }

        public void setStartColor(Color startColor) {
            this.startColor = startColor;
        }

        public Color getEndColor() {
            return endColor;
        }

        public void setEndColor(Color endColor) {
            this.endColor = endColor;
        }

        public Vector2f getStartSpeed() {
            return startSpeed;
        }

        public void setStartSpeed(Vector2f startSpeed) {
            this.startSpeed = startSpeed;
        }

        public Vector2f getStartSpeedNoise() {
            return startSpeedNoise;
        }

        public void setStartSpeedNoise(Vector2f startSpeedNoise) {
            this.startSpeedNoise = startSpeedNoise;
        }

        public Vector2f getSpeedNoise() {
            return speedNoise;
        }

        public void setSpeedNoise(Vector2f speedNoise) {
            this.speedNoise = speedNoise;
        }

        public Vector2f getSize() {
            return size;
        }

        public void setSize(Vector2f size) {
            this.size = size;
        }

        public Vector2f getSizeNoise() {
            return sizeNoise;
        }

        public void setSizeNoise(Vector2f sizeNoise) {
            this.sizeNoise = sizeNoise;
        }

        public boolean isInvertColorTransition() {
            return invertColorTransition;
        }

        public void setInvertColorTransition(boolean invertColorTransition) {
            this.invertColorTransition = invertColorTransition;
        }
    }

    @Editable
    @CantBeNull
    private EmissionInfo emissionInfo = new EmissionInfo();

    @Editable
    @CantBeNull
    private LifetimeInfo lifetimeInfo = new LifetimeInfo();

    @Editable
    @CantBeNull
    private ContainerInfo containerInfo = new ContainerInfo();

    private float timeToCreateParticle;

    private float emissionRate() {
        return emissionInfo.getEmissionRatePerSecond() + Utils.randomFloat(-emissionInfo.getEmissionRateNoise(), emissionInfo.getEmissionRateNoise());
    }

    private void createNewParticle() {
        Container part = new Container(new Script[]{
                new ParticleBehaviorScript(lifetimeInfo, containerInfo),
                new MeshDrawerScript(MeshDrawerScript.DefaultMeshes.RECTANGLE.getValue())
        });
        part.getTransform().translate(emissionInfo.getPoint());
        part.getTransform().translate(new Vector2f(Utils.randomFloat(-emissionInfo.getRectSize().getX() / 2, emissionInfo.getRectSize().getX() / 2), Utils.randomFloat(-emissionInfo.getRectSize().getY() / 2, emissionInfo.getRectSize().getY())));

        getContainer().getChildren().add(part);
    }

    @Override
    public void update(TimeSpan elapsed) {
        if(timeToCreateParticle >= 1f / emissionRate()) {
            timeToCreateParticle = 0;
            createNewParticle();
        }
        timeToCreateParticle += elapsed.getSeconds();
    }

    public EmissionInfo getEmissionInfo() {
        return emissionInfo;
    }

    public void setEmissionInfo(EmissionInfo emissionInfo) {
        this.emissionInfo = emissionInfo;
    }

    public LifetimeInfo getLifetimeInfo() {
        return lifetimeInfo;
    }

    public void setLifetimeInfo(LifetimeInfo lifetimeInfo) {
        this.lifetimeInfo = lifetimeInfo;
    }

    public ContainerInfo getContainerInfo() {
        return containerInfo;
    }

    public void setContainerInfo(ContainerInfo containerInfo) {
        this.containerInfo = containerInfo;
    }

    public float getTimeToCreateParticle() {
        return timeToCreateParticle;
    }

    public void setTimeToCreateParticle(float timeToCreateParticle) {
        this.timeToCreateParticle = timeToCreateParticle;
    }
}
