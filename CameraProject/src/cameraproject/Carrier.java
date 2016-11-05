/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

/**
 *
 * @author jiri
 */
public class Carrier extends SceneObject{
    float size;
    
    float coorX;
    float coorY;
    float coorZ;

    float cameraX;
    float cameraY;
    float cameraAlpha;
    float cameraBeta;

    public Carrier(float coorX, float coorY, float coorZ) {
        this.coorX = coorX;
        this.coorY = coorY;
        this.coorZ = coorZ;
        this.cameraX = 0;
        this.cameraY = 0;
        this.cameraAlpha = 0;
        this.cameraAlpha = 0;
        
        this.textureName="img/carrier.png";
        this.loadTexture();
    }

    public Carrier(float coorX, float coorY, float coorZ, float cameraX, float cameraY, float cameraAlpha, float cameraBeta) {
        this.coorX = coorX;
        this.coorY = coorY;
        this.coorZ = coorZ;
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.cameraAlpha = cameraAlpha;
        this.cameraAlpha = cameraBeta;
        
        this.size=0.5f;
    }
    
    public void setModel(float posX, float posY, float posZ){
        this.tex.bind(0);
        
        float size = 0.5f;
        
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