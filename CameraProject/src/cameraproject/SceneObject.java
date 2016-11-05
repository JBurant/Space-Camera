/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * Parent class for the rendered objects.
 * @author Jiri Burant
 */
public class SceneObject {
    public Model model;
    public Texture tex;
    protected String textureName;
    
    /**
     * Loads the texture.
     * @return Loaded texture.
     */
    public final Texture loadTexture(){    
        glEnable(GL_TEXTURE_2D);
        tex = new Texture(this.textureName);
        glDisable(GL_TEXTURE_2D);
        return tex;        
    }
    
    public String getTextureName() {
        return textureName;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }
}