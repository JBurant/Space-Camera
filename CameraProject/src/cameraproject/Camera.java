/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import utils.Matrix4f;
import utils.Vector3f;

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
    protected float alpha;
    protected float omega;
    
    protected int width;
    protected int height;

public Camera(int width, int height, Vector3f eye, Vector3f focus, float fov, float alpha, float omega){
    this.width=width;
    this.height=height;
    
    this.alpha=alpha;
    this.omega=omega;
    
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
    Vector3f foc=new Vector3f(focus.x, focus.y,focus.z);
    foc.x=this.focus.z*(float)Math.tan(alpha)+this.position.x;
    foc.y=this.focus.z*(float)Math.tan(omega)+this.position.y;
    view=new Matrix4f().setLookAt(this.position, foc, new Vector3f(0,1,0));
    projection=new Matrix4f().setPerspective(fov, width/height, 0.1f, 100f);
    Matrix4f target = projection.mul(view);
    return target;
}
}