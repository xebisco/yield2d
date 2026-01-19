package com.xebisco.yield2d.editor.projects;

import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class MyListCellRenderer extends JLabel implements ListCellRenderer<Project> {

    private BufferedImage image;

    public MyListCellRenderer() {
        setOpaque(true);
        image = new BufferedImage(140, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        try {
            g.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("/projicon.png"))), 0, 0, 140, 100, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        g.dispose();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Project> list,
                                                  Project value,
                                                  int index, 
                                                  boolean isSelected, 
                                                  boolean cellHasFocus) {

        // Configure the component with data from the MyListItem object
        setText(value.getName());
        setHorizontalAlignment(CENTER);
        setVerticalTextPosition(BOTTOM);
        setHorizontalTextPosition(CENTER);
        setIcon(new ImageIcon(image));

        // FlatLaf automatically handles most of this, but you can add custom logic:
        if (isSelected) {
            // FlatLaf uses properties like "List.selectionBackground" and "List.selectionForeground"
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setBorder(new FlatLineBorder(new Insets(5, 5, 5, 5), null, 0, 20));
        return this;
    }
}
