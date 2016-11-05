/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

/**
 * Helper class for passing the float value by reference.
 * @author Jirka3
 */
public class FloatWrapper {
    private float value;
    
    public FloatWrapper(){
        this.value=0;
    }
    
    public FloatWrapper(float value){
        this.value=value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
