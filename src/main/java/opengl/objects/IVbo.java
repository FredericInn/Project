package opengl.objects;

public interface IVbo {

    long getSize();

    void bind();

    void bindToVao(Vao vao);

    void unbind();

    void delete();

}
