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
public class Land extends SceneObject{
    private final int  MAX_WIDTH=800;
    private final int  MAX_HEIGHT=500;
    
    private int wWidth;
    private int wHeight;
    
    
    public Land(int wWidth, int wHeight,String textureName){
        this.wWidth=wWidth;
        this.wHeight=wHeight;
        this.textureName = textureName;
        this.tex=this.loadTexture();
    }
    
    public void setModel(){
        tex.bind(0);
        float landWidth;
        float landHeight;
        
        landWidth=tex.width;
        int heightRatio=tex.height/wHeight;
        
        if(heightRatio>1){
            landHeight=MAX_HEIGHT;
            landWidth=tex.width*(MAX_HEIGHT/tex.height);
        }else{
            landHeight=tex.height;
        }
        
        float widthRatio=tex.width/landWidth;
        
        if(widthRatio>1){
            landWidth=MAX_WIDTH;
            landHeight=tex.height*(MAX_WIDTH/tex.width);
        }else{
            landWidth=tex.width;
        }       
        
        float ratio= tex.width/tex.height;
        
        if(ratio>=1){
            landWidth=10.5f;
            landHeight=10.5f/ratio;
        }
        
        landWidth=10;
        landHeight=10;
        float posZ=10f;
        
        float[] vertices = new float[]{
            landWidth,-landHeight,posZ,    //BOTTOM RIGHT
            landWidth,landHeight,posZ,  //TOP RIGHT
            -landWidth,landHeight,posZ,   //TOP LEFT
            -landWidth,-landHeight,posZ,    //BOTTOM LEFT
        };
                
        float[] texture = new float[]{
            1,1,
            1,0,
            0,0,
            0,1,
        };            
            
        int[] indices = new int[]{
            0,1,2,
            0,2,3
        };
             
        this.model = new Model(vertices, texture, indices);
    }   
}