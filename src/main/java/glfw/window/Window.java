package glfw.window;

import glfw.input.Keyboard;
import glfw.input.Mouse;
import opengl.utils.MemoryUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private static Window current = null;

    private final IntBuffer xPos = MemoryUtils.allocInt(1);
    private final IntBuffer yPos = MemoryUtils.allocInt(1);

    private final String title;
    private long id;
    private int width;
    private int height;

    private Mouse mouse;
    private Keyboard keyboard;

    private boolean resized;
    private boolean vSync;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
        Window.current = this;
    }

    public static Window current() {
        return current;
    }

    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        setHint(WindowHint.VISIBLE, false); // the window will stay hidden after creation
        setHint(WindowHint.RESIZABLE, true); // the window will be resizable
        setHint(WindowHint.CONTEXT_VERSION_MAJOR, 3);
        setHint(WindowHint.CONTEXT_VERSION_MINOR, 2);
        setHint(WindowHint.OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        setHint(WindowHint.OPENGL_FORWARD_COMPAT, true);
        //setHint(WindowHint.MAXIMIZED, true);

        // Create the window
        id = glfwCreateWindow(width, height, title, 0, 0);
        if (id == 0) {
            throw new RuntimeException("Failed to init the GLFW window");
        }

        // Setup resize callback
        glfwSetFramebufferSizeCallback(id, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.resized = true;
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        if (vidMode != null) {
            glfwSetWindowPos(id,
                    (vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2
            );
        }

        // Make the OpenGL context current
        makeContextCurrent();

        if (isvSync()) {
            // Enable v-sync
            glfwSwapInterval(1);
        }

        // Make the window visible
        setVisible(true);

        GL.createCapabilities();

        this.mouse = new Mouse(id);
        this.keyboard = new Keyboard(id);
    }

    private void setHint(WindowHint hint, int value) {
        GLFW.glfwWindowHint(hint.get(), value);
    }

    private void setHint(WindowHint hint, boolean value) {
        GLFW.glfwWindowHint(hint.get(), value ? 1 : 0);
    }

    private void makeContextCurrent() {
        GLFW.glfwMakeContextCurrent(id);
    }

    private void setVisible(boolean visable) {
        if (visable) {
            show();
        } else {
            hide();
        }
    }

    private void show() {
        GLFW.glfwShowWindow(id);
    }

    private void hide() {
        GLFW.glfwHideWindow(id);
    }

    public Mouse getMouse() {
        return mouse;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(id);
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(id);
    }

    public String getTitle() {
        return title;
    }

    public int getX() {
        glfwGetWindowPos(id, xPos, yPos);
        int x = xPos.get();
        xPos.flip();
        return x;
    }

    public int getY() {
        glfwGetWindowPos(id, xPos, yPos);
        int y = yPos.get();
        yPos.flip();
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isvSync() {
        return vSync;
    }

    public boolean isResized() {
        return resized;
    }

    public void setWindowShouldClose(boolean shouldClose) {
        glfwSetWindowShouldClose(id, shouldClose);
    }

    public void update(boolean swapBuffers) {
        if (swapBuffers) {
            swapBuffers();
        }
        resized = false;
        Window.current = this;
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(id);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    public void waitEvents() {
        GLFW.glfwWaitEvents();
    }
}
