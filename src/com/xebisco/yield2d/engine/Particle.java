package com.xebisco.yield2d.engine;


class Particle implements Poolable {


    private final ParticleEmitter emitter;
    private final Vector2f velocity = new Vector2f();
    private float remainingLife, rotation;
    private final Transform2f transform = new Transform2f();
    private Color color = new Color(1, 1, 1, 1);
    private Vector2f size;


    Particle(ParticleEmitter emitter) {
        this.emitter = emitter;
        reset();
    }


    private Vector2f startSpeed() {
        return new Vector2f(
                emitter.getStartSpeed().getX() + Utils.randomFloat(-emitter.getStartSpeedNoise().getX(), emitter.getStartSpeedNoise().getX()),
                emitter.getStartSpeed().getY() + Utils.randomFloat(-emitter.getStartSpeedNoise().getY(), emitter.getStartSpeedNoise().getY())
        );

    }


    private Vector2f size() {
        return new Vector2f(
                emitter.getSize().getX() + Utils.randomFloat(-emitter.getStartSizeNoise().getX(), emitter.getStartSizeNoise().getX()),
                emitter.getSize().getY() + Utils.randomFloat(-emitter.getStartSizeNoise().getY(), emitter.getStartSizeNoise().getY())
        );
    }


    private float direction() {

        return emitter.getDirection() + Utils.randomFloat(-emitter.getDirectionNoise(), emitter.getDirectionNoise());

    }

    private float rotation() {
        return emitter.getRotation() + Utils.randomFloat(-emitter.getRotationNoise(), emitter.getRotationNoise());
    }

    @Override
    public void reset() {
        setSize(size());
        setRotation(rotation());
        velocity.reset();
        transform.reset();
        remainingLife = emitter.getMaxLifeSeconds();
        velocity.addLocal(startSpeed().rotate((float) Math.toDegrees(direction())));

    }

    public boolean update(TimeSpan elapsed) {
        return updatePart(elapsed, emitter.getContainer().getFrames() < 10 ? 0 : 1);
    }

    public boolean updatePart(TimeSpan elapsed, float randSpeedMult) {

        remainingLife -= elapsed.getSeconds();
        if (remainingLife <= 0) {
            return false;
        }

        float l = 1 - remainingLife / emitter.getMaxLifeSeconds();
        float r = TweeningInfo.Easing.LINEAR.call(null, l, emitter.getStartColor().getRed(), emitter.getEndColor().getRed(), 1);
        float g = TweeningInfo.Easing.LINEAR.call(null, l, emitter.getStartColor().getGreen(), emitter.getEndColor().getGreen(), 1);
        float b = TweeningInfo.Easing.LINEAR.call(null, l, emitter.getStartColor().getBlue(), emitter.getEndColor().getBlue(), 1);
        float a = TweeningInfo.Easing.LINEAR.call(null, l, emitter.getStartColor().getAlpha(), emitter.getEndColor().getAlpha(), 1);

        setColor(new Color(r, g, b, a));

        velocity.addLocal(emitter.getGravity().multiply(elapsed.getSeconds()));
        velocity.addLocal(new Vector2f(
                Utils.randomFloat(
                        -emitter.getSpeedNoise().getX(),
                        emitter.getSpeedNoise().getX()),
                Utils.randomFloat(
                        -emitter.getSpeedNoise().getY(),
                        emitter.getSpeedNoise().getY()))
                .multiply(elapsed.getSeconds() * randSpeedMult));

        transform.translate(velocity.multiply(elapsed.getSeconds()));
        return true;
    }


    public ParticleEmitter getEmitter() {

        return emitter;

    }


    public Vector2f getVelocity() {

        return velocity;

    }


    public float getRemainingLife() {

        return remainingLife;

    }


    public Particle setRemainingLife(float remainingLife) {

        this.remainingLife = remainingLife;

        return this;

    }


    public Transform2f getTransform() {
        return transform;
    }


    public Color getColor() {
        return color;
    }


    public Particle setColor(Color color) {
        this.color = color;
        return this;
    }

    public Vector2f getSize() {
        return size;
    }

    public Particle setSize(Vector2f size) {
        this.size = size;
        return this;
    }

    public float getRotation() {
        return rotation;
    }

    public Particle setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }
}