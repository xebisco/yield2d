package com.xebisco.yield2d.editor.projects;

import com.xebisco.yield2d.editor.EngineConfiguration;
import com.xebisco.yield2d.engine.LongText;
import com.xebisco.yield2d.engine.StartSection;
import com.xebisco.yield2d.engine.Visible;

import java.io.Serializable;

public class Project implements Serializable {
    @Visible
    @StartSection("Data")
    private String name;
    @Visible
    @LongText
    private String description;

    @Visible
    @StartSection("Engine")
    private EngineConfiguration engineConfig = new EngineConfiguration();

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }
}
