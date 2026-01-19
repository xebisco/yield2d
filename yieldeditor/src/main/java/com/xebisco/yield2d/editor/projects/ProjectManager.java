package com.xebisco.yield2d.editor.projects;

import com.xebisco.yield2d.editor.Editor;
import com.xebisco.yield2d.editor.ListItemTransferHandler;
import com.xebisco.yield2d.editor.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ProjectManager {
    private JFrame frame;
    private JList<Project> projectJList = new JList<>();
    private Workspace workspace = new Workspace();

    public ProjectManager() {
        Editor.EDITOR_DATA.addIfNullPackage("workspace", workspace);
        workspace = Editor.EDITOR_DATA.getPackage("workspace", Workspace.class);

        frame = new JFrame("Yield Editor Projects");

        JToolBar topToolbar = new JToolBar();
        topToolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topToolbar.add(new JMenuItem("Workspace"));

        //frame.add(topToolbar, BorderLayout.NORTH);

        JScrollPane projectScrollPane = new JScrollPane(projectJList);
        projectScrollPane.getVerticalScrollBar().setBlockIncrement(4);
        projectScrollPane.setBorder(null);
        projectScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        frame.add(projectScrollPane);

        JMenuBar mb = new JMenuBar();
        mb.add(fileMenu());
        frame.setJMenuBar(mb);

        DefaultListModel<Project> projects = new DefaultListModel<>();
        Editor.EDITOR_DATA.addIfNullPackage("projects", projects);
        //noinspection unchecked
        DefaultListModel<Project> loadedProjects = Editor.EDITOR_DATA.getPackage("projects", DefaultListModel.class);
        if (loadedProjects != null) projects = loadedProjects;
        DefaultListModel<Project> finalProjects = projects;

        projectJList.setModel(finalProjects);

        projectJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectJList.setBackground(frame.getBackground().darker());
        projectJList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        projectJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        projectJList.setVisibleRowCount(0);
        projectJList.setCellRenderer(new MyListCellRenderer());
        projectJList.setFixedCellWidth(170);
        projectJList.setFixedCellHeight(150);

        projectJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = projectJList.locationToIndex(e.getPoint());
                if (index == -1) return;
                Project project = projectJList.getModel().getElementAt(index);
                projectJList.setSelectedIndex(index);
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    openProject(project);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (project != null) {
                        JPopupMenu menu = new JPopupMenu(project.getName());
                        menu.add(new AbstractAction("Open Project") {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                openProject(project);
                            }
                        });

                        menu.addSeparator();

                        menu.add(new AbstractAction("Remove...") {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (Utils.yesno("Remove project from list? It won't delete it from your system.", frame))
                                    finalProjects.remove(index);
                            }
                        });

                        menu.show(projectJList, e.getX(), e.getY());
                    }
                }
            }
        });

        projectJList.setDropMode(DropMode.INSERT);
        projectJList.setDragEnabled(true);
        projectJList.setTransferHandler(new ListItemTransferHandler());


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JButton newProject = new JButton(new AbstractAction("New Project") {
            @Override
            public void actionPerformed(ActionEvent e) {
                newProject();
            }
        });
        newProject.setPreferredSize(new Dimension(150, 50));
        bottomPanel.add(newProject);
        frame.getRootPane().setDefaultButton(newProject);

        JButton openProject = new JButton(new AbstractAction("Open Project") {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProject(projectJList.getSelectedValue());
            }
        });
        openProject.setPreferredSize(new Dimension(150, 50));
        bottomPanel.add(openProject);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Editor.EDITOR_DATA.addPackage("projects", finalProjects);
                Editor.EDITOR_DATA.addPackage("workspace", workspace);
                super.windowClosing(e);
            }
        });

        frame.add(bottomPanel, BorderLayout.SOUTH);


        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        frame.setMinimumSize(new Dimension(500, 400));
        frame.setPreferredSize(new Dimension(600, 500));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void openProject(Project project) {
        if (project != null) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            new Editor(project, new File(workspace.getDirectory(), project.getName()));
        } else {
            JOptionPane.showMessageDialog(frame, "No project selected.");
        }
    }

    public void newProject() {
        Project project = Utils.newDialog(new Project(), frame, new Dimension(600, 400));
        if (project == null) return;

        File projectDir = new File(workspace.getDirectory(), project.getName());

        projectDir.mkdirs();

        ((DefaultListModel<Project>) projectJList.getModel()).addElement(project);
    }

    private JMenu fileMenu() {
        JMenu menu = new JMenu("File");

        menu.add(new AbstractAction("New Project...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                newProject();
            }
        });

        menu.addSeparator();

        menu.add(new AbstractAction("Workspace...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.newDialog(workspace, frame, null);
            }
        });

        return menu;
    }

    public JFrame getFrame() {
        return frame;
    }

    public ProjectManager setFrame(JFrame frame) {
        this.frame = frame;
        return this;
    }

    public JList<Project> getProjectJList() {
        return projectJList;
    }

    public ProjectManager setProjectJList(JList<Project> projectJList) {
        this.projectJList = projectJList;
        return this;
    }
}
