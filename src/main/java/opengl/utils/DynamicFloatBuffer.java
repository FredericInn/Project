package opengl.utils;

import java.nio.FloatBuffer;

public class DynamicFloatBuffer {

    private FloatBuffer buffer;

    public DynamicFloatBuffer(int initialCapacity) {
        this.buffer = MemoryUtils.allocFloat(initialCapacity);
    }

    public FloatBuffer ensureCapacity(int needed) {
        if (needed >= buffer.capacity()) {
            final FloatBuffer temp = buffer;
            final int newCapacity = (int) (buffer.capacity() * 1.5);
            this.buffer = MemoryUtils.allocFloat(Math.max(newCapacity, needed));
            return temp;
        }
        return null;
    }

    public FloatBuffer get() {
        return buffer;
    }
}
