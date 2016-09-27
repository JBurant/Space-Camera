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
public class Label extends Component{
    
    public Label(int xCoor, int yCoor, int width, int height) {
        super(xCoor, yCoor, width, height);
        this.Init();
    }

    public Label(int xCoor, int yCoor, int width, int height, Color color) {
        super(xCoor, yCoor, width, height, color);
        this.Init();
    }

    public Label(int xCoor, int yCoor, int width, int height, Component parent) {
        super(xCoor, yCoor, width, height, parent);
        this.Init();
    }

    public Label(int xCoor, int yCoor, int width, int height, Color color, Component parent) {
        super(xCoor, yCoor, width, height, color, parent);
        this.Init();
    }
    
    public final void Init(){ 
    }
    
}
