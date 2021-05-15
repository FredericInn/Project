package opengl.shadersOld;

@FunctionalInterface
public interface ValueSetter<T> {

    T set(T a, T b);

}
