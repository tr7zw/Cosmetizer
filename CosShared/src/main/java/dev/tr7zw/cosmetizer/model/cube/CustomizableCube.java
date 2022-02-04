package dev.tr7zw.cosmetizer.model.cube;

import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;

import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.core.Direction;

public class CustomizableCube extends Cube{

    private final Polygon[] polygons;
    private int polygonCount = 0;
    public final float minX;
    public final float minY;
    public final float minZ;
    public final float maxX;
    public final float maxY;
    public final float maxZ;
    
    public CustomizableCube(Map<String, float[]> uvData, float minX, float minY, float minZ, float sizeX, float sizeY, float sizeZ, float n, float o, float p,
            boolean bl, float q, float r) {
        super(0, 0, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, false, 0f, 0f); // unused
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = minX + sizeX;
        this.maxY = minY + sizeY;
        this.maxZ = minZ + sizeZ;
        this.polygons = new Polygon[6];
        float s = minX + sizeX;
        float t = minY + sizeY;
        float u = minZ + sizeZ;
        minX -= n;
        minY -= o;
        minZ -= p;
        s += n;
        t += o;
        u += p;
        if (bl) {
            float v = s;
            s = minX;
            minX = v;
        }
        Vertex vertex = new Vertex(minX, minY, minZ, 0.0F, 0.0F);
        Vertex vertex2 = new Vertex(s, minY, minZ, 0.0F, 8.0F);
        Vertex vertex3 = new Vertex(s, t, minZ, 8.0F, 8.0F);
        Vertex vertex4 = new Vertex(minX, t, minZ, 8.0F, 0.0F);
        Vertex vertex5 = new Vertex(minX, minY, u, 0.0F, 0.0F);
        Vertex vertex6 = new Vertex(s, minY, u, 0.0F, 8.0F);
        Vertex vertex7 = new Vertex(s, t, u, 8.0F, 8.0F);
        Vertex vertex8 = new Vertex(minX, t, u, 8.0F, 0.0F);
        float[] uv = uvData.get("down");
        if(uv != null)
            this.polygons[polygonCount++] = new Polygon(new Vertex[]{vertex6, vertex5, vertex, vertex2}, uv[0], uv[1],
                    uv[2], uv[3], q, r, bl, Direction.DOWN);
        uv = uvData.get("up");
        if(uv != null)
            this.polygons[polygonCount++] = new Polygon(new Vertex[]{vertex3, vertex4, vertex8, vertex7}, uv[0], uv[1],
                    uv[2], uv[3], q, r, bl, Direction.UP);
        uv = uvData.get("west");
        if(uv != null)
            this.polygons[polygonCount++] = new Polygon(new Vertex[]{vertex, vertex5, vertex8, vertex4}, uv[2], uv[3],
                    uv[0], uv[1], q, r, bl, Direction.WEST);
        uv = uvData.get("north");
        if(uv != null)
            this.polygons[polygonCount++] = new Polygon(new Vertex[]{vertex2, vertex, vertex4, vertex3}, uv[2], uv[3],
                    uv[0], uv[1], q, r, bl, Direction.NORTH);
        uv = uvData.get("east");
        if(uv != null)
            this.polygons[polygonCount++] = new Polygon(new Vertex[]{vertex6, vertex2, vertex3, vertex7}, uv[2], uv[3],
                    uv[0], uv[1], q, r, bl, Direction.EAST);
        uv = uvData.get("south");
        if(uv != null)
            this.polygons[polygonCount++] = new Polygon(new Vertex[]{vertex5, vertex6, vertex7, vertex8}, uv[2], uv[3],
                    uv[0], uv[1], q, r, bl, Direction.SOUTH);
    }
    
    public void compile(PoseStack.Pose pose, VertexConsumer vertexConsumer, int i, int j, float f, float g, float h,
            float k, boolean mirror) {
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        for (int x = 0; x < polygonCount; x++) {
            Polygon polygon = this.polygons[x];
            Vector3f vector3f = polygon.normal.copy();
            vector3f.transform(matrix3f);
            float l = vector3f.x();
            float m = vector3f.y();
            float n = vector3f.z();
            if(mirror) {
                Vertex vertex;
                {
                    vertex = polygon.vertices[2];
                    float o = vertex.pos.x() / 16.0F;
                    float p = vertex.pos.y() / 16.0F;
                    float q = vertex.pos.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(o, p, q, 1.0F);
                    vector4f.transform(matrix4f);
                    vertexConsumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), f, g, h, k, vertex.u, vertex.v, j,
                            i, l, m, n);
                }
                {
                    vertex = polygon.vertices[1];
                    float o = vertex.pos.x() / 16.0F;
                    float p = vertex.pos.y() / 16.0F;
                    float q = vertex.pos.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(o, p, q, 1.0F);
                    vector4f.transform(matrix4f);
                    vertexConsumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), f, g, h, k, vertex.u, vertex.v, j,
                            i, l, m, n);
                }
                {
                    vertex = polygon.vertices[0];
                    float o = vertex.pos.x() / 16.0F;
                    float p = vertex.pos.y() / 16.0F;
                    float q = vertex.pos.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(o, p, q, 1.0F);
                    vector4f.transform(matrix4f);
                    vertexConsumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), f, g, h, k, vertex.u, vertex.v, j,
                            i, l, m, n);
                }
                {
                    vertex = polygon.vertices[3];
                    float o = vertex.pos.x() / 16.0F;
                    float p = vertex.pos.y() / 16.0F;
                    float q = vertex.pos.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(o, p, q, 1.0F);
                    vector4f.transform(matrix4f);
                    vertexConsumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), f, g, h, k, vertex.u, vertex.v, j,
                            i, l, m, n);
                }
            }else {
                for (Vertex vertex : polygon.vertices) {
                    float o = vertex.pos.x() / 16.0F;
                    float p = vertex.pos.y() / 16.0F;
                    float q = vertex.pos.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(o, p, q, 1.0F);
                    vector4f.transform(matrix4f);
                    vertexConsumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), f, g, h, k, vertex.u, vertex.v, j,
                            i, l, m, n);
                }
            }
        }
    }
    
    private static class Polygon {
        public final Vertex[] vertices;

        public final Vector3f normal;

        public Polygon(Vertex[] vertexs, float f, float g, float h, float i, float j, float k, boolean bl,
                Direction direction) {
            this.vertices = vertexs;
            float l = 0.0F / j;
            float m = 0.0F / k;
            vertexs[0] = vertexs[0].remap(h / j - l, g / k + m);
            vertexs[1] = vertexs[1].remap(f / j + l, g / k + m);
            vertexs[2] = vertexs[2].remap(f / j + l, i / k - m);
            vertexs[3] = vertexs[3].remap(h / j - l, i / k - m);
            if (bl) {
                int n = vertexs.length;
                for (int o = 0; o < n / 2; o++) {
                    Vertex vertex = vertexs[o];
                    vertexs[o] = vertexs[n - 1 - o];
                    vertexs[n - 1 - o] = vertex;
                }
            }
            this.normal = direction.step();
            if (bl)
                this.normal.mul(-1.0F, 1.0F, 1.0F);
        }
    }

    private static class Vertex {
        public final Vector3f pos;

        public final float u;

        public final float v;

        public Vertex(float f, float g, float h, float i, float j) {
            this(new Vector3f(f, g, h), i, j);
        }

        public Vertex remap(float f, float g) {
            return new Vertex(this.pos, f, g);
        }

        public Vertex(Vector3f vector3f, float f, float g) {
            this.pos = vector3f;
            this.u = f;
            this.v = g;
        }
    }

}
