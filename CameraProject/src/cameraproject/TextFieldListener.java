/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * Listener for the input JTextFields.
 * @author Jiri Burant
 */
public class TextFieldListener implements DocumentListener{
    /**
     * Collection of documents it listens to, associated with inner float variables.
     */
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
    
    /**
     * If one of the documents changes, determine which one and set according variable.
     * @param e Event raised by the change of one of the input JTextFields.
     */
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
    
    /**
     * Add new key-value pair.
     * @param key Source Document.
     * @param value Target inner variable.
     */
    public void addMapping(Document key, FloatWrapper value){
        documentMapping.put(key, value);
    }
    
}
