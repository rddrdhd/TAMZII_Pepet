package com.example.thegathering;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/*
 * A triangle with 3 vertices.
 */
public class Triangle {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private ByteBuffer indexBuffer;    // Buffer for index-array

    private float[] vertices = {  // Vertices of the triangle
            0.0f,  1.0f, 0.0f, // 0. top
            -1.0f, -1.0f, 0.0f, // 1. left-bottom
            1.0f, -1.0f, 0.0f  // 2. right-bottom
    };
    private byte[] indices = { 0, 1, 2 }; // Indices to above vertices (in CCW)

    // Constructor - Setup the data-array buffers
    public Triangle() {
    // Setup vertex-array buffer. Vertices in float. A float has 4 bytes.
        // Allocate a raw byte buffer. A float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        // Need to use native byte order
        vbb.order(ByteOrder.nativeOrder());
        // Convert the byte buffer into float buffer
        vertexBuffer = vbb.asFloatBuffer();
        // Transfer the data into the buffer
        vertexBuffer.put(vertices);
        // Rewind
        vertexBuffer.position(0);

    // Setup index-array buffer. Indices in byte.
        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    // Render this shape
    public void draw(GL10 gl) {
        // Enable vertex-array to render and define the buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            // gl*Pointer(int size, int type, int stride, Buffer pointer)
            //   size: number of coordinates per vertex (must be 2, 3, or 4).
            //   type: data type of vertex coordinate, GL_BYTE, GL_SHORT, GL_FIXED, or GL_FLOAT
            //   stride: the byte offset between consecutive vertices. 0 for tightly packed.


        // Draw the primitives via index-array
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
            // glDrawElements(int mode, int count, int type, Buffer indices)
            //   mode: GL_POINTS, GL_LINE_STRIP, GL_LINE_LOOP, GL_LINES, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN, or GL_TRIANGLES
            //   count: the number of elements to be rendered.
            //   type: data-type of indices (must be GL_UNSIGNED_BYTE or GL_UNSIGNED_SHORT).
            //   indices: pointer to the index array.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
