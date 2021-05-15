package opengl.utils;

import opengl.constants.DataType;
import opengl.constants.RenderMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;

public final class GlRendering {

    private GlRendering() {

    }

    public static void drawArrays(RenderMode mode, int first, int count) {
        GL11.glDrawArrays(mode.get(), first, count);
    }

    public static void drawElements(RenderMode mode, int count, DataType type, long indices) {
        GL11.glDrawElements(mode.get(), count, type.get(), indices);
    }

    public static void drawArraysInstanced(RenderMode mode, int first, int count, int instances) {
        GL31.glDrawArraysInstanced(mode.get(), first, count, instances);
    }

    public static void drawElementsInstanced(RenderMode mode, int count, DataType type, long indices, int instances) {
        GL31.glDrawElementsInstanced(mode.get(), count, type.get(), indices, instances);
    }

}
