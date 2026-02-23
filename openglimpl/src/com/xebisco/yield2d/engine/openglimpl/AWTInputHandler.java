package com.xebisco.yield2d.engine.openglimpl;

import com.xebisco.yield2d.engine.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class AWTInputHandler extends InputHandler {

    private final Set<Key> pressedKeys = new HashSet<>();
    private int mouseWheelUpTimer, mouseWheelDownTimer;
    private Vector2f lastMousePoint = new Vector2f();

    class AWTKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            pressedKeys.add(fromAWTKeyCodeToYieldKey(e.getKeyCode()));
        }

        @Override
        public void keyReleased(KeyEvent e) {
            pressedKeys.remove(fromAWTKeyCodeToYieldKey(e.getKeyCode()));
        }
    }

    class AWTMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            pressedKeys.add(fromAWTMouseButtonToYieldKey(e.getButton()));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            pressedKeys.remove(fromAWTMouseButtonToYieldKey(e.getButton()));
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getWheelRotation() < 0) {
                mouseWheelUpTimer = 2;
            } else {
                mouseWheelDownTimer = 2;
            }
        }
    }

    @Override
    public boolean isKeyPressed(Key key) {
        return pressedKeys.contains(key);
    }

    public static Key fromAWTKeyCodeToYieldKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_ENTER:
                return Key.KB_ENTER;
            case KeyEvent.VK_BACK_SPACE:
                return Key.KB_BACKSPACE;
            case KeyEvent.VK_TAB:
                return Key.KB_TAB;
            case KeyEvent.VK_CLEAR:
                return Key.KB_CLEAR;
            case KeyEvent.VK_SHIFT:
                return Key.KB_SHIFT;
            case KeyEvent.VK_CONTROL:
                return Key.KB_CONTROL;
            case KeyEvent.VK_ALT:
                return Key.KB_ALT;
            case KeyEvent.VK_CAPS_LOCK:
                return Key.KB_CAPSLOCK;
            case KeyEvent.VK_ESCAPE:
                return Key.KB_ESCAPE;
            case KeyEvent.VK_SPACE:
                return Key.KB_SPACE;
            case KeyEvent.VK_PAGE_UP:
                return Key.KB_PAGE_UP;
            case KeyEvent.VK_PAGE_DOWN:
                return Key.KB_PAGE_DOWN;
            case KeyEvent.VK_END:
                return Key.KB_END;
            case KeyEvent.VK_HOME:
                return Key.KB_HOME;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                return Key.KB_LEFT;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                return Key.KB_UP;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
                return Key.KB_RIGHT;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                return Key.KB_DOWN;
            case KeyEvent.VK_COMMA:
                return Key.KB_COMMA;
            case KeyEvent.VK_MINUS:
                return Key.KB_MINUS;
            case KeyEvent.VK_PERIOD:
                return Key.KB_PERIOD;
            case KeyEvent.VK_SLASH:
                return Key.KB_SLASH;
            case KeyEvent.VK_0:
                return Key.KB_0;
            case KeyEvent.VK_1:
                return Key.KB_1;
            case KeyEvent.VK_2:
                return Key.KB_2;
            case KeyEvent.VK_3:
                return Key.KB_3;
            case KeyEvent.VK_4:
                return Key.KB_4;
            case KeyEvent.VK_5:
                return Key.KB_5;
            case KeyEvent.VK_6:
                return Key.KB_6;
            case KeyEvent.VK_7:
                return Key.KB_7;
            case KeyEvent.VK_8:
                return Key.KB_8;
            case KeyEvent.VK_9:
                return Key.KB_9;
            case KeyEvent.VK_SEMICOLON:
                return Key.KB_SEMICOLON;
            case KeyEvent.VK_EQUALS:
                return Key.KB_EQUALS;
            case KeyEvent.VK_A:
                return Key.KB_A;
            case KeyEvent.VK_B:
                return Key.KB_B;
            case KeyEvent.VK_C:
                return Key.KB_C;
            case KeyEvent.VK_D:
                return Key.KB_D;
            case KeyEvent.VK_E:
                return Key.KB_E;
            case KeyEvent.VK_F:
                return Key.KB_F;
            case KeyEvent.VK_G:
                return Key.KB_G;
            case KeyEvent.VK_H:
                return Key.KB_H;
            case KeyEvent.VK_I:
                return Key.KB_I;
            case KeyEvent.VK_J:
                return Key.KB_J;
            case KeyEvent.VK_K:
                return Key.KB_K;
            case KeyEvent.VK_L:
                return Key.KB_L;
            case KeyEvent.VK_M:
                return Key.KB_M;
            case KeyEvent.VK_N:
                return Key.KB_N;
            case KeyEvent.VK_O:
                return Key.KB_O;
            case KeyEvent.VK_P:
                return Key.KB_P;
            case KeyEvent.VK_Q:
                return Key.KB_Q;
            case KeyEvent.VK_R:
                return Key.KB_R;
            case KeyEvent.VK_S:
                return Key.KB_S;
            case KeyEvent.VK_T:
                return Key.KB_T;
            case KeyEvent.VK_U:
                return Key.KB_U;
            case KeyEvent.VK_V:
                return Key.KB_V;
            case KeyEvent.VK_W:
                return Key.KB_W;
            case KeyEvent.VK_X:
                return Key.KB_X;
            case KeyEvent.VK_Y:
                return Key.KB_Y;
            case KeyEvent.VK_Z:
                return Key.KB_Z;
            case KeyEvent.VK_OPEN_BRACKET:
                return Key.KB_OPEN_BRACKET;
            case KeyEvent.VK_BACK_SLASH:
                return Key.KB_BACK_SLASH;
            case KeyEvent.VK_CLOSE_BRACKET:
                return Key.KB_CLOSE_BRACKET;
            case KeyEvent.VK_NUMPAD0:
                return Key.NUMPAD_0;
            case KeyEvent.VK_NUMPAD1:
                return Key.NUMPAD_1;
            case KeyEvent.VK_NUMPAD2:
                return Key.NUMPAD_2;
            case KeyEvent.VK_NUMPAD3:
                return Key.NUMPAD_3;
            case KeyEvent.VK_NUMPAD4:
                return Key.NUMPAD_4;
            case KeyEvent.VK_NUMPAD5:
                return Key.NUMPAD_5;
            case KeyEvent.VK_NUMPAD6:
                return Key.NUMPAD_6;
            case KeyEvent.VK_NUMPAD7:
                return Key.NUMPAD_7;
            case KeyEvent.VK_NUMPAD8:
                return Key.NUMPAD_8;
            case KeyEvent.VK_NUMPAD9:
                return Key.NUMPAD_9;
            case KeyEvent.VK_SEPARATOR:
                return Key.KB_SEPARATOR;
            case KeyEvent.VK_SUBTRACT:
                return Key.KB_SUBTRACT;
            case KeyEvent.VK_DELETE:
                return Key.KB_DELETE;
            case KeyEvent.VK_NUM_LOCK:
                return Key.KB_NUM_LOCK;
            case KeyEvent.VK_SCROLL_LOCK:
                return Key.KB_SCROLL_LOCK;
            case KeyEvent.VK_F1:
                return Key.KB_F1;
            case KeyEvent.VK_F2:
                return Key.KB_F2;
            case KeyEvent.VK_F3:
                return Key.KB_F3;
            case KeyEvent.VK_F4:
                return Key.KB_F4;
            case KeyEvent.VK_F5:
                return Key.KB_F5;
            case KeyEvent.VK_F6:
                return Key.KB_F6;
            case KeyEvent.VK_F7:
                return Key.KB_F7;
            case KeyEvent.VK_F8:
                return Key.KB_F8;
            case KeyEvent.VK_F9:
                return Key.KB_F9;
            case KeyEvent.VK_F10:
                return Key.KB_F10;
            case KeyEvent.VK_F11:
                return Key.KB_F11;
            case KeyEvent.VK_F12:
                return Key.KB_F12;
            case KeyEvent.VK_F13:
                return Key.KB_F13;
            case KeyEvent.VK_F14:
                return Key.KB_F14;
            case KeyEvent.VK_F15:
                return Key.KB_F15;
            case KeyEvent.VK_F16:
                return Key.KB_F16;
            case KeyEvent.VK_F17:
                return Key.KB_F17;
            case KeyEvent.VK_F18:
                return Key.KB_F18;
            case KeyEvent.VK_F19:
                return Key.KB_F19;
            case KeyEvent.VK_F20:
                return Key.KB_F20;
            case KeyEvent.VK_F21:
                return Key.KB_F21;
            case KeyEvent.VK_F22:
                return Key.KB_F22;
            case KeyEvent.VK_F23:
                return Key.KB_F23;
            case KeyEvent.VK_F24:
                return Key.KB_F24;
            case KeyEvent.VK_PRINTSCREEN:
                return Key.KB_PRINT_SCREEN;
            case KeyEvent.VK_INSERT:
                return Key.KB_INSERT;
            case KeyEvent.VK_BACK_QUOTE:
                return Key.KB_BACK_QUOTE;
            case KeyEvent.VK_QUOTE:
                return Key.KB_QUOTE;
            case KeyEvent.VK_DEAD_GRAVE:
                return Key.KB_GRAVE;
            case KeyEvent.VK_DEAD_ACUTE:
                return Key.KB_ACUTE;
            case KeyEvent.VK_DEAD_CIRCUMFLEX:
            case KeyEvent.VK_CIRCUMFLEX:
                return Key.KB_CIRCUMFLEX;
            case KeyEvent.VK_DEAD_TILDE:
                return Key.KB_TILDE;
            case KeyEvent.VK_AMPERSAND:
                return Key.KB_AMPERSAND;
            case KeyEvent.VK_ASTERISK:
            case KeyEvent.VK_MULTIPLY:
                return Key.KB_ASTERISK;
            case KeyEvent.VK_QUOTEDBL:
                return Key.KB_DOUBLE_QUOTE;
            case KeyEvent.VK_LESS:
                return Key.KB_LESS;
            case KeyEvent.VK_GREATER:
                return Key.KB_GREATER;
            case KeyEvent.VK_BRACELEFT:
                return Key.KB_LEFT_BRACE;
            case KeyEvent.VK_BRACERIGHT:
                return Key.KB_RIGHT_BRACE;
            case KeyEvent.VK_AT:
                return Key.KB_AT;
            case KeyEvent.VK_COLON:
                return Key.KB_COLON;
            case KeyEvent.VK_DOLLAR:
                return Key.KB_DOLLAR;
            case KeyEvent.VK_EURO_SIGN:
                return Key.KB_EURO_SIGN;
            case KeyEvent.VK_EXCLAMATION_MARK:
                return Key.KB_EXCLAMATION_MARK;
            case KeyEvent.VK_INVERTED_EXCLAMATION_MARK:
                return Key.KB_INVERTED_EXCLAMATION_MARK;
            case KeyEvent.VK_LEFT_PARENTHESIS:
                return Key.KB_LEFT_PARENTHESIS;
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_ADD:
                return Key.KB_PLUS;
            case KeyEvent.VK_RIGHT_PARENTHESIS:
                return Key.KB_RIGHT_PARENTHESIS;
            case KeyEvent.VK_UNDERSCORE:
                return Key.KB_UNDERSCORE;
            case KeyEvent.VK_CONTEXT_MENU:
                return Key.KB_CONTEXT_MENU;
            default:
                return Key.UNDEFINED;
        }
    }

    public static Key fromAWTMouseButtonToYieldKey(int button) {
        switch (button) {
            case 1:
                return Key.MOUSE_1;
            case 2:
                return Key.MOUSE_2;
            case 3:
                return Key.MOUSE_3;
            case 4:
                return Key.MOUSE_4;
            case 5:
                return Key.MOUSE_5;
            default:
                return Key.UNDEFINED;
        }
    }

    @Override
    public ImmutableVector2f getMouse() {
        float mx = 0, my = 0;
        GraphicsHandler gh0 = getApplication().getGraphicsHandler();
        if (gh0 instanceof OpenGLGraphicsHandler) {
            OpenGLGraphicsHandler gh = (OpenGLGraphicsHandler) gh0;
            Point p = gh.getCanvas().getMousePosition();
            if (p != null) {
                lastMousePoint.setX(mx = p.x / (gh.getCanvas().getWidth() / 2f) - 1f);
                lastMousePoint.setY(my = 1f - p.y / (gh.getCanvas().getHeight() / 2f));
            } else {
                mx = lastMousePoint.getX();
                my = lastMousePoint.getY();
            }
        } else {
            Debug.println("WARNING: Could not link AWTInputHandler.");
        }
        return new ImmutableVector2f(mx, my);
    }

    @Override
    public void load() {

    }

    @Override
    public void init() {
        if (getApplication().getGraphicsHandler() instanceof OpenGLGraphicsHandler) {
            OpenGLGraphicsHandler gh = (OpenGLGraphicsHandler) getApplication().getGraphicsHandler();
            gh.getMainPanel().addKeyListener(new AWTKeyAdapter());
            AWTMouseAdapter ma = new AWTMouseAdapter();
            gh.getCanvas().addMouseListener(ma);
            gh.getCanvas().addMouseWheelListener(ma);
        } else {
            Debug.println("WARNING: Could not link AWTInputHandler.");
        }
    }

    @Override
    public void update(TimeSpan elapsed) {
        mouseWheelUpTimer--;
        mouseWheelDownTimer--;

        if (mouseWheelDownTimer < 0) {
            pressedKeys.remove(Key.MOUSE_WDOWN);
        } else {
            pressedKeys.add(Key.MOUSE_WDOWN);
        }
        if (mouseWheelUpTimer < 0) {
            pressedKeys.remove(Key.MOUSE_WUP);
        } else {
            pressedKeys.add(Key.MOUSE_WUP);
        }
    }

    @Override
    public void fixedUpdate(TimeSpan elapsed) {

    }

    @Override
    public void destroy() {

    }
}
