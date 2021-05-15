package opengl.shadersOld;

@FunctionalInterface
public interface UniformLoader<T> {

    void load(int location, T value);

}
