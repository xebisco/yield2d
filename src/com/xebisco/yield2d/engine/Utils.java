package com.xebisco.yield2d.engine;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.stream.Collectors;

public final class Utils {

    public static final Random RANDOM = new Random();

    private Utils() {
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
}
