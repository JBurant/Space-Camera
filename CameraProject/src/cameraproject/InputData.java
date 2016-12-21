/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

/**
 * Stores data from the GUI input.
 * @author Jiri Burant
 */
public class InputData {
    public FloatWrapper initPosX;
    public FloatWrapper initPosY;
    public FloatWrapper initPosZ;
    public FloatWrapper endPosX;
    public FloatWrapper endPosY;
    public FloatWrapper endPosZ;
    public FloatWrapper noImages;
    public FloatWrapper alpha;
    public FloatWrapper omega;
    public FloatWrapper fov;
    
    public InputData(){
     this.initPosX=new FloatWrapper();
     this.initPosY=new FloatWrapper();
     this.initPosZ=new FloatWrapper(-9);
     this.endPosX=new FloatWrapper();
     this.endPosY=new FloatWrapper();
     this.endPosZ=new FloatWrapper(-9);
     this.noImages=new FloatWrapper();
     this.alpha=new FloatWrapper();
     this.omega=new FloatWrapper();     
     this.fov=new FloatWrapper();
    }
}
