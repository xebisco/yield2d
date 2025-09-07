package com.xebisco.yield2d.editor;

import com.xebisco.yield2d.engine.*;
import com.xebisco.yield2d.engine.Container;
import com.xebisco.yield2d.engine.desktop.DesktopFileHandler;
import com.xebisco.yield2d.engine.openalimpl.OpenALAudioHandler;
import com.xebisco.yield2d.engine.openglimpl.AWTInputHandler;
import com.xebisco.yield2d.engine.openglimpl.OpenGLGraphicsHandler;
import com.xebisco.yield2d.engine.openglimpl.WindowProperties;

import javax.swing.*;
import java.awt.*;

public class EntityEditor extends JPanel {

    //TEST
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        EntityEditor entityEditor = new EntityEditor();
        frame.add(entityEditor);

        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public EntityEditor() {
        startEngine();
    }

    public void startEngine() {
        setLayout(new BorderLayout());
        Application app = new Application(Application.Type.EMPTY);

        WindowProperties p = new WindowProperties();
        p.setCreateWindow(false);
        OpenGLGraphicsHandler gh = new OpenGLGraphicsHandler(p);

        app.setGraphicsHandler(gh);
        app.setInputHandler(new AWTInputHandler());
        app.setAudioHandler(new OpenALAudioHandler());
        app.setFileHandler(new DesktopFileHandler());

        app.load();

        app.getSceneHandler().setActualScene(new Scene());

        Container c = new Container(new Script[]{
                new TextDrawerScript("TEST ON OUT WINDOW")
        });

        app.getSceneHandler().getActualScene().getChildren().add(c);

        app.getSceneHandler().getActualScene().load();
        app.init();

        SwingUtilities.invokeLater(() -> add(gh.getMainPanel()));
    }
}
