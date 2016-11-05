
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import java.nio.FloatBuffer;
import utils.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.*;

/**
 * Creates new frag and vert shader program.
 * @author Jiri Burant
 */

public class Shader {
    private final int program;
    private final int vs;
    private final int fs;
    private final FloatBuffer buffer;

    /**
     * Creates and links the shader program from specified string description of the shaders.
     */
public Shader (){
    buffer = BufferUtils.createFloatBuffer(16);
    
    String vertexShaderSource = "#version 120\n"
                + "\n"
                + "attribute vec3 vertices;\n"
                + "\n"
                + "attribute vec2 textures;\n"
                + "\n"
                + "varying vec2 tex_coords;\n"
                + "\n"
                + "uniform mat4 projection;"
                + "\n"
                + "void main() {\n"
                + "\n"
                +   "tex_coords=textures;"
                + "\n"
                + " gl_Position = projection * vec4(vertices, 1);\n"
                + "}"
                + "\n"
                + "\n"
                + "\n";

    String fragmentShaderSource = "#version 120\n"
                + "\n"
                + "uniform sampler2D sampler;"
                + "\n"
                + "varying vec2 tex_coords;"
                + "\n"
                + "void main() {\n"
                + "gl_FragColor = texture2D(sampler,tex_coords);\n"
                + "}";
    
    program =glCreateProgram();
    
    vs=glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vs,vertexShaderSource);
    glCompileShader(vs);
    
    if(glGetShader(vs,GL_COMPILE_STATUS)!=1){
        System.out.println("error vert");
    }
    
    fs=glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fs,fragmentShaderSource);
    glCompileShader(fs);
    
    if(glGetShader(fs,GL_COMPILE_STATUS)!=1){
        System.out.println("error frag");
    }
    
    glAttachShader(program, vs);
    glAttachShader(program, fs);
    
    glBindAttribLocation(program,0,"vertices");
    glBindAttribLocation(program,1,"textures");
    
    glLinkProgram(program);
    glValidateProgram(program);
}

/**
 * Set uniform Matrix4f variable of the shader program.
 * @param name Name of the variable.
 * @param value Value to be used.
 */
public void setUniform(String name, Matrix4f value){
    int location = glGetUniformLocation(program,name);
    value.get(buffer);
    if(location!=-1){
        GL20.glUniformMatrix4(location, false, buffer);
    }
}

/**
 * Set uniform int variable of the shader program.
 * @param name Name of the variable.
 * @param value Value to be used.
 */
public void setUniform(String name, int value){
    int location = glGetUniformLocation(program,name);

    if(location!=-1){
        glUniform1i(location,value);
    }
}

/**
 * Binds the shader program.
 */
public void bind(){
        glUseProgram(program);
    }
}