package com.xebisco.yield2d.engine;

public class ParticleBehaviorScript extends Script {

    private final ParticleEmitterScript.LifetimeInfo lifetimeInfo;
    private final ParticleEmitterScript.ContainerInfo containerInfo;

    private final Vector2f gravity = new Vector2f(0, -10), velocity = new Vector2f();

    private MeshDrawerScript md;

    private float remainingLife;

    public ParticleBehaviorScript(ParticleEmitterScript.LifetimeInfo lifetimeInfo, ParticleEmitterScript.ContainerInfo containerInfo) {
        this.lifetimeInfo = lifetimeInfo;
        this.containerInfo = containerInfo;
    }

    private Vector2f startSpeed() {
        return new Vector2f(
                lifetimeInfo.getStartSpeed().getX() + Utils.randomFloat(-lifetimeInfo.getStartSpeedNoise().getX(), lifetimeInfo.getStartSpeedNoise().getX()),
                lifetimeInfo.getStartSpeed().getY() + Utils.randomFloat(-lifetimeInfo.getStartSpeedNoise().getY(), lifetimeInfo.getStartSpeedNoise().getY())
        );
    }

    private Vector2f size() {
        return new Vector2f(
                lifetimeInfo.getSize().getX() + Utils.randomFloat(-lifetimeInfo.getSizeNoise().getX(), lifetimeInfo.getSizeNoise().getX()),
                lifetimeInfo.getSize().getY() + Utils.randomFloat(-lifetimeInfo.getSizeNoise().getY(), lifetimeInfo.getSizeNoise().getY())
        );
    }

    private float direction() {
        return lifetimeInfo.getDirection() + Utils.randomFloat(-lifetimeInfo.getDirectionNoise(), lifetimeInfo.getDirectionNoise());
    }

    private float rotation() {
        return lifetimeInfo.getRotation() + Utils.randomFloat(-lifetimeInfo.getRotationNoise(), lifetimeInfo.getRotationNoise());
    }

    @Override
    public void init() {
        remainingLife = lifetimeInfo.getMaxLifeSeconds();
        md = getScript(MeshDrawerScript.class);
        md.setTextureFile(containerInfo.getTextureFile());
        md.setExtraScale(size());
        gravity.multiplyLocal(lifetimeInfo.getGravityMultiplier());
        velocity.addLocal(startSpeed().rotate((float) Math.toDegrees(direction())));
        getTransform().rotate(rotation());
    }

    @Override
    public void update(TimeSpan elapsed) {
        remainingLife -= elapsed.getSeconds();
        float r = TweeningInfo.Easing.LINEAR.call(null, lifetimeInfo.getMaxLifeSeconds() - remainingLife, lifetimeInfo.getStartColor().getRed(), lifetimeInfo.getEndColor().getRed(), lifetimeInfo.getMaxLifeSeconds());
        float g = TweeningInfo.Easing.LINEAR.call(null, lifetimeInfo.getMaxLifeSeconds() - remainingLife, lifetimeInfo.getStartColor().getGreen(), lifetimeInfo.getEndColor().getGreen(), lifetimeInfo.getMaxLifeSeconds());
        float b = TweeningInfo.Easing.LINEAR.call(null, lifetimeInfo.getMaxLifeSeconds() - remainingLife, lifetimeInfo.getStartColor().getBlue(), lifetimeInfo.getEndColor().getBlue(), lifetimeInfo.getMaxLifeSeconds());
        float a = TweeningInfo.Easing.LINEAR.call(null, lifetimeInfo.getMaxLifeSeconds() - remainingLife, lifetimeInfo.getStartColor().getAlpha(), lifetimeInfo.getEndColor().getAlpha(), lifetimeInfo.getMaxLifeSeconds());
        md.setColor(new Color(
                lifetimeInfo.isInvertColorTransition() ? 1 - r : r,
                lifetimeInfo.isInvertColorTransition() ? 1 - g : g,
                lifetimeInfo.isInvertColorTransition() ? 1 - b : b,
                lifetimeInfo.isInvertColorTransition() ? 1 - a : a
        ));
        if (remainingLife <= 0) {
            getContainer().destroy();
        }
        velocity.addLocal(gravity);
        velocity.addLocal(new Vector2f(
                Utils.randomFloat(-lifetimeInfo.getSpeedNoise().getX(), lifetimeInfo.getSpeedNoise().getX()),
                Utils.randomFloat(-lifetimeInfo.getSpeedNoise().getY(), lifetimeInfo.getSpeedNoise().getY())
        ));
        getTransform().translate(velocity.multiply(elapsed.getSeconds()));
    }
}
