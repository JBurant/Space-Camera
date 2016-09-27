
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

/**
 *
 * @author Jirka3
 */

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL13.*;

public class Shader {
    private int program;
    private int vs;
    private int fs;

public Shader (){
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
    
    if(glGetShaderi(vs,GL_COMPILE_STATUS)!=1){
        System.out.println("error vert");
    }
    
    fs=glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fs,fragmentShaderSource);
    glCompileShader(fs);
    
    if(glGetShaderi(fs,GL_COMPILE_STATUS)!=1){
        System.out.println("error frag");
    }
    
    glAttachShader(program, vs);
    glAttachShader(program, fs);
    
    glBindAttribLocation(program,0,"vertices");
    glBindAttribLocation(program,1,"textures");
    
    glLinkProgram(program);
    glValidateProgram(program);
}

public void setUniform(String name, Matrix4f value){
    int location = glGetUniformLocation(program,name);
    FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
    value.get(buffer);
    if(location!=-1){
        glUniformMatrix4fv(location, false, buffer);
    }
}

public void setUniform(String name, int value){
    int location = glGetUniformLocation(program,name);

    if(location!=-1){
        glUniform1i(location,value);
    }
}

    public void bind(){
        glUseProgram(program);
    }
}