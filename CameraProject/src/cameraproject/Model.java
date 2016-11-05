package cameraproject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
/**
 * OpenGL model of a SceneObject.
 * @author Jiri Burant
 */
public class Model {
    private final int draw_count;
    private final int v_id;
    private int c_id;
    private int t_id;
    private int i_id;
    
    /**
     * Build new textured Model.
     * @param vertices
     * @param tex_coords
     * @param indices 
     */
    public Model(float[] vertices, float[]tex_coords, int[] indices){
        draw_count = indices.length;
        
        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);
        
        t_id=glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(tex_coords), GL_STATIC_DRAW);
        
        i_id=glGenBuffers();
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    /**
     * Build new non-textured model.
     * @param vertices 
     */
    public Model(float[] vertices){
        draw_count = vertices.length/3;

        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    /**
     * Render non-textured model.
     */
    public void renderNoTex(){
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glVertexPointer(3, GL_FLOAT, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, c_id);

        glDrawArrays(GL_TRIANGLES, 0, draw_count);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
         }
    
    /**
     * Render textured model.
     */
    public void render(){
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glVertexAttribPointer(0,3, GL_FLOAT, false, 0, 0);
        
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glVertexAttribPointer(1,2, GL_FLOAT, false, 0, 0);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,i_id);
        glDrawElements(GL_TRIANGLES,draw_count,GL_UNSIGNED_INT,0);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }
    
    /**
     * Creater new FloatBuffer with specified data.
     * @param data Data to include to buffer.
     * @return Buffer of the data.
     */
    private FloatBuffer createBuffer (float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
