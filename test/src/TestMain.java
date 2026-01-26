import com.xebisco.yield2d.engine.*;
import com.xebisco.yield2d.engine.openalimpl.OpenALAudioHandler;
import com.xebisco.yield2d.engine.openglimpl.AWTInputHandler;
import com.xebisco.yield2d.engine.openglimpl.OpenGLGraphicsHandler;
import com.xebisco.yield2d.engine.desktop.DesktopFileHandler;
import com.xebisco.yield2d.engine.physics.*;

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
                new MeshDrawer().setExtraScale(new Vector2f(100f, 100f)),
                new S(),
                new PhysicsBody().setInertia(10f),
                new BoxCollider().setSize(new Vector2f(100, 100))
        });
        app.getSceneHandler().getActualScene().addChild(c);

        Container particles = new Container(new Script[] {
                new ParticleEmitter()
                        .setEmissionRatePerSecond(60f).setPopulate(false),
                new Follow(c)
                /*
                .setGravity(new Vector2f(0, 500))
                        .setSpeedNoise(new Vector2f(100, 100))
                        .setStartSpeed(new Vector2f(0, 0))
                        .setEmissionRatePerSecond(60f)
                        .setSize(new Vector2f(150, 150))
                        .setMaxLifeSeconds(.01f)
                        .setStartColor(new Color(0, .5f, 1, .1f))
                        .setEndColor(new Color(0, 1, 1, 0))
                        .setSizeNoise(new Vector2f(30, 30))
                        .setMaxLifeSeconds(1f)
                        .setPointNoise(new Vector2f(100, 100))
                        .setPopulate(true),
                 */
        });
        app.getSceneHandler().getActualScene().addChild(particles);

        Container c1 = new Container(new Script[]{
                new MeshDrawer().setExtraScale(new Vector2f(100, 10)),
                new PhysicsBody().setType(PhysicsBody.Type.STATIC),
                new BoxCollider().setSize(new Vector2f(100, 10))
        });
        c1.getTransform().translate(new Vector2f(0, -100));

        app.getSceneHandler().getActualScene().addChild(c1);

        app.init();
    }

    static class S extends Script {
        @InjectScript
        private PhysicsBody pb;

        @Override
        public void update(TimeSpan elapsed) {
            Debug.println(1 / elapsed.getSeconds());
            Vector2f p = getAxisValue2f("HORIZONTAL", "VERTICAL").multiply(100 * elapsed.getSeconds());
            pb.applyLinearImpulse(p);
        }
    }

    static class Follow extends Script {
        private final Container toFollow;
        @InjectScript
        private ParticleEmitter pe;

        public Follow(Container toFollow) {
            this.toFollow = toFollow;
        }

        @Override
        public void update(TimeSpan elapsed) {
            pe.getPoint().setX(toFollow.getTransform().getPosition().getX());
            pe.getPoint().setY(toFollow.getTransform().getPosition().getY());
        }
    }
}
