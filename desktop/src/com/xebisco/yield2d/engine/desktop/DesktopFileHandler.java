package com.xebisco.yield2d.engine.desktop;

import com.xebisco.yield2d.engine.FileHandler;

import java.io.*;
import java.util.Objects;

public class DesktopFileHandler extends FileHandler {
    private final boolean useClasspath;
    private final File home;

    public DesktopFileHandler(boolean useClasspath) {
        this.useClasspath = useClasspath;
        home = null;
    }

    public DesktopFileHandler(File home) {
        this.useClasspath = false;
        this.home = home;
    }

    public DesktopFileHandler() {
        this(true);
    }

    @Override
    public InputStream openInputStream(String pathname) {
        if (useClasspath) {
            return Objects.requireNonNull(getClass().getResourceAsStream(File.separator + pathname));
        } else {
            try {
                return new FileInputStream(pathname);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void closeInputStream(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
