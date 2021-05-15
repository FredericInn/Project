package opengl.textures;

public interface ITexture {

    void bind(int unit);

    void bind();

    void unbind();

    void delete();

}
