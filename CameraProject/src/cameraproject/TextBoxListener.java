/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Jirka3
 */
public class TextBoxListener implements ActionListener{
    public void actionPerformed(ActionEvent event) {
        //convertInput()
    }
            
    private float convertInput(String text) {
        float ret=0;

        try{
            ret=Float.parseFloat(text);
        }catch(Exception ex){
        }
        System.out.println("converting");
        return ret;        
    }
}
