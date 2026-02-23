import com.xebisco.yield2d.engine.*;
import com.xebisco.yield2d.engine.desktop.DesktopFileHandler;
import com.xebisco.yield2d.engine.openalimpl.OpenALAudioHandler;
import com.xebisco.yield2d.engine.openglimpl.AWTInputHandler;
import com.xebisco.yield2d.engine.openglimpl.OpenGLGraphicsHandler;

public class Hello {
    public static void main(String[] args) {
        Application app = new Application(Application.Type.GAME);

        app.setGraphicsHandler(new OpenGLGraphicsHandler());
        app.setAudioHandler(new OpenALAudioHandler());
        app.setInputHandler(new AWTInputHandler());
        app.setFileHandler(new DesktopFileHandler());

        app.load();

        Scene scene = new Scene();

        scene.addChild(new Container(
                new Script[]{
                        new TextDrawer().setContents("Hello, World!")
                }
        ));

        app.getSceneHandler().setActualScene(scene);

        app.init();
    }
}
