package com.xebisco.yield2d.engine.openglimpl;

import com.xebisco.yield2d.engine.Debug;
import com.xebisco.yield2d.engine.InputHandler;
import com.xebisco.yield2d.engine.Key;
import com.xebisco.yield2d.engine.TimeSpan;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class AWTInputHandler extends InputHandler {

    private final Set<Key> pressedKeys = new HashSet<>();

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

    @Override
    public boolean isKeyPressed(Key key) {
        return pressedKeys.contains(key);
    }

    public static Key fromAWTKeyCodeToYieldKey(int keyCode) {
        return switch (keyCode) {
            case KeyEvent.VK_ENTER -> Key.KB_ENTER;
            case KeyEvent.VK_BACK_SPACE -> Key.KB_BACKSPACE;
            case KeyEvent.VK_TAB -> Key.KB_TAB;
            case KeyEvent.VK_CLEAR -> Key.KB_CLEAR;
            case KeyEvent.VK_SHIFT -> Key.KB_SHIFT;
            case KeyEvent.VK_CONTROL -> Key.KB_CONTROL;
            case KeyEvent.VK_ALT -> Key.KB_ALT;
            case KeyEvent.VK_CAPS_LOCK -> Key.KB_CAPSLOCK;
            case KeyEvent.VK_ESCAPE -> Key.KB_ESCAPE;
            case KeyEvent.VK_SPACE -> Key.KB_SPACE;
            case KeyEvent.VK_PAGE_UP -> Key.KB_PAGE_UP;
            case KeyEvent.VK_PAGE_DOWN -> Key.KB_PAGE_DOWN;
            case KeyEvent.VK_END -> Key.KB_END;
            case KeyEvent.VK_HOME -> Key.KB_HOME;
            case KeyEvent.VK_LEFT, KeyEvent.VK_KP_LEFT -> Key.KB_LEFT;
            case KeyEvent.VK_UP, KeyEvent.VK_KP_UP -> Key.KB_UP;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_KP_RIGHT -> Key.KB_RIGHT;
            case KeyEvent.VK_DOWN, KeyEvent.VK_KP_DOWN -> Key.KB_DOWN;
            case KeyEvent.VK_COMMA -> Key.KB_COMMA;
            case KeyEvent.VK_MINUS -> Key.KB_MINUS;
            case KeyEvent.VK_PERIOD -> Key.KB_PERIOD;
            case KeyEvent.VK_SLASH -> Key.KB_SLASH;
            case KeyEvent.VK_0 -> Key.KB_0;
            case KeyEvent.VK_1 -> Key.KB_1;
            case KeyEvent.VK_2 -> Key.KB_2;
            case KeyEvent.VK_3 -> Key.KB_3;
            case KeyEvent.VK_4 -> Key.KB_4;
            case KeyEvent.VK_5 -> Key.KB_5;
            case KeyEvent.VK_6 -> Key.KB_6;
            case KeyEvent.VK_7 -> Key.KB_7;
            case KeyEvent.VK_8 -> Key.KB_8;
            case KeyEvent.VK_9 -> Key.KB_9;
            case KeyEvent.VK_SEMICOLON -> Key.KB_SEMICOLON;
            case KeyEvent.VK_EQUALS -> Key.KB_EQUALS;
            case KeyEvent.VK_A -> Key.KB_A;
            case KeyEvent.VK_B -> Key.KB_B;
            case KeyEvent.VK_C -> Key.KB_C;
            case KeyEvent.VK_D -> Key.KB_D;
            case KeyEvent.VK_E -> Key.KB_E;
            case KeyEvent.VK_F -> Key.KB_F;
            case KeyEvent.VK_G -> Key.KB_G;
            case KeyEvent.VK_H -> Key.KB_H;
            case KeyEvent.VK_I -> Key.KB_I;
            case KeyEvent.VK_J -> Key.KB_J;
            case KeyEvent.VK_K -> Key.KB_K;
            case KeyEvent.VK_L -> Key.KB_L;
            case KeyEvent.VK_M -> Key.KB_M;
            case KeyEvent.VK_N -> Key.KB_N;
            case KeyEvent.VK_O -> Key.KB_O;
            case KeyEvent.VK_P -> Key.KB_P;
            case KeyEvent.VK_Q -> Key.KB_Q;
            case KeyEvent.VK_R -> Key.KB_R;
            case KeyEvent.VK_S -> Key.KB_S;
            case KeyEvent.VK_T -> Key.KB_T;
            case KeyEvent.VK_U -> Key.KB_U;
            case KeyEvent.VK_V -> Key.KB_V;
            case KeyEvent.VK_W -> Key.KB_W;
            case KeyEvent.VK_X -> Key.KB_X;
            case KeyEvent.VK_Y -> Key.KB_Y;
            case KeyEvent.VK_Z -> Key.KB_Z;
            case KeyEvent.VK_OPEN_BRACKET -> Key.KB_OPEN_BRACKET;
            case KeyEvent.VK_BACK_SLASH -> Key.KB_BACK_SLASH;
            case KeyEvent.VK_CLOSE_BRACKET -> Key.KB_CLOSE_BRACKET;
            case KeyEvent.VK_NUMPAD0 -> Key.NUMPAD_0;
            case KeyEvent.VK_NUMPAD1 -> Key.NUMPAD_1;
            case KeyEvent.VK_NUMPAD2 -> Key.NUMPAD_2;
            case KeyEvent.VK_NUMPAD3 -> Key.NUMPAD_3;
            case KeyEvent.VK_NUMPAD4 -> Key.NUMPAD_4;
            case KeyEvent.VK_NUMPAD5 -> Key.NUMPAD_5;
            case KeyEvent.VK_NUMPAD6 -> Key.NUMPAD_6;
            case KeyEvent.VK_NUMPAD7 -> Key.NUMPAD_7;
            case KeyEvent.VK_NUMPAD8 -> Key.NUMPAD_8;
            case KeyEvent.VK_NUMPAD9 -> Key.NUMPAD_9;
            case KeyEvent.VK_SEPARATOR -> Key.KB_SEPARATOR;
            case KeyEvent.VK_SUBTRACT -> Key.KB_SUBTRACT;
            case KeyEvent.VK_DELETE -> Key.KB_DELETE;
            case KeyEvent.VK_NUM_LOCK -> Key.KB_NUM_LOCK;
            case KeyEvent.VK_SCROLL_LOCK -> Key.KB_SCROLL_LOCK;
            case KeyEvent.VK_F1 -> Key.KB_F1;
            case KeyEvent.VK_F2 -> Key.KB_F2;
            case KeyEvent.VK_F3 -> Key.KB_F3;
            case KeyEvent.VK_F4 -> Key.KB_F4;
            case KeyEvent.VK_F5 -> Key.KB_F5;
            case KeyEvent.VK_F6 -> Key.KB_F6;
            case KeyEvent.VK_F7 -> Key.KB_F7;
            case KeyEvent.VK_F8 -> Key.KB_F8;
            case KeyEvent.VK_F9 -> Key.KB_F9;
            case KeyEvent.VK_F10 -> Key.KB_F10;
            case KeyEvent.VK_F11 -> Key.KB_F11;
            case KeyEvent.VK_F12 -> Key.KB_F12;
            case KeyEvent.VK_F13 -> Key.KB_F13;
            case KeyEvent.VK_F14 -> Key.KB_F14;
            case KeyEvent.VK_F15 -> Key.KB_F15;
            case KeyEvent.VK_F16 -> Key.KB_F16;
            case KeyEvent.VK_F17 -> Key.KB_F17;
            case KeyEvent.VK_F18 -> Key.KB_F18;
            case KeyEvent.VK_F19 -> Key.KB_F19;
            case KeyEvent.VK_F20 -> Key.KB_F20;
            case KeyEvent.VK_F21 -> Key.KB_F21;
            case KeyEvent.VK_F22 -> Key.KB_F22;
            case KeyEvent.VK_F23 -> Key.KB_F23;
            case KeyEvent.VK_F24 -> Key.KB_F24;
            case KeyEvent.VK_PRINTSCREEN -> Key.KB_PRINT_SCREEN;
            case KeyEvent.VK_INSERT -> Key.KB_INSERT;
            case KeyEvent.VK_BACK_QUOTE -> Key.KB_BACK_QUOTE;
            case KeyEvent.VK_QUOTE -> Key.KB_QUOTE;
            case KeyEvent.VK_DEAD_GRAVE -> Key.KB_GRAVE;
            case KeyEvent.VK_DEAD_ACUTE -> Key.KB_ACUTE;
            case KeyEvent.VK_DEAD_CIRCUMFLEX, KeyEvent.VK_CIRCUMFLEX -> Key.KB_CIRCUMFLEX;
            case KeyEvent.VK_DEAD_TILDE -> Key.KB_TILDE;
            case KeyEvent.VK_AMPERSAND -> Key.KB_AMPERSAND;
            case KeyEvent.VK_ASTERISK, KeyEvent.VK_MULTIPLY -> Key.KB_ASTERISK;
            case KeyEvent.VK_QUOTEDBL -> Key.KB_DOUBLE_QUOTE;
            case KeyEvent.VK_LESS -> Key.KB_LESS;
            case KeyEvent.VK_GREATER -> Key.KB_GREATER;
            case KeyEvent.VK_BRACELEFT -> Key.KB_LEFT_BRACE;
            case KeyEvent.VK_BRACERIGHT -> Key.KB_RIGHT_BRACE;
            case KeyEvent.VK_AT -> Key.KB_AT;
            case KeyEvent.VK_COLON -> Key.KB_COLON;
            case KeyEvent.VK_DOLLAR -> Key.KB_DOLLAR;
            case KeyEvent.VK_EURO_SIGN -> Key.KB_EURO_SIGN;
            case KeyEvent.VK_EXCLAMATION_MARK -> Key.KB_EXCLAMATION_MARK;
            case KeyEvent.VK_INVERTED_EXCLAMATION_MARK -> Key.KB_INVERTED_EXCLAMATION_MARK;
            case KeyEvent.VK_LEFT_PARENTHESIS -> Key.KB_LEFT_PARENTHESIS;
            case KeyEvent.VK_PLUS, KeyEvent.VK_ADD -> Key.KB_PLUS;
            case KeyEvent.VK_RIGHT_PARENTHESIS -> Key.KB_RIGHT_PARENTHESIS;
            case KeyEvent.VK_UNDERSCORE -> Key.KB_UNDERSCORE;
            case KeyEvent.VK_CONTEXT_MENU -> Key.KB_CONTEXT_MENU;
            default -> Key.UNDEFINED;
        };
    }

    @Override
    public void load() {

    }

    @Override
    public void init() {
        if (getApplication().getGraphicsHandler() instanceof OpenGLGraphicsHandler gh) {
            gh.getMainPanel().addKeyListener(new AWTKeyAdapter());
        } else {
            Debug.println("WARNING: Could not link AWTInputHandler.");
        }
    }

    @Override
    public void update(TimeSpan elapsed) {

    }

    @Override
    public void destroy() {

    }
}
