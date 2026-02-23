package com.xebisco.yield2d.engine;

import java.io.OutputStream;
import java.io.PrintStream;

public class Debug {
    private static final OutputStream nullOutputStream = new OutputStream() {
        @Override
        public void write(int b) {

        }

        @Override
        public void write(byte[] b, int off, int len) {

        }
    };

    public static final PrintStream NULL_OUTPUT = new PrintStream(nullOutputStream);

    private static PrintStream out = NULL_OUTPUT, debugOut = System.err;

    private static boolean debug;

    public static void refreshOut() {
        if(isDebug()) {
            setOut(getDebugOut());
        } else {
            setOut(NULL_OUTPUT);
        }
    }

    public static void println(Object toPrint) {
        getOut().println(toPrint);
    }

    public static void print(Object toPrint) {
        getOut().print(toPrint);
    }

    public static PrintStream getOut() {
        return out;
    }

    private static void setOut(PrintStream out) {
        Debug.out = out;
    }

    public static PrintStream getDebugOut() {
        return debugOut;
    }

    public static void setDebugOut(PrintStream debugOut) {
        Debug.debugOut = debugOut;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Debug.debug = debug;
        refreshOut();
    }
}
