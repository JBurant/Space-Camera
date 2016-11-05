/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * OpenGL texture of a Scene object.
 * @author Jiri Burant
 */
public class Texture {
    private int id;
    public int width;
    public int height;

    /**
     * Build new OpenGL texture from given filename.
     * @param filename Texture's source file.
     */
public Texture(String filename){
    BufferedImage bi;
    try{
        bi=ImageIO.read(new File(filename));
        width=bi.getWidth();
        height=bi.getHeight();
        
        int[] pixels_raw = new int[width*height*4];
            pixels_raw=bi.getRGB(0, 0,width,height,null,0,width);
            ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*4);
            
            for(int i =0; i<width; i++){
                for(int j=0;j<height; j++){
                    int pixel = pixels_raw[i*height+j];
                    pixels.put((byte) ((pixel>>16) & 0xFF));
                    pixels.put((byte) ((pixel>>8) & 0xFF));
                    pixels.put((byte) ((pixel) & 0xFF));
                    pixels.put((byte) ((pixel>>24) & 0xFF));
                }
                }
            pixels.flip();
            

            id=glGenTextures();
            glBindTexture(GL_TEXTURE_2D,id);
            glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
            
            glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,width,height,0,GL_RGBA,GL_UNSIGNED_BYTE,pixels);

            
    }catch(IOException e){
    }
}
    
/**
 * Bind the texture to openGL.
 * @param sampler Sampler of the texture, 0 is used.
 */
    public void bind(int sampler){
        glEnable(GL_TEXTURE_2D);
        
        if(sampler >=0 && sampler <=32){
        glActiveTexture(GL_TEXTURE0 + sampler);
        glBindTexture(GL_TEXTURE_2D,id);
        }
        
        glDisable(GL_TEXTURE_2D);
    }
}