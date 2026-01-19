package com.xebisco.yield2d.editor;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.util.SystemInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Utils {
    public static void setupAndRun(Runnable runnable) {
        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }
        SwingUtilities.invokeLater(() -> {
            FlatDarculaLaf.setup();
            runnable.run();
        });
    }

    public static JPanel createLabeledSeparator(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel(text);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(label, gbc);

        JSeparator rightSep = new JSeparator(SwingConstants.HORIZONTAL);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 8, 0, 0);
        panel.add(rightSep, gbc);

        return panel;
    }

    public static String pStr(String str) {
        String output;
        output = str.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        if (Character.isLowerCase(output.charAt(0)))
            output = Character.toUpperCase(output.charAt(0)) + output.substring(1);
        return output;
    }

    public static <T> T newDialog(T o, Window parent, Dimension size) {
        JDialog dialog = new JDialog(parent, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setTitle(o.getClass().getSimpleName());
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        PropertiesPanel propertiesPanel = new PropertiesPanel(o, null, true, false);
        dialog.add(propertiesPanel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton okButton = new JButton(new AbstractAction("OK") {
            @Override
            public void actionPerformed(ActionEvent e) {
                propertiesPanel.apply();
                dialog.dispose();
            }
        });
        dialog.getRootPane().setDefaultButton(okButton);
        bottomPanel.add(okButton);

        JButton cancelButton = new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        bottomPanel.add(cancelButton);

        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setMinimumSize(new Dimension(200, 100));
        dialog.pack();if(size == null)
        dialog.setSize(new Dimension(350, dialog.getPreferredSize().height));
        else dialog.setSize(size);
        dialog.setLocationRelativeTo(parent);

        dialog.setVisible(true);
        if (propertiesPanel.isApplied())
            return o;
        else return null;
    }

    public static boolean yesno(String title, Window parent) {
        return JOptionPane.showConfirmDialog(parent, title, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
