package com.xebisco.yield2d.engine;

import com.xebisco.yield2d.engine.physics.RayCastInfo;
import org.jbox2d.collision.RayCastOutput;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.stream.Collectors;

public final class Utils {

    public static final Random RANDOM = new Random();

    private Utils() {
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float randomFloat(float min, float max) {
        if (min == max) return min;
        return RANDOM.nextFloat(min, max);
    }

    public static String readStringFromClasspath(String res) throws URISyntaxException, IOException {
        if (!res.startsWith(File.separator)) res = File.separator + res;
        InputStream in = Utils.class.getResourceAsStream(res);
        try (in) {
            if (in == null) throw new NullPointerException("Could not find '" + res + "' resource.");
            try (InputStreamReader isr = new InputStreamReader(in);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public static String readStringFromInputStream(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes());
    }

    public static Transform toB2Transform(Transform2f t, float ppm) {
        return new Transform(Utils.toB2Vec2(t.getPosition().divide(ppm)), new Rot((float) Math.toRadians(t.getRotation())));
    }

    public static Transform2f toTransform2f(Transform t, Vector2f scale, float ppm) {
        return new Transform2f(Utils.toVector2f(t.p.mul(ppm)), scale, (float) Math.toDegrees(t.q.getAngle()));
    }

    public static void updateB2Vec2(Vec2 v1, Vector2f v2) {
        v1.x = v2.getX();
        v1.y = v2.getY();
    }

    public static void updateVector2f(Vector2f v1, Vec2 v2) {
        v1.setX(v2.x);
        v1.setY(v2.y);
    }

    public static Vec2 toB2Vec2(Vector2f v) {
        return new Vec2(v.getX(), v.getY());
    }

    public static Vec2[] toB2Vec2(Vector2f[] v) {
        Vec2[] vs = new Vec2[v.length];
        for (int i = 0; i < v.length; i++) vs[i] = Utils.toB2Vec2(v[i]);
        return vs;
    }

    public static Vector2f toVector2f(Vec2 v) {
        return new Vector2f(v.x, v.y);
    }

    public static RayCastInfo toRayCastInfo(RayCastOutput output) {
        return new RayCastInfo(Utils.toVector2f(output.normal), output.fraction);
    }
}
