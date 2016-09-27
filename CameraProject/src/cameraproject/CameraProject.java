/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


/**
 *
 * @author jiri
 */
public class CameraProject {

    private long window;
    private int wHeight;
    private int wWidth;
    
    private CarrierS carrier;
    private Land land;
    private int imgCounter;

    public void run() {
        imgCounter = 0;
        
        try {
            init();
            loop();

            // Free the window callbacks and destroy the window
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        } finally {
            // Terminate GLFW and free the error callback
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        //glfwWindowHint(ENABLE_OPENGL_2X_SMOOTH);
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);

        wWidth = 1500;
        wHeight = 800;
        // Create the window
        window = glfwCreateWindow(wWidth, wHeight, "Hello World!", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                window, 0, 0);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
       
    }

    private Model drawCarrier(float posX, float posY) {
        carrier.setModel(posX,posY);
        return carrier.model;
    }

    private Model drawLand() {
        land.setModel();
        return land.model;       
    }
   
    private void loop() {
        
                carrier = new CarrierS(-0.5f,0.5f,0.5f);
        land=new Land(wWidth, wHeight);
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 900, 0, 600, 100, -100);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        //Set the camera perspective
        glLoadIdentity(); //Reset the camera
        
        
/*        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, w, h);
        int width = w.get(0);
        int height = h.get(0);

        
        int panWidth = 300;
        int panHeight = height;

        GUILoader guiLoader = new GUILoader();
        Panel pan = new Panel(width-panWidth,0,panWidth,panHeight,new Color(0.98f,0.98f,0.98f));
        guiLoader.attach(pan);
        guiLoader.attach(new Label(25,50,panWidth-50,30,new Color(0.9f,0.9f,0.9f),pan));
        guiLoader.attach(new Label(25,100,panWidth-50,30,new Color(0.9f,0.9f,0.9f),pan));
        guiLoader.attach(new Label(25,150,panWidth-50,30,new Color(0.9f,0.9f,0.9f),pan));
        guiLoader.attach(new Label(25,200,panWidth-50,30,new Color(0.9f,0.9f,0.9f),pan));
        guiLoader.attach(new Label(25,250,panWidth-50,30,new Color(0.9f,0.9f,0.9f),pan));
        guiLoader.attach(new Label(25,300,panWidth-50,30,new Color(0.9f,0.9f,0.9f),pan));
        guiLoader.attach(new Label(25,350,panWidth-50,30,new Color(0.9f,0.9f,0.9f),pan));
        
        guiLoader.attach(new Label(25,450,panWidth-50,panHeight-30-450,new Color(0.9f,0.9f,0.9f),pan));
        glAttachShader(program, vs);*/

        Shader shader = new Shader();
        Camera camera = new Camera(640,480,new Vector3f(0,0,8f),new Vector3f(0f,0,10f),35f);
        Camera camera2 = new Camera(1024,768,new Vector3f(0,0,9f),new Vector3f(1f,0f,10f),35f);
        
        int mainWidth=wWidth-300;
        int mainHeight=wHeight;
        int sideHeight=wHeight-200;
        
        Vector3f carrierPos=new Vector3f(0,0,9f);
             
        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f );
            GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer	
            //glLoadIdentity();	
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            //glClear(GL_COLOR_BUFFER_BIT);
            
            shader.bind();
            //shader.setUniform("projection", camera2.getProjection());
            
            camera2.focus=new Vector3f(carrierPos.x,carrierPos.y,10.0f);
            glViewport(0,0,mainWidth,mainHeight);
            drawMainView(shader,carrierPos,camera2);
            carrierPos.x+=0.5f;
            
            camera2.setPosition(carrierPos);
            
            glViewport(mainWidth,sideHeight,300,200);
            //camera.setPosition(carrierPos);
            
            //drawMainView(shader,carrierPos,camera);
                     
            
            glfwSwapBuffers(window); // swap the color buffers
            glfwPollEvents();
            if(carrierPos.x<2.6f){
                System.out.println("taken");
                takePicture();
            }else{break;}
            
            
        }
    }
    
    public void drawMainView(Shader shader, Vector3f carrierPos, Camera camera){
        Model model;
        
        shader.setUniform("projection", camera.getProjection()); 
        shader.setUniform("sampler", 0);
        
        model=drawLand();            
        model.render();
        model=drawCarrier(carrierPos.x,carrierPos.y);
        model.render();
    }
    
    public void takePicture(){
        int width=wWidth-300;
        int height=wHeight;
        
        String name;
        
        GL11.glReadBuffer(GL11.GL_FRONT);
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );
       
        name="img/screenshot" + imgCounter + ".jpg"; 
        imgCounter++;
        saveImage(name, width, height, buffer, bpp);
    }
    
    public void saveImage(String name, int width, int height, ByteBuffer buffer,int bpp){
    File file = new File(name);
    String format = "jpg";
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   
    for(int x = 0; x < width; x++) 
    {
        for(int y = 0; y < height; y++)
        {
            int i = (x + (width * y)) * bpp;
            int r = buffer.get(i) & 0xFF;
            int g = buffer.get(i + 1) & 0xFF;
            int b = buffer.get(i + 2) & 0xFF;
            image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
    }
    }
        try {
            ImageIO.write(image, format, file);
        } catch (Exception ex) {
            System.out.println("exc");
            Logger.getLogger(CameraProject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        new CameraProject().run();
    }
}