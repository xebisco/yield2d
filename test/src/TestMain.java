import com.xebisco.yield2d.engine.*;
import com.xebisco.yield2d.engine.openalimpl.OpenALAudioHandler;
import com.xebisco.yield2d.engine.openglimpl.AWTInputHandler;
import com.xebisco.yield2d.engine.openglimpl.OpenGLGraphicsHandler;
import com.xebisco.yield2d.engine.desktop.DesktopFileHandler;

public class TestMain {
    public static void main(String[] args) {
        Debug.setDebug(true);
        Debug.println("Hello, World!");

        Application app = new Application(Application.Type.GAME);

        app.setGraphicsHandler(new OpenGLGraphicsHandler());
        app.setInputHandler(new AWTInputHandler());
        app.setAudioHandler(new OpenALAudioHandler());
        app.setFileHandler(new DesktopFileHandler());

        app.load();

        app.getSceneHandler().setActualScene(new Scene());

        Container c = new Container(new Script[]{
                new ParticleEmitterScript(),
                new S()
        });

        app.getSceneHandler().getActualScene().getChildren().add(c);

        app.getSceneHandler().getActualScene().load();
        app.init();
    }

    static class S extends Script {
        @Override
        public void update(TimeSpan elapsed) {
            Vector2f p = getAxisValue2f("HORIZONTAL", "VERTICAL").multiply(500 * elapsed.getSeconds());
            getScript(ParticleEmitterScript.class, 0).getPoint().addLocal(p);
        }
    }
}
