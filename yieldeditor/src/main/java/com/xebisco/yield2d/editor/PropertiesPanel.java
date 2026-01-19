package com.xebisco.yield2d.editor;

import com.xebisco.yield2d.engine.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertiesPanel extends SmartScrollablePanel {

    interface CompC {
        Component create(Object old, Field field);

        Object apply(Component c);
    }

    private final static Map<Class<?>, CompC> comps = new HashMap<>();

    static {
        comps.put(String.class, new CompC() {
            @Override
            public Component create(Object old, Field field) {
                String t = old == null ? null : old.toString();
                return field.isAnnotationPresent(LongText.class) ? new JTextArea(t, 5, 0) : new JTextField(t);
            }

            @Override
            public Object apply(Component component) {
                return ((JTextComponent) component).getText();
            }
        });

        CompC booleanC = new CompC() {
            @Override
            public Component create(Object old, Field field) {
                JCheckBox checkBox = new JCheckBox(field.getName(), (boolean) old);
                checkBox.setText("<html><body>" + Utils.pStr(field.getName()));
                checkBox.setName("s");
                if (field.isAnnotationPresent(Desc.class))
                    checkBox.setToolTipText(field.getAnnotation(Desc.class).value());
                return checkBox;
            }

            @Override
            public Object apply(Component c) {
                return ((JCheckBox) c).isSelected();
            }
        };

        comps.put(boolean.class, booleanC);
        comps.put(Boolean.class, booleanC);

        CompC intC = new CompC() {
            @Override
            public Component create(Object old, Field field) {
                return createNumberField(NumberFormat.getIntegerInstance(), old);
            }

            @Override
            public Object apply(Component c) {
                return ((JFormattedTextField) c).getValue();
            }
        };

        comps.put(int.class, intC);
        comps.put(Integer.class, intC);

        comps.put(long.class, intC);
        comps.put(Long.class, intC);

        CompC fracC = new CompC() {
            @Override
            public Component create(Object old, Field field) {
                return createNumberField(NumberFormat.getInstance(), old);
            }

            @Override
            public Object apply(Component c) {
                return ((JFormattedTextField) c).getValue();
            }
        };

        comps.put(float.class, fracC);
        comps.put(Float.class, fracC);

        comps.put(double.class, fracC);
        comps.put(Double.class, fracC);

        CompC enumC = new CompC() {
            @Override
            public Component create(Object old, Field field) {
                JComboBox<?> comboBox = new JComboBox<>(field.getType().getEnumConstants());
                comboBox.setSelectedItem(old);
                return comboBox;
            }

            @Override
            public Object apply(Component c) {
                return ((JComboBox<?>) c).getSelectedItem();
            }
        };

        comps.put(Enum.class, enumC);

        CompC listC = new CompC() {
            @Override
            public Component create(Object old, Field field) {
                if (old == null) {
                    try {
                        old = field.getType().getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
                @SuppressWarnings("unchecked") YList yList = new YList((List<Object>) old, field);
                yList.setName("l");
                return yList;
            }

            @Override
            public Object apply(Component c) {
                return ((YList) c).getValue();
            }
        };

        comps.put(List.class, listC);

        CompC fileC = new CompC() {
            @Override
            public Component create(Object old, Field field) {
                FileField fileField = new FileField((File) old);
                if (field.isAnnotationPresent(DirsOnly.class))
                    fileField.setSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (field.isAnnotationPresent(FileExtension.class)) {
                    fileField.setFileFilter(
                            field.getAnnotation(FileExtension.class).name(),
                            field.getAnnotation(FileExtension.class).extensions()
                    );
                }
                return fileField;
            }

            @Override
            public Object apply(Component c) {
                return new File(((FileField) c).getSelectedPath());
            }
        };

        comps.put(File.class, fileC);
    }

    private static JFormattedTextField createNumberField(NumberFormat format, Object old) {
        JFormattedTextField numberField = new JFormattedTextField(format);
        numberField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (numberField.getValue().equals(0)) numberField.setText("");
                SwingUtilities.invokeLater(() -> numberField.setCaretPosition(numberField.getText().length()));
            }
        });
        numberField.setValue(old);
        return numberField;
    }

    private final Runnable toApply;
    private boolean requestApply;
    private boolean applied;

    public static CompC checkClass(Class<?> cl) {
        Class<?> ocl = cl;
        CompC c1;
        do {
            c1 = comps.get(cl);
            if (c1 == null) {
                for (Class<?> in : cl.getInterfaces()) {
                    c1 = comps.get(in);
                    if (c1 != null) break;
                }
            }
            if (c1 == null) {
                cl = cl.getSuperclass();
                if (cl == null) throw new NotSupportedClassException(ocl + " not supported.");
            }
        } while (c1 == null);
        return c1;
    }

    private final Map<Field, Component> components = new HashMap<>();
    private final Component valueComp;
    private final Class<?> valueClass;
    private Runnable extToApply;
    private List<Runnable> insideApply = new ArrayList<>();

    public PropertiesPanel(Object propsObj, Field field, boolean showTypes, boolean autoApply) {
        setLayout(new BorderLayout());
        JPanel innerPanel = new JPanel();
        add(innerPanel);
        innerPanel.setMinimumSize(new Dimension(270, 100));
        List<Pair<Field, Object>> fields = ReflectionUtils.getAllFieldsWithAnnotation(propsObj, Visible.class);
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();

        int addI = 0;
        for (int i = 0; i < fields.size(); i++) {
            Pair<Field, Object> f = fields.get(i);
            if (f.x().isAnnotationPresent(StartSection.class)) {
                addSeparator(innerPanel, gbc, i + addI++, f.x().getAnnotation(StartSection.class).value());
            }
            Class<?> cl = f.x().getType();
            Component c;
            try {
                CompC c1 = checkClass(cl);
                c = c1.create(f.y(), f.x());
            } catch (NotSupportedClassException e) {
                Object o = f.y();
                if (o == null) {
                    try {
                        o = f.x().getType().getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                Object finalO = o;
                c = new PropertiesPanel(o, f.x(), showTypes, autoApply);
                Component finalC = c;
                insideApply.add(() -> {
                    try {
                        f.x().set(propsObj, finalO);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                    ((PropertiesPanel) finalC).apply();
                });
            }

            if (c.getName() == null) {
                if (checkWrap(innerPanel, gbc, i + addI, 0)) addI++;
            } else addI--;
            addRow(innerPanel, gbc, i + addI, 0, c.getName() != null ? null : f.x().getName(), c, showTypes ? f.x().getType() : null);
            if (f.x().isAnnotationPresent(Desc.class) && c.getName() == null) {
                addDesc(innerPanel, gbc, i + ++addI, f.x().getAnnotation(Desc.class).value());
            }
            components.put(f.x(), c);
        }
        checkWrap(innerPanel, gbc, fields.size(), 0);

        if (fields.isEmpty()) {
            CompC c1 = checkClass(propsObj.getClass());
            if (c1 != null) {
                Component c = c1.create(propsObj, field);
                addRow(innerPanel, gbc, 0, 0, "value", c, showTypes ? propsObj.getClass() : null);
                valueComp = c;
                valueClass = propsObj.getClass();
            } else {
                throw new IllegalArgumentException(propsObj.getClass().toString());
            }
        } else {
            valueComp = null;
            valueClass = null;
        }

        toApply = () -> {
            for (Pair<Field, Object> f : fields) {
                try {
                    f.x().set(propsObj, checkClass(f.x().getType()).apply(components.get(f.x())));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (NotSupportedClassException ignore) {
                }
            }
        };

        gbc.gridy = fields.size() + 99;
        gbc.weighty = 1.0;
        innerPanel.add(new JLabel(), gbc);

        if (autoApply) {
            apply();
            Timer timer;
            if (!comps.containsKey(propsObj.getClass())) {
                timer = new Timer(100, a -> {
                    apply();
                });
                timer.start();
            } else {
                timer = null;
            }
            final boolean[] focused = {false};
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(
                    "focusOwner",
                    evt -> {
                        Component newOwner = (Component) evt.getNewValue();

                        if (focused[0]) {
                            focused[0] = false;
                            if (timer != null) {
                                timer.stop();
                            }
                            apply();
                        } else if (newOwner != null && SwingUtilities.isDescendingFrom(newOwner, PropertiesPanel.this)) {
                            focused[0] = true;
                        }
                    }
            );
        }
    }

    private JPanel wrapComps;

    public Object getValueField() {
        return checkClass(valueClass).apply(valueComp);
    }

    public void saveValueFieldState() {
        System.out.println(valueComp);
    }

    public void applyValueFieldState() {
        valueComp.transferFocus();
    }

    private void addSeparator(JPanel panel, GridBagConstraints gbc, int row, String sectionName) {
        JPanel separator = Utils.createLabeledSeparator(sectionName);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);

        panel.add(separator, gbc);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, int column, String text, Component field, Class<?> type) {

        if (text != null) {
            Dimension d = field.getPreferredSize();
            field.setPreferredSize(new Dimension(0, d.height > 0 ? d.height : 25));
            field.setMinimumSize(new Dimension(100, d.height));
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 1;
            // --- LABEL (30%) ---
            gbc.gridx = column * 2;
            gbc.gridy = row;
            gbc.weightx = 0.3;

            //ReflowingLabel label = new ReflowingLabel("<html><body>" + Utils.pStr(text)  + (type == null ? "" : "<small>  " + type.getSimpleName() + "</small>") + "</body></html>");
            ReflowingLabel label = new ReflowingLabel(Utils.pStr(text) + ":");
            panel.add(label, gbc);

            gbc.gridx = column * 2 + 1;
            gbc.gridy = row;
            gbc.weightx = 0.7;

            panel.add(field, gbc);
        } else {
            if (wrapComps == null) {
                wrapComps = new JPanel(new WrapLayout(FlowLayout.LEADING));
            }
            wrapComps.add(field);
        }


    }

    private void addDesc(JPanel panel, GridBagConstraints gbc, int row, String text) {

        if (text != null) {
            gbc.insets = new Insets(0, 5, 5, 5);
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 1;

            JLabel desc = new JLabel("<html><small>" + text);
            desc.setEnabled(false);

            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.weightx = 0.7;

            panel.add(desc, gbc);
        }
    }

    private boolean checkWrap(JPanel panel, GridBagConstraints gbc, int row, int column) {
        if (wrapComps != null) {
            if (wrapComps.getComponentCount() == 1) {
                Component c = wrapComps.getComponent(0);
                wrapComps.removeAll();
                wrapComps.revalidate();
                wrapComps.setLayout(new BorderLayout());
                wrapComps.add(c);
            }
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 2;
            gbc.gridx = column * 2;
            gbc.gridy = row;
            gbc.weightx = 1;

            panel.add(wrapComps, gbc);
            wrapComps = null;
            return true;
        }
        return false;
    }

    public void apply() {
        applied = true;
        toApply.run();
        if (extToApply != null) extToApply.run();
        insideApply.forEach(Runnable::run);
    }

    static class ReflowingLabel extends JTextArea {
        public ReflowingLabel(String text) {
            super(text);
            setFocusable(false);
            setOpaque(false);
            setLineWrap(true);
            setWrapStyleWord(true);
            setBorder(null);
            setFont(UIManager.getFont("Label.font"));
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            return new Dimension(0, size.height);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
    }

    public Runnable getExtToApply() {
        return extToApply;
    }

    public PropertiesPanel setExtToApply(Runnable extToApply) {
        this.extToApply = extToApply;
        return this;
    }

    public boolean isApplied() {
        return applied;
    }

    public PropertiesPanel setApplied(boolean applied) {
        this.applied = applied;
        return this;
    }
}