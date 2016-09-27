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
public class Component {
    public int xCoor;
    public int yCoor;
    public Model model;
    
    public int height;
    public int width;
    
    public Color color;
    
    public Component(int xCoor, int yCoor, int width, int height){
        this.color=new Color(1,1,0);  
        this.init(xCoor,yCoor,width,height);  
    }
    
    public Component(int xCoor, int yCoor, int width, int height, Color color){
        this.color=color;
        this.init(xCoor,yCoor,width,height); 
    }
        
    public Component(int xCoor, int yCoor, int width, int height, Color color, Component parent) {
        this.color=color;
        this.init(xCoor+parent.xCoor,yCoor+parent.yCoor,width,height); 
    }
    
    public Component(int xCoor, int yCoor, int width, int height, Component parent) {
        this.color=new Color(1,1,0); 
        this.init(xCoor+parent.xCoor,yCoor+parent.yCoor,width,height);     
    }
    
    public Model getModel(){
        return model;
    }
    
    private float[] createVertices(int xCoor, int yCoor, int width, int height){
        float[] vertices = new float[]{
            xCoor+width,yCoor,0,    //BOTTOM RIGHT
            xCoor+width,yCoor+height,0,  //TOP RIGHT
            xCoor,yCoor+height,0,   //TOP LEFT
            
            xCoor+width,yCoor,0,    //BOTTOM RIGHT
            xCoor,yCoor+height,0,   //TOP LEFT
            xCoor,yCoor,0,    //BOTTOM LEFT*/
        };
        
        return vertices;
    }
    
    private float[] createColors(Color color){
        float[] colors = new float[]{
            color.r,color.g,color.b,    //BOTTOM RIGHT
            color.r,color.g,color.b,  //TOP RIGHT
            color.r,color.g,color.b,   //TOP LEFT
            
            color.r,color.g,color.b,   //BOTTOM RIGHT
            color.r,color.g,color.b,   //TOP LEFT
            color.r,color.g,color.b,    //BOTTOM LEFT*/
        };
        
        return colors;
    }

    private void init(int xCoor, int yCoor, int height, int width) {
        this.xCoor=xCoor;
        this.yCoor=yCoor;
        
        this.height=height;
        this.width=width;
        
        //this.model=new Model(createVertices(xCoor,yCoor,height,width), createColors(this.color), false);
    }
}
