import com.xebisco.yield2d.engine.*;
import com.xebisco.yield2d.engine.desktop.DesktopFileHandler;
import com.xebisco.yield2d.engine.openalimpl.OpenALAudioHandler;
import com.xebisco.yield2d.engine.openglimpl.AWTInputHandler;
import com.xebisco.yield2d.engine.openglimpl.OpenGLGraphicsHandler;
import com.xebisco.yield2d.engine.physics.BoxCollider;
import com.xebisco.yield2d.engine.physics.PhysicsBody;

public class TestMain {
    public static void main(String[] args) throws Exception {
        Debug.setDebug(true);
        Debug.println("Hello, World!");

        Application app = new Application(Application.Type.GAME);
        //app.getLoop().setUpdateInterval(new TimeSpan(16_666_666));

        app.setGraphicsHandler(new OpenGLGraphicsHandler());
        app.setInputHandler(new AWTInputHandler());
        app.setAudioHandler(new OpenALAudioHandler());
        app.setFileHandler(new DesktopFileHandler());

        app.load();

        app.getSceneHandler().setActualScene(new Scene());

        TextureAtlasFile taf = app.loadTextureAtlas("img.json");

        Container c = new Container(new Script[]{
                //new MeshDrawer().setExtraScale(new Vector2f(100f, 100f)),
                new MeshDrawer().setClips(taf.getTextureAtlasInfo().getClips("anim")),
                new Tweener().setPlayingInfo(new TweeningInfo(
                        true,
                        new TweeningInfo.TweeningPoint(
                                MeshDrawer.class,
                                0,
                                "clipIndex",
                                0,
                                0,
                                0,
                                null,
                                null
                        ),
                        new TweeningInfo.TweeningPoint(
                                MeshDrawer.class,
                                0,
                                "clipIndex",
                                0,
                                2,
                                .1f,
                                TweeningInfo.Easing.LINEAR_CLIP,
                                null
                        )
                )),
                new S(),
                new PhysicsBody().setInertia(10f),
                new BoxCollider().setSize(new Vector2f(100, 100))
        });
        app.getSceneHandler().getActualScene().addChild(c);

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
        @InjectScript
        private TextDrawer td;

        @Override
        public void update(TimeSpan elapsed) {
            Vector2f p = getAxisValue2f("HORIZONTAL", "VERTICAL").multiply(100 * elapsed.getSeconds());
            pb.applyLinearImpulse(p);
        }
    }
}
