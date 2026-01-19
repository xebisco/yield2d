package com.xebisco.yield2d.editor;

import com.xebisco.yield2d.editor.projects.Project;
import com.xebisco.yield2d.editor.projects.ProjectManager;
import com.xebisco.yield2d.engine.system.AppData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Editor {

    public static final AppData EDITOR_DATA = new AppData("YieldEditor", "dev", 0);


    private final Project project;
    private final File projectDir;

    private final JFrame frame;

    public Editor(Project project, File projectDir) {
        this.project = project;
        this.projectDir = projectDir;



        updateProjectDir();

        frame = new JFrame() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                updateTitle();
            }
        };
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (Utils.yesno("Are you sure you want to exit?", frame)) {
                    new ProjectManager().getFrame().setLocationRelativeTo(frame);
                    frame.dispose();
                }
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu());
        frame.setJMenuBar(menuBar);

        frame.setMinimumSize(new Dimension(200, 200));
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.repaint();
    }

    public void updateProjectDir() {
        File assetsDir = new File(projectDir, "Assets");
        assetsDir.mkdir();

        File scriptsDir = new File(projectDir, "Scripts");
        scriptsDir.mkdir();


        File libsDir = new File(projectDir, "Libraries");
        libsDir.mkdir();
    }

    private JMenu fileMenu() {
        JMenu menu = new JMenu("File");

        menu.add("test");

        menu.add(new AbstractAction("Project...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.newDialog(project, frame, new Dimension(600, 400));
            }
        });

        return menu;
    }

    public void updateTitle() {
        frame.setTitle("Yield Editor | " + project.getName());
    }
}
