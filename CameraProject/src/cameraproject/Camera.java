/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author Jirka3
 */
public class Camera {
    public Vector3f position;
    protected Matrix4f projection;
    protected Matrix4f view;
    protected Vector3f focus;
    protected float fov;
    
    protected int width;
    protected int height;

public Camera(int width, int height, Vector3f eye, Vector3f focus, float fov){
    this.width=width;
    this.height=height;
    
    position= eye;
    this.focus=focus;
    this.fov=fov;
    projection=new Matrix4f().setPerspective(fov, width/height, 0.1f, 100f);
    view=new Matrix4f().setLookAt(eye, this.focus, new Vector3f(0,1,0));
} 

public void setPosition(Vector3f position){
    this.position = position;
}

public void addPosition(Vector3f position){
    this.position.add(position);
}

public Vector3f getPosition(){
    return position;
}

public Matrix4f getProjection(){
    view=new Matrix4f().setLookAt(this.position, this.focus, new Vector3f(0,1,0));
    projection=new Matrix4f().setPerspective(fov, width/height, 0.1f, 100f);
    Matrix4f target = projection.mul(view);
    return target;
}
}