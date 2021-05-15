package opengl.textures;

import engine.fileLoaders.PNGDecoder;
import opengl.utils.MemoryUtils;

import java.nio.ByteBuffer;

public final class TextureLoader {

    private TextureLoader() {

    }

    public static TextureInfo load(String file) throws Exception {
        String fileFormat = file.substring(file.lastIndexOf(".") + 1).toLowerCase();
        switch (fileFormat) {
            case "png":
                return decodePng(file);
            default:
                throw new Exception("File format " + fileFormat + " is not supported");
        }
    }

    private static TextureInfo decodePng(String textureFile) throws Exception {
        PNGDecoder decoder = new PNGDecoder(TextureLoader.class.getResourceAsStream(textureFile));

        ByteBuffer textureBuffer = MemoryUtils.allocByte(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(textureBuffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        textureBuffer.flip();

        return new TextureInfo(decoder.getWidth(), decoder.getHeight(), textureBuffer);
    }
}
