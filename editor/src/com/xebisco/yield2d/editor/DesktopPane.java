package com.xebisco.yield2d.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class DesktopPane extends JDesktopPane {
    private ArrayList<Connection> connections = new ArrayList<>();

    public DesktopPane() {
        setOpaque(false);
        setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.GRAY);

        for (Connection c : connections)
            if (!c.getFrameA().isIcon() && !c.getFrameB().isIcon() && c.getFrameA().isVisible() && c.getFrameB().isVisible()) {
                drawCurve(g2, c.getFrameA(), c.getFrameB());
            }

        g2.dispose();
    }

    private void drawCurve(Graphics2D g2, JInternalFrame source, JInternalFrame target) {
        int x1 = source.getX() + source.getWidth() / 2;
        int y1 = source.getY() + source.getHeight() / 2;
        int x2 = target.getX() + target.getWidth() / 2;
        int y2 = target.getY() + target.getHeight() / 2;

        int ctrlX1 = x1 + (x2 - x1) / 2;
        int ctrlY1 = y1;
        int ctrlX2 = x1 + (x2 - x1) / 2;
        int ctrlY2 = y2;

        Path2D path = new Path2D.Double();
        path.moveTo(x1, y1);
        path.curveTo(ctrlX1, ctrlY1, ctrlX2, ctrlY2, x2, y2);
        g2.draw(path);
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public DesktopPane setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
        return this;
    }

    public static class Connection {
        private final JInternalFrame frameA, frameB;

        public Connection(JInternalFrame frameA, JInternalFrame frameB) {
            this.frameA = frameA;
            this.frameB = frameB;
        }

        public JInternalFrame getFrameA() {
            return frameA;
        }

        public JInternalFrame getFrameB() {
            return frameB;
        }
    }
}
