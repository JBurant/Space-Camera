/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import org.joml.Vector3f;

/**
 *
 * @author Jirka3
 */
public class InputData {
    public FloatWrapper initPosX;
    public FloatWrapper initPosY;
    public FloatWrapper endPosX;
    public FloatWrapper endPosY;
    public FloatWrapper noImages;
    public FloatWrapper alpha;
    public FloatWrapper omega;
    
    public InputData(){
     this.initPosX=new FloatWrapper();
     this.initPosY=new FloatWrapper();
     this.endPosX=new FloatWrapper();
     this.endPosY=new FloatWrapper();
     this.noImages=new FloatWrapper();
     this.alpha=new FloatWrapper();
     this.omega=new FloatWrapper();     
    }
}