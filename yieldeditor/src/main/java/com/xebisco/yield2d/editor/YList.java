package com.xebisco.yield2d.editor;

import com.formdev.flatlaf.ui.FlatBorder;
import com.xebisco.yield2d.engine.AddList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class YList extends JPanel {
    private List<Object> value;
    private final Field listField;

    public YList(List<Object> value, Field listField) {
        this.value = value;
        this.listField = listField;
        reload();
    }

    private Integer selectedIndex;

    public void reload() {
        selectedIndex = null;
        removeAll();
        revalidate();
        setLayout(new BorderLayout());

        //setBorder(new FlatRoundBorder());

        JToolBar toolBar = new JToolBar();

        toolBar.setBorder(new FlatBorder());

        JButton addButton = new JButton();
        addButton.setAction(new AbstractAction("+") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!listField.isAnnotationPresent(AddList.class)) {
                    throw new IllegalStateException(listField + " hasn't AddList annotation.");
                }
                try {
                    Class<? extends AddList.AddListMethod> clazz = listField.getAnnotation(AddList.class).value();
                    value.add(clazz.getMethod("instance").invoke(clazz.getConstructor().newInstance()));
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                         InstantiationException ex) {
                    throw new RuntimeException(ex);
                }
                reload();
            }
        });
        toolBar.add(addButton);

        JButton remButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                setEnabled(selectedIndex != null);
                super.paintComponent(g);
            }
        };
        remButton.setAction(new AbstractAction("-") {
            @Override
            public void actionPerformed(ActionEvent e) {
                value.remove((int) selectedIndex);
                reload();
            }
        });

        toolBar.add(remButton);

        toolBar.add(Box.createHorizontalGlue());

        toolBar.add(new JLabel(listField.getName() + "   "));

        add(toolBar, BorderLayout.NORTH);

        JPanel mainP = new JPanel(new BorderLayout()), inner = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        int i = 0;

        if (value != null && !value.isEmpty()) {
            for (Object o : value) {
                addItem(inner, gbc, i, o);
                i++;
            }
            gbc.gridy = value.size() + 99;
            gbc.weighty = 1.0;
            inner.add(new JLabel(), gbc);

            /*JScrollPane scrollPane = new JScrollPane(inner, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    CompletableFuture.runAsync(() -> inner.setPreferredSize(new Dimension(scrollPane.getViewport().getWidth(), inner.getPreferredSize().height)));
                }
            });

             */
            inner.setOpaque(true);
            inner.setBackground(UIManager.getColor("List.background"));
            mainP.setBorder(new FlatBorder());
            mainP.add(inner);
            add(mainP);
        } else {
            JPanel p = new JPanel();
            //p.setLayout(new BorderLayout());
            JLabel emptyLabel = new JLabel("Empty");
            p.setOpaque(true);
            p.add(emptyLabel);
            p.setBackground(UIManager.getColor("List.background"));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setVerticalAlignment(SwingConstants.CENTER);
            JScrollPane scrollPane = new JScrollPane(p, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            //scrollPane.setBorder(new FlatButtonBorder());
            add(scrollPane);
        }
        repaint();
    }

    private void addItem(JPanel panel, GridBagConstraints gbc, int row, Object o) {
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1;

        JPanel compPanel = new JPanel(new BorderLayout());
        compPanel.setOpaque(false);

        JToolBar toolBar = new JToolBar() {
            @Override
            public void paintComponent(Graphics g) {
                if (selectedIndex != null && selectedIndex == row) {
                    setBackground(UIManager.getColor("List.selectionBackground"));
                } else {
                    setBackground(UIManager.getColor("List.background"));
                }
                super.paintComponent(g);
            }
        };
        toolBar.setOpaque(true);
        //toolBar.setBorder(new FlatButtonBorder());
        toolBar.add(new JButton(new AbstractAction(o.toString()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedIndex = row;
                panel.getParent().getParent().repaint();
            }
        }));
        compPanel.add(toolBar, BorderLayout.NORTH);

        toolBar.add(Box.createHorizontalGlue());

        JButton up = new JButton(new AbstractAction("/\\") {
            @Override
            public void actionPerformed(ActionEvent e) {
                value.remove(row);
                value.add(row - 1, o);
                reload();
            }
        });

        up.setEnabled(row > 0);
        toolBar.add(up);

        JButton down = new JButton(new AbstractAction("\\/") {
            @Override
            public void actionPerformed(ActionEvent e) {
                value.remove(row);
                value.add(row + 1, o);
                reload();
            }
        });

        down.setEnabled(row < value.size() - 1);
        toolBar.add(down);

        JPanel props = new JPanel(new BorderLayout());
        PropertiesPanel propertiesPanel = new PropertiesPanel(o, listField, true, true);
        if (PropertiesPanel.checkClass(o.getClass()) != null)
            propertiesPanel.setExtToApply(() -> {
                Object n = propertiesPanel.getValueField();
                if (!o.equals(n)) {
                    value.remove(row);
                    value.add(row, n);
                    reload();
                }
            });
        props.add(propertiesPanel);
        //props.setBorder(new FlatButtonBorder());
        compPanel.add(props);

        panel.add(compPanel, gbc);
    }

    public List<Object> getValue() {
        return value;
    }

    public YList setValue(List<Object> value) {
        this.value = value;
        return this;
    }
}
