package com.xebisco.yield2d.editor;

import javax.swing.*;
import java.awt.*;

class SmartScrollablePanel extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }

        @Override
        public boolean getScrollableTracksViewportHeight() { return false; }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            // If the parent window is larger than 250px, stretch to fit (wrap text).
            // If smaller, stop stretching and show horizontal scrollbar.
            return getParent() != null && getParent().getWidth() > 250;
        }
    }