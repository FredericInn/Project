package opengl.objects;

import opengl.utils.GlConfigs;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class Vao {

    private static final Vao NULL = new Vao(0);
    private static final Vao FIRST = Vao.create();

    private static int boundVao = 0;

    private final int id;

    private IVbo indexBuffer;
    private final List<IVbo> dataBuffers;
    private final List<Attribute> attributes;

    private boolean deleted = false;

    private Vao(int id) {
        this.id = id;
        this.dataBuffers = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.bind();
    }

    public static Vao create() {
        int id = GL30.glGenVertexArrays();
        return new Vao(id);
    }

    public static void bindIfNone() {
        if (boundVao == 0) {
            FIRST.bind();
        }
    }

    public boolean isBound() {
        return boundVao == id;
    }

    public void enableAttributes() {
        attributes.forEach(Attribute::enable);
    }

    public void disableAttributes() {
        attributes.forEach(Attribute::disable);
    }

    public boolean hasIndices() {
        return indexBuffer != null;
    }

    public void loadDataBuffer(IVbo vbo, Attribute... attributes) {
        vbo.bindToVao(this);
        linkAttributes(attributes);
        dataBuffers.add(vbo);
    }

    public void loadIndexBuffer(IVbo indexBuffer) {
        loadIndexBuffer(indexBuffer, true);
    }

    public void loadIndexBuffer(IVbo indexBuffer, boolean deleteOld) {
        if (this.indexBuffer != null && deleteOld) {
            this.indexBuffer.delete();
        }
        this.indexBuffer = indexBuffer;
        this.indexBuffer.bindToVao(this);
    }

    public int getIndexCount() {
        return hasIndices() ? (int) indexBuffer.getSize() : 0;
    }

    public void bind() {
        if (GlConfigs.CACHE_STATE || !isBound()) {
            GL30.glBindVertexArray(id);
            boundVao = id;
        }
    }

    public void unbind() {
        if (GlConfigs.CACHE_STATE || isBound()) {
            GL30.glBindVertexArray(0);
            boundVao = 0;
        }
    }

    public void delete(boolean deleteVbos) {
        if (deleteVbos) {
            if (indexBuffer != null) {
                indexBuffer.delete();
            }
            dataBuffers.forEach(IVbo::delete);
        }
        if (GlConfigs.CACHE_STATE || !deleted) {
            GL30.glDeleteVertexArrays(id);
            deleted = true;
        }
    }

    private void linkAttributes(Attribute... attributes) {
        int offset = 0;
        int stride = getBytesPerVertex(attributes);
        for (Attribute attribute : attributes) {
            attribute.link(stride, offset);
            offset += attribute.getBytesPerVertex();
            this.attributes.add(attribute);
        }
    }

    private int getBytesPerVertex(Attribute... attributes) {
        int sizeInBytes = 0;
        for (Attribute attribute : attributes) {
            sizeInBytes += attribute.getBytesPerVertex();
        }
        return sizeInBytes;
    }
}