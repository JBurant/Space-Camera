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
    public Target(){}
    
    public void setModel(float posX, float posY){
        Texture tex= this.loadTexture("img/target.png");
        
        float size = 0.2f;
        
        float posZ=9f;

        float[] vertices = new float[]{
            posX + size, posY + size, posZ,  //TOP RIGHT
            posX-size, posY + size, posZ,    //TOP LEFT
            posX-size, posY-size, posZ,     //BOTTOM LEFT
            posX + size, posY-size, posZ,   //BOTTOM RIGHT
          /*  
            posX + size, posY, posZ,
            posX + size, posY + size, posZ,
            posX + size/2, posY + size/2,posZ-size/2,
            
            posX + size, posY + size, posZ,
            posX , posY+size, posZ,
            posX + size/2, posY + size/2,posZ-size/2,
            
            posX, posY + size, posZ,
            posX, posY, posZ,
            posX + size/2, posY + size/2,posZ-size/2,
            
            posX + size, posY , posZ,
            posX, posY, posZ,
            posX + size/2, posY + size/2,posZ-size/2,
        */   
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
