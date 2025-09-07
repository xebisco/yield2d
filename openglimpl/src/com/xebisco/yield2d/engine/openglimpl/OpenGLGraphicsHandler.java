package com.xebisco.yield2d.engine.openglimpl;

import com.xebisco.yield2d.engine.*;
import com.xebisco.yield2d.engine.Color;
import com.xebisco.yield2d.engine.Graphics;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL33.*;

public class OpenGLGraphicsHandler extends GraphicsHandler {

    public class OpenGLGraphics implements Graphics {
        @Override
        public void start() {
            modelMatrix.pushMatrix();
        }

        @Override
        public void end() {
            modelMatrix.popMatrix();
        }

        @Override
        public void drawMesh(Mesh2f mesh, Color color) {
            OpenGLMesh2f oglMesh = setupMesh(mesh);

            plainShader.bind();

            plainShader.setUniform("projectionMatrix", projectionMatrix);
            plainShader.setUniform("viewMatrix", viewMatrix);
            plainShader.setUniform("modelMatrix", modelMatrix);
            plainShader.setUniform("color", color);

            glBindVertexArray(oglMesh.getVaoId());
            glDrawElements(GL_TRIANGLES, oglMesh.getNumVertices(), GL_UNSIGNED_INT, 0);

            plainShader.unbind();
        }

        @Override
        public void drawMesh(Mesh2f mesh, TextureFile textureFile, Color color) {
            drawMeshWithTexture(mesh, setupTexture(textureFile), color);
        }

        public void drawMeshWithTexture(Mesh2f mesh, OpenGLTexture oglTexture, Color color) {
            OpenGLMesh2f oglMesh = setupMesh(mesh);

            textureShader.bind();

            textureShader.setUniform("projectionMatrix", projectionMatrix);
            textureShader.setUniform("viewMatrix", viewMatrix);
            textureShader.setUniform("modelMatrix", modelMatrix);
            textureShader.setUniform("color", color);

            int tid = oglTexture.getTextureId();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, tid);

            glBindVertexArray(oglMesh.getVaoId());
            glDrawElements(GL_TRIANGLES, oglMesh.getNumVertices(), GL_UNSIGNED_INT, 0);

            textureShader.unbind();
        }

        @Override
        public void drawText(String text, FontFile fontFile, Color color, float charRotation) {
            OpenGLFont oglFont = setupFont(fontFile);
            start();
            for (char c : text.toCharArray()) {
                oglFont.loadChar(c);
                OpenGLTexture texture = oglFont.glyphs.get(c);
                translate(-texture.width / 2f, 0);
            }
            for (char c : text.toCharArray()) {
                oglFont.loadChar(c);
                OpenGLTexture texture = oglFont.glyphs.get(c);
                translate(texture.width / 2f, 0);
                start();
                rotate(charRotation);
                scale(texture.width, texture.height);
                drawMeshWithTexture(MeshDrawerScript.DefaultMeshes.RECTANGLE.getValue(), texture, color);
                end();
                translate(texture.width / 2f, 0);
            }
            end();
        }

        @Override
        public void rotate(float angle) {
            modelMatrix.rotateZ((float) Math.toRadians(angle));
        }

        @Override
        public void scale(float x, float y) {
            modelMatrix.scaleXY(x, y);
        }

        @Override
        public void translate(float x, float y) {
            modelMatrix.translate(x, y, 0);
        }
    }

    public static class ShaderProgram {

        private final int programId;
        private final Map<String, Integer> uniformsCache = new HashMap<>();

        public ShaderProgram(List<ShaderModuleData> shaderModuleDataList) {
            programId = glCreateProgram();
            if (programId == 0) {
                throw new RuntimeException("Could not create Shader");
            }

            List<Integer> shaderModules = new ArrayList<>();
            shaderModuleDataList.forEach(s -> shaderModules.add(createShader(s.shaderCode, s.shaderType)));

            link(shaderModules);
        }

        public void createUniform(String uniformName) {
            int uniformLocation = glGetUniformLocation(programId, uniformName);
            if (uniformLocation < 0) {
                throw new RuntimeException("Could not find uniform [" + uniformName + "] in shader program [" +
                        programId + "]");
            }
            uniformsCache.put(uniformName, uniformLocation);
        }

        public void setUniform(String uniformName, Matrix4f value) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                Integer location = uniformsCache.get(uniformName);
                if (location == null) {
                    throw new RuntimeException("Could not find uniform [" + uniformName + "]");
                }
                glUniformMatrix4fv(location, false, value.get(stack.mallocFloat(16)));
            }
        }

        public void setUniform(String uniformName, Color value) {
            Integer location = uniformsCache.get(uniformName);
            if (location == null) {
                throw new RuntimeException("Could not find uniform [" + uniformName + "]");
            }
            glUniform4f(location, value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha());
        }

        public void bind() {
            glUseProgram(programId);
        }

        public void cleanup() {
            uniformsCache.clear();
            unbind();
            if (programId != 0) {
                glDeleteProgram(programId);
            }
        }

        protected int createShader(String shaderCode, int shaderType) {
            int shaderId = glCreateShader(shaderType);
            if (shaderId == 0) {
                throw new RuntimeException("Error creating shader. Type: " + shaderType);
            }

            glShaderSource(shaderId, shaderCode);
            glCompileShader(shaderId);

            if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
                throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
            }

            glAttachShader(programId, shaderId);

            return shaderId;
        }

        public int getProgramId() {
            return programId;
        }

        private void link(List<Integer> shaderModules) {
            glLinkProgram(programId);
            if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
                throw new RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
            }

            shaderModules.forEach(s -> glDetachShader(programId, s));
            shaderModules.forEach(GL33::glDeleteShader);
        }

        public void unbind() {
            glUseProgram(0);
        }

        public void validate() {
            glValidateProgram(programId);
            if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
                throw new RuntimeException("Error validating Shader code: " + glGetProgramInfoLog(programId, 1024));
            }
        }

        public record ShaderModuleData(String shaderCode, int shaderType) {
        }
    }

    public class OpenGLMesh2f {
        private int numVertices;
        private int vaoId;
        private final ArrayList<Integer> vboIdList = new ArrayList<>();
        private final int meshHashcode;

        public OpenGLMesh2f(Vector2f[] vertices, Vector2f[] vtexCoords, int[] indices, int meshHashcode) {
            numVertices = indices.length;
            this.meshHashcode = meshHashcode;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Positions VBO
            float[] positions = new float[vertices.length * 2];
            for (int i = 0; i < vertices.length; i++) {
                positions[i * 2] = vertices[i].getX();
                positions[i * 2 + 1] = vertices[i].getY();
            }
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer positionsBuffer = MemoryUtil.memCallocFloat(positions.length);
            positionsBuffer.put(0, positions);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

            if (vtexCoords != null) {
                // Texture coordinates VBO
                float[] texCoords = new float[vtexCoords.length * 2];
                for (int i = 0; i < vtexCoords.length; i++) {
                    texCoords[i * 2] = vtexCoords[i].getX();
                    texCoords[i * 2 + 1] = vtexCoords[i].getY();
                }
                vboId = glGenBuffers();
                vboIdList.add(vboId);
                FloatBuffer textCoordsBuffer = MemoryUtil.memCallocFloat(texCoords.length);
                textCoordsBuffer.put(0, texCoords);
                glBindBuffer(GL_ARRAY_BUFFER, vboId);
                glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
                glEnableVertexAttribArray(1);
                glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            }

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            IntBuffer indicesBuffer = MemoryUtil.memCallocInt(indices.length);
            indicesBuffer.put(0, indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

            MemoryUtil.memFree(positionsBuffer);
            MemoryUtil.memFree(indicesBuffer);
        }

        public void cleanup() {
            vboIdList.forEach(GL33::glDeleteBuffers);
            glDeleteVertexArrays(vaoId);
        }

        public int getNumVertices() {
            return numVertices;
        }

        public void setNumVertices(int numVertices) {
            this.numVertices = numVertices;
        }

        public int getVaoId() {
            return vaoId;
        }

        public void setVaoId(int vaoId) {
            this.vaoId = vaoId;
        }

        public ArrayList<Integer> getVboIdList() {
            return vboIdList;
        }

        public int getMeshHashcode() {
            return meshHashcode;
        }
    }

    public class CanvasImpl extends AWTGLCanvas {
        public CanvasImpl(GLData data) {
            super(data);
        }

        public void initGL() {
            Debug.println("OpenGL version: " + effective.majorVersion + "." + effective.minorVersion + " (Profile: " + effective.profile + ")");
            createCapabilities();

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_SCISSOR_TEST);

            int width = windowProperties.getViewportSize().getX(), height = windowProperties.getViewportSize().getY();

            projectionMatrix.ortho(-width / 2f, width / 2f, -height / 2f, height / 2f, 0, 1f);
        }

        public void runQueue() {
            while (!runOnOGLContext.isEmpty()) {
                runOnOGLContext.get(0).run();
                runOnOGLContext.remove(0);
            }
        }

        public void paintGL() {
            runQueue();

            camera = getApplication().getSceneHandler().getActualScene().getCameraScript();

            glClearColor(0, 0, 0, 1);

            glScissor(0, 0, getWidth(), getHeight());

            glClear(GL_COLOR_BUFFER_BIT);

            updateViewport();

            glClearColor(camera.getBackground().getRed(), camera.getBackground().getGreen(), camera.getBackground().getBlue(), 1);

            glClear(GL_COLOR_BUFFER_BIT);

            getApplication().getSceneHandler().getActualScene().draw(graphics);

            swapBuffers();
        }
    }

    public class OpenGLTexture {
        private int width, height, textureId = -1;
        private final TextureFile.TextureFilter filter;

        public OpenGLTexture(InputStream imageInputStream, TextureFile.TextureFilter filter) throws IOException {
            this(ImageIO.read(imageInputStream), filter);
        }

        public OpenGLTexture(BufferedImage image, TextureFile.TextureFilter filter) {
            this.filter = filter;
            ByteBuffer buffer = loadImage(image);
            runOnOGLContext.add(() -> generateTexture(width, height, buffer));
        }

        public ByteBuffer loadImage(BufferedImage image) {
            width = image.getWidth();
            height = image.getHeight();
            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4); // 4 bytes per pixel (RGBA)

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                    buffer.put((byte) (pixel & 0xFF));         // Blue
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
                }
            }
            buffer.flip();

            image.flush();
            return buffer;
        }

        private void generateTexture(int width, int height, ByteBuffer buffer) {
            textureId = glGenTextures();
            int filter = this.filter == TextureFile.TextureFilter.LINEAR ? GL_LINEAR : GL_NEAREST;

            glBindTexture(GL_TEXTURE_2D, textureId);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);
        }

        public void unload() {
            glDeleteTextures(textureId);
        }

        public int getTextureId() {
            return textureId;
        }
    }

    public class OpenGLFont {
        private final Map<Character, OpenGLTexture> glyphs = new HashMap<>();
        private final float size;
        private final boolean antialias;

        private final TextureFile.TextureFilter filter;
        private final Font font;

        public OpenGLFont(InputStream fontInputStream, TextureFile.TextureFilter filter, boolean antialias, float size) {
            this.filter = filter;
            this.antialias = antialias;
            this.size = size;
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, fontInputStream).deriveFont(size);
            } catch (FontFormatException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void loadChar(char c) {
            if (font.canDisplay(c)) {
                if (!glyphs.containsKey(c))
                    glyphs.put(c, new OpenGLTexture(createCharImage(font, c), filter));
            } else {
                Debug.println("WARNING: Can't display '" + c + "'");
            }
        }

        private BufferedImage createCharImage(java.awt.Font font, char c) {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics();
            g.dispose();

            int charWidth = metrics.charWidth(c);
            int charHeight = metrics.getHeight();

            image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
            g = image.createGraphics();
            g.setFont(font);
            if (antialias)
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setColor(java.awt.Color.WHITE);
            g.drawString(String.valueOf(c), 0, metrics.getAscent());
            g.dispose();
            return image;
        }
    }

    private JFrame frame;
    private JPanel mainPanel;
    private CanvasImpl canvas;
    private final Graphics graphics = new OpenGLGraphics();
    private CameraScript camera;

    private final WindowProperties windowProperties;

    private Map<Mesh2f, OpenGLMesh2f> meshCache;
    private Map<TextureFile, OpenGLTexture> textureCache;
    private Map<FontFile, OpenGLFont> fontCache;
    private Matrix4f projectionMatrix = new Matrix4f(), viewMatrix = new Matrix4f();
    private GrowableMat4fStack modelMatrix = new GrowableMat4fStack(10);
    private ShaderProgram plainShader, textureShader;
    private List<Runnable> runOnOGLContext = new ArrayList<>();

    public OpenGLGraphicsHandler() {
        this(new WindowProperties());
    }

    public OpenGLGraphicsHandler(WindowProperties windowProperties) {
        this.windowProperties = windowProperties;
    }

    public void updateViewport() {
        int x = 0, y = 0, w = canvas.getWidth(), h = canvas.getHeight();

        if (windowProperties.getViewportStyle() == WindowProperties.ViewportStyle.SCALE_TO_FIT) {
            double aspectRatio = (double) w / h;
            double desAspectRatio = (double) windowProperties.getViewportSize().getX() / windowProperties.getViewportSize().getY();

            if (aspectRatio > desAspectRatio) {
                w = (int) (h * desAspectRatio);
                x = canvas.getWidth() / 2 - w / 2;
            } else {
                h = (int) (w / desAspectRatio);
                y = canvas.getHeight() / 2 - h / 2;
            }
        }

        glViewport(x, y, w, h);
        glScissor(x, y, w, h);
    }

    public OpenGLMesh2f setupMesh(Mesh2f mesh) {
        OpenGLMesh2f oglMesh = meshCache.get(mesh);
        Integer hash = null;
        if (oglMesh == null || (oglMesh.meshHashcode != (hash = mesh.hashCode()))) {
            if (hash == null) hash = mesh.hashCode();
            oglMesh = new OpenGLMesh2f(mesh.getVertices(), mesh.getTextureCoords(), mesh.getIndices(), hash);
            meshCache.put(mesh, oglMesh);
            canvas.runQueue();
        }
        return oglMesh;
    }

    public OpenGLTexture setupTexture(TextureFile textureFile) {
        OpenGLTexture oglTexture = textureCache.get(textureFile);
        boolean loadTexture = textureFile.getInputStream() == null;
        if (oglTexture == null) {
            if (loadTexture)
                textureFile.setInputStream(getApplication().getFileHandler().openInputStream(textureFile.getPath()));
            try {
                oglTexture = new OpenGLTexture(textureFile.getInputStream(), textureFile.getFilter());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (loadTexture) getApplication().getFileHandler().closeInputStream(textureFile.getInputStream());
            textureCache.put(textureFile, oglTexture);
            canvas.runQueue();
        }
        return oglTexture;
    }

    public OpenGLFont setupFont(FontFile fontFile) {
        OpenGLFont oglFont = fontCache.get(fontFile);
        boolean loadTexture = fontFile.getInputStream() == null;
        if (oglFont == null) {
            if (loadTexture)
                fontFile.setInputStream(getApplication().getFileHandler().openInputStream(fontFile.getPath()));
            oglFont = new OpenGLFont(fontFile.getInputStream(), fontFile.getFilter(), fontFile.isAntiAliasing(), fontFile.getSize());
            if (loadTexture) getApplication().getFileHandler().closeInputStream(fontFile.getInputStream());
            fontCache.put(fontFile, oglFont);
            canvas.runQueue();
        }
        return oglFont;
    }

    @Override
    public void load() {
        meshCache = new HashMap<>();
        textureCache = new HashMap<>();
        fontCache = new HashMap<>();

        runOnOGLContext.add(() -> {
            List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
            shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("""
                    
                    #version 330
                    
                    layout (location=0) in vec2 position;
                    
                    uniform mat4 modelMatrix;
                    uniform mat4 viewMatrix;
                    uniform mat4 projectionMatrix;
                    
                    void main()
                    {
                        gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 0.0, 1.0);
                    }
                    
                    """, GL_VERTEX_SHADER));
            shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("""
                    
                    #version 330
                    
                    out vec4 fragColor;
                    uniform vec4 color;
                    
                    void main()
                    {
                        fragColor = color;
                    }
                    
                    """, GL_FRAGMENT_SHADER));

            plainShader = new ShaderProgram(shaderModuleDataList);
            plainShader.createUniform("modelMatrix");
            plainShader.createUniform("viewMatrix");
            plainShader.createUniform("projectionMatrix");
            plainShader.createUniform("color");

            shaderModuleDataList.clear();

            shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("""
                    
                    #version 330
                    
                    layout (location=0) in vec2 position;
                    layout (location=1) in vec2 texCoord;
                    
                    out vec2 outTextCoord;
                    
                    uniform mat4 projectionMatrix;
                    uniform mat4 viewMatrix;
                    uniform mat4 modelMatrix;
                    
                    void main()
                    {
                        gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 0.0, 1.0);
                        outTextCoord = texCoord;
                    }
                    
                    """, GL_VERTEX_SHADER));

            shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("""
                    
                    #version 330
                    
                    in vec2 outTextCoord;
                    
                    out vec4 fragColor;
                    
                    uniform sampler2D txtSampler;
                    uniform vec4 color;
                    
                    void main()
                    {
                        fragColor = texture(txtSampler, outTextCoord);
                        fragColor.r *= color.r;
                        fragColor.g *= color.g;
                        fragColor.b *= color.b;
                        fragColor.a *= color.a;
                    }
                    
                    """, GL_FRAGMENT_SHADER));

            textureShader = new ShaderProgram(shaderModuleDataList);
            textureShader.createUniform("modelMatrix");
            textureShader.createUniform("viewMatrix");
            textureShader.createUniform("projectionMatrix");
            textureShader.createUniform("txtSampler");
            textureShader.createUniform("color");
        });

        System.setProperty("sun.awt.noerasebackground", "true");
        Toolkit.getDefaultToolkit().setDynamicLayout(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setFocusable(true);

        GLData data = new GLData();
        data.doubleBuffer = windowProperties.isDoubleBuffered();
        data.swapInterval = windowProperties.isVerticalSync() ? 1 : 0;

        mainPanel.add(canvas = new CanvasImpl(data));
        canvas.setFocusable(false);
        canvas.setIgnoreRepaint(true);
    }

    @Override
    public void init() {
        if (windowProperties.isCreateWindow()) {
            frame = new JFrame();
            frame.setIgnoreRepaint(true);
            if (windowProperties.isWindowIcon()) {
                InputStream iconIs = getApplication().getFileHandler().openInputStream("icon.png");
                try {
                    frame.setIconImage(ImageIO.read(iconIs));
                } catch (IOException e) {
                    Debug.println("WARNING: Couldn't load 'icon.png'.");
                }
                getApplication().getFileHandler().closeInputStream(iconIs);
            }
            frame.setTitle(getApplication().getTitle());
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    getApplication().getLoop().getRunning().set(false);
                }
            });
            frame.add(mainPanel);

            switch (windowProperties.getWindowStyle()) {
                case PLAIN -> {
                    frame.setSize(windowProperties.getWindowSize().getX(), windowProperties.getWindowSize().getY());
                }
                case UNDECORATED -> {
                    frame.setSize(windowProperties.getWindowSize().getX(), windowProperties.getWindowSize().getY());
                    frame.setUndecorated(true);
                }
                case MAXIMIZED -> {
                    frame.setSize(windowProperties.getWindowSize().getX(), windowProperties.getWindowSize().getY());
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
                case FULLSCREEN -> {
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    GraphicsDevice gd = ge.getDefaultScreenDevice();
                    gd.setFullScreenWindow(frame);
                }
            }
            frame.setResizable(windowProperties.isResizable());

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.transferFocus();
            SwingUtilities.invokeLater(mainPanel::transferFocus);
        }
    }

    @Override
    public void update(TimeSpan elapsed) {
        if (canvas.isDisplayable()) {
            canvas.render();
        }
    }

    @Override
    public void destroy() {
        if (frame != null && frame.isDisplayable()) frame.dispose();
        plainShader.cleanup();
    }

    @Override
    public ImmutableVector2i getTextureSize(TextureFile textureFile) {
        OpenGLTexture oglTexture = setupTexture(textureFile);
        return new ImmutableVector2i(oglTexture.width, oglTexture.height);
    }

    @Override
    public void loadTextureAtlasInfo(TextureAtlasFile.TextureAtlas info) {
        InputStream inputStream = getApplication().getFileHandler().openInputStream(info.imagePath());
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getApplication().getFileHandler().closeInputStream(inputStream);

        for (TextureAtlasFile.Clip clip : info.clipMap().values()) {
            TextureFile texture = new TextureFile("", info.filter());
            textureCache.put(texture, new OpenGLTexture(image.getSubimage(clip.getX(), clip.getY(), clip.getWidth(), clip.getHeight()), info.filter()));
            clip.setTexture(texture);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public CanvasImpl getCanvas() {
        return canvas;
    }

    public void setCanvas(CanvasImpl canvas) {
        this.canvas = canvas;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    private WindowProperties getWindowProperties() {
        return windowProperties;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }
}
