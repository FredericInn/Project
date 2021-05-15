package opengl.utils;

import maths.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

public final class GlUtils {

    private static boolean[] clipPlanes = new boolean[6];
    private static boolean provokingVertexFirst = false;
    private static boolean polygonLines = false;
    private static boolean cullingFaces = false;
    private static boolean depthMasking = false;
    private static int cullingFace = 0;

    private static BlendFunction blendFunction = BlendFunction.NONE;
    private static DepthFunction depthFunction = DepthFunction.NONE;

    private GlUtils() {

    }

    public static int valueOf(boolean b) {
        return b ? GL11.GL_TRUE : GL11.GL_FALSE;
    }

    public static boolean isPolygonLines() {
        return polygonLines;
    }

    public static void setProvokingVertexFirst() {
        if (!provokingVertexFirst) {
            GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);
            provokingVertexFirst = true;
        }
    }

    public static void setProvokingVertexLast() {
        if (!provokingVertexFirst) {
            GL32.glProvokingVertex(GL32.GL_LAST_VERTEX_CONVENTION);
            provokingVertexFirst = false;
        }
    }

    public static void enableCulling() {
        cull(GL11.GL_BACK);
    }

    public static void enableFrontCulling() {
        cull(GL11.GL_FRONT);
    }

    public static void disableCulling() {
        if (cullingFaces) {
            GL11.glDisable(GL11.GL_CULL_FACE);
            cullingFaces = false;
            cullingFace = 0;
        }
    }

    private static void cull(int face) {
        if (!cullingFaces) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glCullFace(face);
            cullingFaces = true;
            cullingFace = face;
        } else if (cullingFace != face) {
            GL11.glCullFace(face);
        }
    }

    public static void enableAlphaBlending() {
        setBlendFunction(BlendFunction.ALPHA);
    }

    public static void enableAdditiveBlending() {
        setBlendFunction(BlendFunction.ADDITIVE);
    }

    public static void disableBlending() {
        setBlendFunction(BlendFunction.NONE);
    }

    public static void setBlendFunction(BlendFunction blendFunction) {
        if (GlUtils.blendFunction != blendFunction) {
            GlUtils.blendFunction = blendFunction;
            if (blendFunction == BlendFunction.NONE) {
                GlUtils.setEnabled(GL11.GL_BLEND, false);
            } else {
                GlUtils.setEnabled(GL11.GL_BLEND, true);
                GL11.glBlendFunc(blendFunction.getSource(),
                        blendFunction.getDestination());
            }
        }
    }

    public static void enableDepthTest() {
        setDepthFunction(DepthFunction.LESS);
    }

    public static void disableDepthTest() {
        setDepthFunction(DepthFunction.NONE);
    }

    public static void setDepthFunction(DepthFunction depthFunction) {
        if (GlUtils.depthFunction != depthFunction) {
            GlUtils.depthFunction = depthFunction;
            if (depthFunction == DepthFunction.NONE) {
                GlUtils.setEnabled(GL11.GL_DEPTH_TEST, false);
            } else {
                GlUtils.setEnabled(GL11.GL_DEPTH_TEST, true);
                GL11.glDepthFunc(depthFunction.get());
            }
        }
    }

    public static void enableDepthMasking() {
        if (!depthMasking) {
            GL11.glDepthMask(true);
            depthMasking = true;
        }
    }

    public static void disableDepthMasking() {
        if (depthMasking) {
            GL11.glDepthMask(false);
            depthMasking = false;
        }
    }

    private static void setEnabled(int state, boolean enabled) {
        if (enabled) {
            GL11.glEnable(state);
        } else {
            GL11.glDisable(state);
        }
    }

    public static void setClearColour(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public static void setClearColour(Vector3f colour) {
        GL11.glClearColor(colour.x, colour.y, colour.z, 1);
    }

    public static void setViewport(int x, int y, int w, int h) {
        GL11.glViewport(x, y, w, h);
    }

    public static void enableClipPlane(int clipPlane) {
        if (!clipPlanes[clipPlane]) {
            GL11.glEnable(GL11.GL_CLIP_PLANE0 + clipPlane);
            clipPlanes[clipPlane] = true;
        }
    }

    public static void disableClipPlane(int clipPlane) {
        if (clipPlanes[clipPlane]) {
            GL11.glEnable(GL11.GL_CLIP_PLANE0 + clipPlane);
            clipPlanes[clipPlane] = false;
        }
    }

    public static void drawPolygonLine() {
        if (!polygonLines) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            polygonLines = true;
        }
    }

    public static void drawPolygonFill() {
        if (polygonLines) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            polygonLines = false;
        }
    }

    public static void clearColourAndDepthBuffer() {
        clear(GlBuffer.COLOUR, GlBuffer.DEPTH);
    }

    public static void clear(GlBuffer... glBuffers) {
        int mask = 0;
        for (GlBuffer glBuffer : glBuffers) {
            mask |= glBuffer.get();
        }
        GL11.glClear(mask);
    }

}
