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
public class Target extends SceneObject{
    public Target(){
        this.textureName = "img/target.png";
        this.tex=this.loadTexture();
    }
    
    public void setModel(float posX, float posY, float posZ){
        this.tex.bind(0);
        
        float size = 0.2f;        

        float[] vertices = new float[]{
            posX + size, posY + size, posZ,  //TOP RIGHT
            posX-size, posY + size, posZ,    //TOP LEFT
            posX-size, posY-size, posZ,     //BOTTOM LEFT
            posX + size, posY-size, posZ,   //BOTTOM RIGHT   
        };
        
        float[] texture = new float[]{
            1,0,
            0,0,
            0,1,
            1,1,
        };            
        
        int [] indices = new int[]{
            0,1,2,
            0,3,2
        };
                
        this.model = new Model(vertices, texture, indices);
    }
}
