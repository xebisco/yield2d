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
                new ParticleEmitterScript()
                        .setGravity(new Vector2f(0, 20))
                        .setSpeedNoise(new Vector2f(20, 20))
                        .setStartSpeed(new Vector2f(0, 0))
                        .setEmissionRatePerSecond(20f)
                        .setSize(new Vector2f(20, 20))
                        .setMaxLifeSeconds(.6f)
                        .setStartColor(new Color(1, .5f, 0, 1))
                        .setEndColor(new Color(1, 0, 0, 0))
                        .setSizeNoise(new Vector2f(10f, 10f))
                        .setInstantiateParticlesInParent(true)
                ,
                new MeshDrawerScript().setExtraScale(new Vector2f(100f, 100f)),
                new S(),
                new PhysicsBody(),
                new BoxCollider().setSize(new Vector2f(100, 100))
        });
        app.getSceneHandler().getActualScene().addChild(c);

        Container c1 = new Container(new Script[]{
                new MeshDrawerScript().setExtraScale(new Vector2f(100, 10)),
                new PhysicsBody().setType(PhysicsBody.Type.STATIC),
                new LineCollider().setV1(new Vector2f(-50, 0)).setV2(new Vector2f(50, 0)).setCollisionCategory("ground"),
                new LineCollider().setV1(new Vector2f(50, 0)).setV2(new Vector2f(150, 0))
        });
        c1.getTransform().translate(new Vector2f(0, -100));

        app.getSceneHandler().getActualScene().addChild(c1);

        app.init();
    }

    static class S extends Script {
        @Override
        public void update(TimeSpan elapsed) {
            Vector2f p = getAxisValue2f("HORIZONTAL", "VERTICAL").multiply(100 * elapsed.getSeconds());
            getScript(PhysicsBody.class).applyLinearImpulse(p);
        }
    }
}
