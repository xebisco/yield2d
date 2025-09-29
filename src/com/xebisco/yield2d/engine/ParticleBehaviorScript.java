package com.xebisco.yield2d.engine;

class ParticleBehaviorScript extends Script {

    private final ParticleEmitterScript emitter;

    private final Vector2f velocity = new Vector2f();

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
        velocity.addLocal(startSpeed().rotate((float) Math.toDegrees(direction())));
        getTransform().rotate(rotation());
    }

    @Override
    public void update(TimeSpan elapsed) {
        if (emitter.getContainer().getFrames() < 10)
            updatePart(elapsed, 0);
        else
            updatePart(elapsed, 1);
    }

    public void updatePart(TimeSpan elapsed, float randSpeedMult) {
        remainingLife -= elapsed.getSeconds();
        if (remainingLife <= 0) {
            getContainer().destroy();
            return;
        }
        /*
        float l = 1 - remainingLife / emitter.getMaxLifeSeconds();
        float r = emitter.getStartColor().getRed() + ((-emitter.getStartColor().getRed() + emitter.getEndColor().getRed())) * l;
        float g = emitter.getStartColor().getGreen() + ((-emitter.getStartColor().getGreen() + emitter.getEndColor().getGreen()) * l);
        float b = emitter.getStartColor().getBlue() + ((-emitter.getStartColor().getBlue() + emitter.getEndColor().getBlue()) * l);
        float a = emitter.getStartColor().getAlpha() + ((-emitter.getStartColor().getAlpha() + emitter.getEndColor().getAlpha()) * l);
        md.setColor(new Color(r,g,b,a));
        */
        float l = 1 - remainingLife / emitter.getMaxLifeSeconds();
        float r = TweeningInfo.Easing.LINEAR.call(null, l, emitter.getStartColor().getRed(), emitter.getEndColor().getRed(), 1);
        float g = TweeningInfo.Easing.LINEAR.call(null, l, emitter.getStartColor().getGreen(), emitter.getEndColor().getGreen(), 1);
        float b = TweeningInfo.Easing.LINEAR.call(null, l, emitter.getStartColor().getBlue(), emitter.getEndColor().getBlue(), 1);
        float a = TweeningInfo.Easing.LINEAR.call(null, l, emitter.getStartColor().getAlpha(), emitter.getEndColor().getAlpha(), 1);
        md.setColor(new Color(r, g, b, a));

        velocity.addLocal(emitter.getGravity().multiply(elapsed.getSeconds()));
        velocity.addLocal(new Vector2f(
                Utils.randomFloat(
                        -emitter.getSpeedNoise().getX(),
                        emitter.getSpeedNoise().getX()),
                Utils.randomFloat(
                        -emitter.getSpeedNoise().getY(),
                        emitter.getSpeedNoise().getY()))
                .multiply(elapsed.getSeconds() * randSpeedMult));
        getTransform().translate(velocity.multiply(elapsed.getSeconds()));
    }

    public ParticleEmitterScript getEmitter() {
        return emitter;
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
