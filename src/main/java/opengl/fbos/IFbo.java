package opengl.fbos;

import opengl.textures.parameters.MagFilterParameter;
import opengl.utils.GlBuffer;

public interface IFbo {

    default void blitFbo(IFbo fbo) {
        blitFbo(fbo, MagFilterParameter.NEAREST, GlBuffer.COLOUR, GlBuffer.DEPTH);
    }

    void blitFbo(IFbo fbo, MagFilterParameter filter, GlBuffer... buffers);

    int getWidth();

    int getHeight();

    default void bind() {
        bind(FboTarget.FRAMEBUFFER);
    }

    void bind(FboTarget target);

    default void unbind() {
        unbind(FboTarget.FRAMEBUFFER);
    }

    void unbind(FboTarget target);

    void delete();

}
