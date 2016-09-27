/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import java.util.ArrayList;

/**
 *
 * @author Jirka3
 */
public class GUILoader {
    public ArrayList<Component> componentList;
    
    public GUILoader(){
        this.componentList=new ArrayList();
    }
    
    public void render (){
        for (int i = 0; i < componentList.size(); i++) {
            componentList.get(i).getModel().renderNoTex();
        }
    }
    
    public void attach(Component component){
        componentList.add(component);
    }
}
