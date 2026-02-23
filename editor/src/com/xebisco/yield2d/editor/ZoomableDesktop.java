package com.xebisco.yield2d.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ZoomableDesktop extends JPanel {
    private final DesktopPane desktopPane;
    private double zoomFactor = 1.0;
    private double xOffset = 0;
    private double yOffset = 0;
    private Point lastMousePos;

    public ZoomableDesktop(DesktopPane desktopPane) {
        this.setLayout(new BorderLayout());
        this.desktopPane = desktopPane;
        desktopPane.setDesktopManager(new ZoomEnabledDesktopManager());

        add(desktopPane);

        addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                double oldZoom = zoomFactor;
                if (e.getWheelRotation() < 0) zoomFactor *= 1.1;
                else zoomFactor /= 1.1;

                zoomFactor = Math.max(0.5, Math.min(zoomFactor, 5.0));

                double mouseX = e.getX();
                double mouseY = e.getY();

                xOffset = mouseX - (mouseX - xOffset) * (zoomFactor / oldZoom);
                yOffset = mouseY - (mouseY - yOffset) * (zoomFactor / oldZoom);

                revalidate();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    lastMousePos = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    desktopPane.setSelectedFrame(null);
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    int dx = e.getX() - lastMousePos.x;
                    int dy = e.getY() - lastMousePos.y;

                    xOffset += dx;
                    yOffset += dy;

                    lastMousePos = e.getPoint();

                    revalidate();
                    repaint();
                }
            }
        });
    }

    public void addFrameToDesktop(JInternalFrame frame, int x, int y, int w, int h) {
        Rectangle originalBounds = new Rectangle(x, y, w, h);
        frame.putClientProperty("originalBounds", originalBounds);
        desktopPane.add(frame);
        frame.setVisible(true);
        repaint();
    }

    private void layoutFrames() {
        for (Component comp : desktopPane.getComponents()) {
            if (comp instanceof JInternalFrame) {
                JInternalFrame frame = (JInternalFrame) comp;
                if (frame.isMaximum()) continue;
                Rectangle orig = (Rectangle) frame.getClientProperty("originalBounds");

                if (orig != null) {
                    // 1. Calculate Screen corners from World corners
                    int newX = (int) Math.round(orig.x * zoomFactor + xOffset);
                    int newY = (int) Math.round(orig.y * zoomFactor + yOffset);

                    int rightX = (int) Math.round((orig.x + orig.width) * zoomFactor + xOffset);
                    int bottomY = (int) Math.round((orig.y + orig.height) * zoomFactor + yOffset);

                    // 2. Derive Screen dimensions from those corners
                    int newW = rightX - newX;
                    int newH = bottomY - newY;

                    frame.setBounds(newX, newY, newW, newH);
                }
            }
        }

        updateInfiniteSize(); // Make sure the desktop grows if needed
    }

    private void updateInfiniteSize() {
        int maxX = 2000; // Minimum floor
        int maxY = 2000;

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            Rectangle r = frame.getBounds();
            maxX = Math.max(maxX, r.x + r.width + 500); // 500px padding
            maxY = Math.max(maxY, r.y + r.height + 500);
        }

        desktopPane.setPreferredSize(new Dimension(maxX, maxY));
        desktopPane.revalidate();
    }

    @Override
    protected void paintChildren(Graphics g) {
        g.setColor(UIManager.getColor("Desktop.background"));
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D) g.create();

        layoutFrames();

        super.paintChildren(g2);

        g2.translate(xOffset, yOffset);
        g2.scale(zoomFactor, zoomFactor);

        g2.dispose();
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public ZoomableDesktop setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        return this;
    }

    public double getxOffset() {
        return xOffset;
    }

    public ZoomableDesktop setxOffset(double xOffset) {
        this.xOffset = xOffset;
        return this;
    }

    public double getyOffset() {
        return yOffset;
    }

    public ZoomableDesktop setyOffset(double yOffset) {
        this.yOffset = yOffset;
        return this;
    }

    public Point getLastMousePos() {
        return lastMousePos;
    }

    public ZoomableDesktop setLastMousePos(Point lastMousePos) {
        this.lastMousePos = lastMousePos;
        return this;
    }

    public DesktopPane getDesktopPane() {
        return desktopPane;
    }

    public class ZoomEnabledDesktopManager extends DefaultDesktopManager {
        @Override
        public void dragFrame(JComponent f, int newX, int newY) {
            // Let Swing move the frame physically on the screen first
            super.dragFrame(f, newX, newY);

            if (f instanceof JInternalFrame) {
                JInternalFrame frame = (JInternalFrame) f;
                // Pass the guaranteed NEW screen coordinates, keep current screen width/height
                updateOriginalBounds(frame, newX, newY, frame.getWidth(), frame.getHeight());
            }
        }

        @Override
        public void resizeFrame(JComponent f, int newX, int newY, int newW, int newH) {
            // Let Swing resize the frame physically first
            super.resizeFrame(f, newX, newY, newW, newH);

            if (f instanceof JInternalFrame) {
                JInternalFrame frame = (JInternalFrame) f;
                // Pass the guaranteed NEW screen coordinates AND dimensions
                updateOriginalBounds(frame, newX, newY, newW, newH);
            }
        }

        private void updateOriginalBounds(JInternalFrame frame, int screenX, int screenY, int screenW, int screenH) {
            // 1. Calculate the exact Screen corners
            double rightX = screenX + screenW;
            double bottomY = screenY + screenH;

            // 2. Convert BOTH corners to World coordinates independently
            int worldX = (int) Math.round((screenX - xOffset) / zoomFactor);
            int worldY = (int) Math.round((screenY - yOffset) / zoomFactor);

            int worldRight = (int) Math.round((rightX - xOffset) / zoomFactor);
            int worldBottom = (int) Math.round((bottomY - yOffset) / zoomFactor);

            // 3. Derive the World width and height from the anchored corners
            int worldW = worldRight - worldX;
            int worldH = worldBottom - worldY;

            frame.putClientProperty("originalBounds", new Rectangle(worldX, worldY, worldW, worldH));
        }
    }
}