import com.xebisco.yield2d.engine.*;
import com.xebisco.yield2d.engine.desktop.DesktopFileHandler;
import com.xebisco.yield2d.engine.openalimpl.OpenALAudioHandler;
import com.xebisco.yield2d.engine.openglimpl.AWTInputHandler;
import com.xebisco.yield2d.engine.openglimpl.OpenGLGraphicsHandler;
import com.xebisco.yield2d.engine.openglimpl.WindowProperties;

import java.util.Random;

public class Hello {
    public static void main(String[] args) {
        Application app = new Application(Application.Type.GAME);

        WindowProperties props = new WindowProperties();

        app.setGraphicsHandler(new OpenGLGraphicsHandler(props));
        app.setInputHandler(new AWTInputHandler());
        app.setAudioHandler(new OpenALAudioHandler());
        app.setFileHandler(new DesktopFileHandler());

        app.load();

        Scene scene = new Scene();

        scene.getCameraScript().setBackground(Colors.LIGHT_BLUE);

        scene.addChild(new Container(new Script[]{
                new TextDrawer("O RAHMAN É GAY"),
                new Script() {
                    @InjectScript
                    TextDrawer td;
                    Random rd = new Random();

                    @Override
                    public void update(TimeSpan elapsed) {
                        getTransform().getPosition().setX(getApplication().getInputHandler().getMouse().getX() * 1280 / 2);
                        getTransform().getPosition().setY(getApplication().getInputHandler().getMouse().getY() * 720 / 2);
                        getTransform().scale(getAxisValue2f("HORIZONTAL", "VERTICAL").multiply(.1f));
                        getTransform().rotate(1f);
                        td.setCharactersRotation(td.getCharactersRotation() - 1);
                        td.setColor(new Color(rd.nextInt()));
                    }
                }
        }));

        scene.addChild(new Container(new Script[]{
                new TextDrawer("bíblia:"),
                new ParticleEmitter()
                        .setMaxLifeSeconds(10f)
                        .setEmissionRate(100f)
                        .setSize(new Vector2f(10f, 10f))
                        .setStartSpeedNoise(new Vector2f(100f, 100f))
                        .setGravity(new Vector2f(0, -900f))
                        .setEndColor(new Color(1, 1, 1, 0))
                        .setTimeToSpawn(2)
                        .setPopulate(true)
        }));

        app.getSceneHandler().setActualScene(scene);

        app.init();
    }
}
