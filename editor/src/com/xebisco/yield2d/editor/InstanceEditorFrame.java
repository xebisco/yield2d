package com.xebisco.yield2d.editor;

import com.xebisco.yield2d.engine.*;
import com.xebisco.yield2d.engine.Container;
import com.xebisco.yield2d.engine.desktop.DesktopFileHandler;
import com.xebisco.yield2d.engine.openalimpl.OpenALAudioHandler;
import com.xebisco.yield2d.engine.openglimpl.AWTInputHandler;
import com.xebisco.yield2d.engine.openglimpl.OpenGLGraphicsHandler;
import com.xebisco.yield2d.engine.openglimpl.WindowProperties;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InstanceEditorFrame extends JInternalFrame {

    private final Application app;
    private final OpenGLGraphicsHandler openGLGraphicsHandler;

    public InstanceEditorFrame() {
        super("Instance Editor", true, true, true, true);
        setLayout(new BorderLayout());
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                destroyEngine();
            }
        });
        SwingUtilities.invokeLater(() -> SwingUtilities.getWindowAncestor(this).addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                destroyEngine();
            }
        }));


        app = new Application(Application.Type.GAME);

        WindowProperties props = new WindowProperties().setCreateWindow(false).setViewportStyle(WindowProperties.ViewportStyle.FIT_ON_FRAME);

        openGLGraphicsHandler = new OpenGLGraphicsHandler(props);

        app.setGraphicsHandler(openGLGraphicsHandler);
        app.setInputHandler(new AWTInputHandler());
        app.setAudioHandler(new OpenALAudioHandler());
        app.setFileHandler(new DesktopFileHandler());

        app.load();

        app.getSceneHandler().setActualScene(new Scene());

        app.getSceneHandler().getActualScene().addChild(new Container(
                new Script[]{
                        new MeshDrawer().setExtraScale(new Vector2f(100, 100)).setColor(Colors.RED),
                        new Script() {
                            @Override
                            public void update(TimeSpan elapsed) {
                                getTransform().rotate(1);
                            }
                        }
                }
        ));

        app.init();

        add(openGLGraphicsHandler.getMainPanel(), BorderLayout.CENTER);
    }

    public void destroyEngine() {
        if (!app.isDestroyed())
            app.destroy();
    }
}
