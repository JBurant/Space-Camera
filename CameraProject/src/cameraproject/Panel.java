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
public class Panel extends Component{

    public Panel(int xCoor, int yCoor, int width, int height) {
        super(xCoor, yCoor, width, height);
    }

    public Panel(int xCoor, int yCoor, int width, int height, Color color) {
        super(xCoor, yCoor, width, height, color);
    }

    public Panel(int xCoor, int yCoor, int width, int height, Color color, Component parent) {
        super(xCoor, yCoor, width, height, color, parent);
    }

    public Panel(int xCoor, int yCoor, int width, int height, Component parent) {
        super(xCoor, yCoor, width, height, parent);
    }
}
