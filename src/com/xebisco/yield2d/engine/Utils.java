package com.xebisco.yield2d.engine;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public final class Utils {

    public static final Random RANDOM = new Random();

    private Utils() {
    }

    public static float randomFloat(float min, float max) {
        if(min == max) return min;
        return RANDOM.nextFloat(min, max);
    }

    public static String readStringFromClasspath(String res) throws URISyntaxException, IOException {
        if (!res.startsWith(File.separator)) res = File.separator + res;
        URL resource = Utils.class.getResource(res);
        if (resource == null) throw new NullPointerException("Could not find '" + res + "' resource.");
        return Files.readString(Path.of(resource.toURI()));
    }

    public static String readStringFromInputStream(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes());
    }
}
