package com.xebisco.yield2d.engine;

class ParticleBehaviorScript extends Script {

    private final ParticleEmitterScript emitter;

    private final Vector2f gravity = new Vector2f(0, -10), velocity = new Vector2f();

    private MeshDrawerScript md;

    private float remainingLife;

    ParticleBehaviorScript(ParticleEmitterScript emitter) {
        this.emitter = emitter;
    }

    private Vector2f startSpeed() {
        return new Vector2f(
                emitter.getStartSpeed().getX() + Utils.randomFloat(-emitter.getStartSpeedNoise().getX(), emitter.getStartSpeedNoise().getX()),
                emitter.getStartSpeed().getY() + Utils.randomFloat(-emitter.getStartSpeedNoise().getY(), emitter.getStartSpeedNoise().getY())
        );
    }

    private Vector2f size() {
        return new Vector2f(
                emitter.getSize().getX() + Utils.randomFloat(-emitter.getSizeNoise().getX(), emitter.getSizeNoise().getX()),
                emitter.getSize().getY() + Utils.randomFloat(-emitter.getSizeNoise().getY(), emitter.getSizeNoise().getY())
        );
    }

    private float direction() {
        return emitter.getDirection() + Utils.randomFloat(-emitter.getDirectionNoise(), emitter.getDirectionNoise());
    }

    private float rotation() {
        return emitter.getRotation() + Utils.randomFloat(-emitter.getRotationNoise(), emitter.getRotationNoise());
    }

    @Override
    public void init() {
        remainingLife = emitter.getMaxLifeSeconds();
        md = getScript(MeshDrawerScript.class);
        md.setTextureFile(emitter.getTextureFile());
        md.setExtraScale(size());
        gravity.multiplyLocal(emitter.getGravityMultiplier());
        velocity.addLocal(startSpeed().rotate((float) Math.toDegrees(direction())));
        getTransform().rotate(rotation());
    }

    @Override
    public void update(TimeSpan elapsed) {
        remainingLife -= elapsed.getSeconds();
        float r = TweeningInfo.Easing.LINEAR.call(null, emitter.getMaxLifeSeconds() - remainingLife, emitter.getStartColor().getRed(), emitter.getEndColor().getRed(), emitter.getMaxLifeSeconds());
        float g = TweeningInfo.Easing.LINEAR.call(null, emitter.getMaxLifeSeconds() - remainingLife, emitter.getStartColor().getGreen(), emitter.getEndColor().getGreen(), emitter.getMaxLifeSeconds());
        float b = TweeningInfo.Easing.LINEAR.call(null, emitter.getMaxLifeSeconds() - remainingLife, emitter.getStartColor().getBlue(), emitter.getEndColor().getBlue(), emitter.getMaxLifeSeconds());
        float a = TweeningInfo.Easing.LINEAR.call(null, emitter.getMaxLifeSeconds() - remainingLife, emitter.getStartColor().getAlpha(), emitter.getEndColor().getAlpha(), emitter.getMaxLifeSeconds());
        md.setColor(new Color(
                emitter.isInvertColorTransition() ? 1 - r : r,
                emitter.isInvertColorTransition() ? 1 - g : g,
                emitter.isInvertColorTransition() ? 1 - b : b,
                emitter.isInvertColorTransition() ? 1 - a : a
        ));
        if (remainingLife <= 0) {
            getContainer().destroy();
        }
        velocity.addLocal(gravity);
        velocity.addLocal(new Vector2f(
                Utils.randomFloat(-emitter.getSpeedNoise().getX(), emitter.getSpeedNoise().getX()),
                Utils.randomFloat(-emitter.getSpeedNoise().getY(), emitter.getSpeedNoise().getY())
        ));
        getTransform().translate(velocity.multiply(elapsed.getSeconds()));
    }

    public ParticleEmitterScript getEmitter() {
        return emitter;
    }

    public Vector2f getGravity() {
        return gravity;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public MeshDrawerScript getMd() {
        return md;
    }

    public ParticleBehaviorScript setMd(MeshDrawerScript md) {
        this.md = md;
        return this;
    }

    public float getRemainingLife() {
        return remainingLife;
    }

    public ParticleBehaviorScript setRemainingLife(float remainingLife) {
        this.remainingLife = remainingLife;
        return this;
    }
}
