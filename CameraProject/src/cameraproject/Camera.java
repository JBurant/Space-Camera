/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import utils.Matrix4f;
import utils.Vector3f;

/**
 * Stores basic parameters of the camera and calculates projection and view matrices.
 * @author Jiri Burant
 */
public class Camera {
    /**
     * Position of the camera.
     */
    public Vector3f position;
    /**
     * Projection matrix of the camera.
     */
    protected Matrix4f projection;
    /**
     * View matrix of the camera.
     */
    protected Matrix4f view;
    /**
     * Focus point of the camera.
     */
    protected Vector3f focus;
    /**
     * Field of view of the camera.
     */
    protected float fov;
    /**
     * Angle in the x direction.
     */
    protected float alpha;
    /**
     * Angle in the y direction.
     */
    protected float omega;
    /**
     * Width of the camera image.
     */
    protected float width;
    /**
     * Height of the camera image.
     */
    protected float height;

/**
 * Constructor of camera with lookAt view matrix.
 * @param width Width of the camera image.
 * @param height Height of the camera image.
 * @param eye Position of the observer.
 * @param focus Point of interest.
 * @param fov Field of view of the camera.
 * @param alpha Angle in the x direction.
 * @param omega Angle in the y direction.
 */
public Camera(int width, int height, Vector3f eye, Vector3f focus, float fov, float alpha, float omega){
    this.width=width;
    this.height=height;
    this.alpha=alpha;
    this.omega=omega;
    this.position= eye;
    this.focus=focus;
    this.fov=fov;
    this.projection=new Matrix4f().setPerspective(fov, width/height, 0.1f, 100f);
    this.view=new Matrix4f().setLookAt(eye, this.focus, new Vector3f(0,1,0));
} 

/**
 * Setter for the camera position.
 * @param position Camera position.
 */
public void setPosition(Vector3f position){
    this.position = position;
}

/**
 * Move the camera.
 * @param step How much move the camera. 
 */
public void addPosition(Vector3f step){
    this.position.add(step);
}

/**
 * Getter for the camera position.
 * @return Camera position.
 */
public Vector3f getPosition(){
    return position;
}

/**
 * Recalculate the view and the projection matrix.
 * @return Projection * View matrix.
 */
public Matrix4f getProjection(){
    Vector3f foc=new Vector3f(focus.x, focus.y,focus.z);
    foc.x=(this.focus.z-this.position.z)*(float)Math.tan(alpha)+this.position.x;
    foc.y=this.focus.z*(float)Math.tan(omega)+this.position.y;
    view=new Matrix4f().setLookAt(this.position, foc, new Vector3f(0,1,0));
    projection= perspective(-5f,5f,-5f,5f, 1f, 100f);
    float yScale = (float) (1 / (Math.tan(Math.toRadians(fov / 2))));
    float xScale = yScale / (width/height);
    
    Matrix4f target = projection.mul(view);
    return target;
}
public Matrix4f perspective(float left,float right,float bottom,float top,float near,float far){
        Matrix4f matrix = new Matrix4f();
    
       matrix.m00(-1 / ((float)Math.tan(Math.PI*fov/90)*(width/height)));
       matrix.m11(1 / ((float)Math.tan(Math.PI*fov/90)));
       matrix.m22(-(far +near)/(far - near));
       matrix.m23(-1);
       matrix.m32(2.0f*(near*far)/(near-far));
       System.out.println(Math.PI*fov/90);
       System.out.println(matrix);
       return matrix;
}
}