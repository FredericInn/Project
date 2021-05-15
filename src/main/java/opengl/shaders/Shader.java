package opengl.shaders;

import engine.fileLoaders.TextFileLoader;
import org.lwjgl.opengl.GL20;

public class Shader {

    public final int id;
    private final String code;
    private final ShaderType type;

    private Shader(int id, String code, ShaderType type) {
        this.id = id;
        this.code = code;
        this.type = type;
    }

    public static Shader of(String fileName, ShaderType type) throws Exception {
        final int id = GL20.glCreateShader(type.get());
        final String code = TextFileLoader.loadResource(fileName);
        return new Shader(id, code, type);
    }

    public static Shader createVertex(String fileName) throws Exception {
        return Shader.of(fileName, ShaderType.VERTEX);
    }

    public static Shader createFragment(String fileName) throws Exception {
        return Shader.of(fileName, ShaderType.FRAGMENT);
    }

    public static Shader createGeometry(String fileName) throws Exception {
        return Shader.of(fileName, ShaderType.GEOMETRY);
    }

    public static Shader createTessControl(String fileName) throws Exception {
        return Shader.of(fileName, ShaderType.TESS_CONTROL);
    }

    public static Shader createTessEvaluation(String fileName) throws Exception {
        return Shader.of(fileName, ShaderType.TESS_EVALUATION);
    }

    public void init() throws ShaderCompileException {
        GL20.glShaderSource(id, code);
        GL20.glCompileShader(id);
        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == 0) {
            final String infoLog = GL20.glGetShaderInfoLog(id, 1024);
            throw new ShaderCompileException("Shader type: " + type + ", Error compiling Shader code:\n" + infoLog);
        }
    }

    public void attach(int programId) {
        GL20.glAttachShader(programId, id);
    }

    public void detach(int programId) {
        GL20.glDetachShader(programId, id);
    }

    public void delete() {
        GL20.glDeleteShader(id);
    }
}