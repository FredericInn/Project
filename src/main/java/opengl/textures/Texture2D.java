package opengl.textures;

import opengl.textures.parameters.MagFilterParameter;
import opengl.textures.parameters.MinFilterParameter;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture2D implements ITexture {

    private static final TextureTarget target = TextureTarget.TEXTURE_2D;

    private final Texture texture;
    private final int width;
    private final int height;

    private final TextureConfigs configs;
    private final TextureFunctions functions;

    public Texture2D(Texture texture) {
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.configs = new TextureConfigs();
        this.functions = new TextureFunctions(this, target);
    }

    public Texture2D(int width, int height) {
        this(width, height, new TextureConfigs());
    }

    public Texture2D(int width, int height, TextureConfigs configs) {
        this.texture = Texture.create(target);
        this.width = width;
        this.height = height;
        this.configs = configs.copy();
        this.functions = new TextureFunctions(this, target);

        allocate();
    }

    public static Texture2D of(int width, int height) {
        return new Texture2D(width, height);
    }

    public static Texture2D of(String fileName) throws Exception {
        ITexture cached = TextureCache.getTexture(fileName);
        if (cached instanceof Texture2D) {
            return (Texture2D) cached;
        }
        final TextureConfigs configs = defaultConfigs();
        final TextureInfo info = TextureLoader.load(fileName);
        final Texture2D texture = new Texture2D(info.getWidth(),
                info.getHeight(), configs);
        texture.load(info.getData());

        TextureCache.addToCache(fileName, texture);

        return texture;
    }

    private static TextureConfigs defaultConfigs() {
        final TextureConfigs configs = new TextureConfigs();
        configs.minFilter = MinFilterParameter.NEAREST_MIPMAP_LINEAR;
        configs.magFilter = MagFilterParameter.LINEAR;
        configs.anisotropicFilter = 4f;
        configs.mipmap = true;
        return configs;
    }

    private void allocate() {
        this.texture.allocate(target, 0, configs.internalFormat, width,
                height, 0, configs.format, configs.dataType, (ByteBuffer) null);
        this.functions.apply(configs);
    }

    public void load(ByteBuffer data) {
        if (configs.dataType.getBytes() != 1) {
            throw new IllegalArgumentException("Texture with data type of " +
                    configs.dataType + " " + "cannot load byte buffer");
        }
        this.texture.load(target, 0, 0, 0, width, height,
                configs.format, configs.dataType, data);
        this.functions.apply(configs);
    }

    public void load(IntBuffer data) {
        /*if (configs.dataType.getBytes() != 4) {
            throw new IllegalArgumentException("Texture with data type of " +
                    configs.dataType + " cannot load int buffer");
        }*/
        this.texture.load(target, 0, 0, 0, width, height,
                configs.format, configs.dataType, data);
        this.functions.apply(configs);
    }

    public void load(String textureFile) throws Exception {
        this.load(TextureLoader.load(textureFile).getData());
    }

    public ByteBuffer getPixels() {
        return texture.getPixelsBuffer();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void bind(int unit) {
        this.texture.bind(unit);
    }

    @Override
    public void bind() {
        this.texture.bind();
    }

    @Override
    public void unbind() {
        this.texture.unbind();
    }

    @Override
    public void delete() {
        this.texture.delete();
    }
}
