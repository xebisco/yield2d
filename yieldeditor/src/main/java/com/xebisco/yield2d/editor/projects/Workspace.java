package com.xebisco.yield2d.editor.projects;

import com.xebisco.yield2d.engine.DirsOnly;
import com.xebisco.yield2d.engine.Visible;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

public class Workspace implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Visible
    @DirsOnly
    private File directory = new File(System.getProperty("user.home"), "YieldWorkspace");

    public File getDirectory() {
        return directory;
    }

    public Workspace setDirectory(File directory) {
        this.directory = directory;
        return this;
    }
}
