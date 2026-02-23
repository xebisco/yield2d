package com.xebisco.yield2d.editor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

public class FileField extends JPanel {
    private final JTextField pathField;
    private final JButton browseButton;
    private final JFileChooser fileChooser;

    public FileField(File file) {
        setLayout(new BorderLayout(5, 0));

        pathField = new JTextField();
        pathField.setText(file.getAbsolutePath());
        browseButton = new JButton("Browse");
        fileChooser = new JFileChooser();

        // 1. Browse Button Action
        browseButton.addActionListener(e -> {
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                pathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        // 2. Enable Drag-and-Drop functionality
        pathField.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked") List<File> droppedFiles = (List<File>) evt.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);
                    if (!droppedFiles.isEmpty()) {
                        pathField.setText(droppedFiles.get(0).getAbsolutePath());
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        add(pathField, BorderLayout.CENTER);
        add(browseButton, BorderLayout.EAST);
    }

    public void setFileFilter(String description, String... extensions) {
        fileChooser.setFileFilter(new FileNameExtensionFilter(description, extensions));
    }

    public void setSelectionMode(int mode) {
        fileChooser.setFileSelectionMode(mode);
    }

    public String getSelectedPath() {
        return pathField.getText();
    }

    public void setSelectedPath(String path) {
        pathField.setText(path);
    }
}