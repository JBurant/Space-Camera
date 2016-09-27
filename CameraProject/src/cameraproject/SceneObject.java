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
 *
 * @author Jirka3
 */
public class SceneObject {
    public Model model;
    
        public Texture loadTexture(String name){
        glEnable(GL_TEXTURE_2D);
        Texture tex = new Texture("img/"+name);
        tex.bind(0);
        glDisable(GL_TEXTURE_2D);
        
        return tex;        
    }
}