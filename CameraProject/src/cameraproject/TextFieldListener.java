/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author Jirka3
 */
public class TextFieldListener implements DocumentListener{
    private HashMap<Document, FloatWrapper> documentMapping;
    
    public TextFieldListener(){
        documentMapping=new HashMap<>();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        getTheValue(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
         getTheValue(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        getTheValue(e);
    }
    
    private void getTheValue(DocumentEvent e){
        String text="";
        float val;
        
        try {
            text=e.getDocument().getText(0, e.getDocument().getLength());
        } catch (Exception ex) {
            Logger.getLogger(TextFieldListener.class.getName()).log(Level.SEVERE, null, ex);
        }

        try{
            val = Float.parseFloat(text);
            documentMapping.get(e.getDocument()).setValue(val);
        }catch(Exception ex){
            //Logger.getLogger(TextFieldListener.class.getName()).log(Level.SEVERE, null, ex);     
        }
        
        System.out.println(text);
    }
    
    public void addMapping(Document key, FloatWrapper value){
        documentMapping.put(key, value);
    }
    
}
