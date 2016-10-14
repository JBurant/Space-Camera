/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static org.lwjgl.opengl.GL11.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

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
    
    private State cameraState;
    private boolean moving;
    private float stepSize;
    
    private final int DISPLAY_WIDTH=1500;
    private final int DISPLAY_HEIGHT=800;
    
    public enum State {
        OVERVIEW, CARRIER
    }
        

    public void run() {
        imgCounter = 0;
        
        try {
            init();
            loop();

        } finally {
            System.out.println("Error in run");
        }
        System.exit(0);
    }

    private void init() {
        JFrame frame = new JFrame();        
         
        // The exit button.
        JButton button = new JButton("START");
        JPanel sidePanel = new JPanel();
        
        JLabel labelInitPos=new JLabel("Initial position");
        JLabel labelEndPos=new JLabel("Endposition");
        JTextField textFieldInitPosx = new JTextField(5);
        JTextField textFieldInitPosy = new JTextField(5);
        
        JTextField textFieldEndPosx = new JTextField(5);
        JTextField textFieldEndPosy = new JTextField(5);
        
        
        
        JTextField textField = new JTextField(10);
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //stepSize=convertInput(textField.getText());
            }

            private float convertInput(String text) {
                float ret=0;
                
                try{
                    ret=Float.parseFloat(text);
                }catch(Exception ex){
                }
                return ret;
            }
        });
        
        sidePanel.setSize(300,DISPLAY_HEIGHT);
        sidePanel.add(button);
        sidePanel.add(textField);
        
        sidePanel.add(labelInitPos);
        sidePanel.add(textFieldInitPosx);
        sidePanel.add(textFieldInitPosy);
        sidePanel.add(labelEndPos);
        sidePanel.add(textFieldEndPosx);
        sidePanel.add(textFieldEndPosy);
        
         
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                moving=true;
                stepSize=convertInput(textField.getText());
            }
            
            private float convertInput(String text) {
                float ret=0;
                
                try{
                    ret=Float.parseFloat(text);
                }catch(Exception ex){
                }
                return ret;
            }
        });
         
        // Create a new canvas and set its size.
        Canvas canvas = new Canvas();
        // Must be 640*480 to match the size of an Env3D window
        canvas.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        // This is the magic!  The setParent method attaches the 
        // opengl window to the awt canvas.
        try {
            Display.setParent(canvas);
        } catch (Exception e) {
        }
         
        // Construct the GUI as normal
        frame.add(sidePanel, BorderLayout.EAST);
        frame.add(canvas, BorderLayout.CENTER);
         
        frame.pack();
        frame.setVisible(true);

        try {
            Display.setDisplayMode(new DisplayMode(1500,800));
            Display.setFullscreen(false);
            Display.setTitle("Camera project");
            Display.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(CameraProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        wWidth = 1500;
        wHeight = 800;
        
            glClearColor(0.0f,0.0f,0.0f,0.0f);
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_LIGHTING);
    glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
 
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(0.0f,DISPLAY_WIDTH,0.0f,DISPLAY_HEIGHT);
    glPushMatrix();
 
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glPushMatrix();
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
        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 900, 0, 600, 100, -100);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity(); //Reset the camera

        Shader shader = new Shader();
        Camera camera = new Camera(640,480,new Vector3f(0,0,8f),new Vector3f(0f,0,10f),35f);
        Camera camera2 = new Camera(1024,768,new Vector3f(0,0,9f),new Vector3f(1f,0f,10f),35f);
        Camera currentCamera = camera;
        
        int mainWidth=wWidth-300;
        int mainHeight=wHeight;
        
        moving=false;
        stepSize=0.5f;
        
        Vector3f carrierPos=new Vector3f(0,0,9f);
        
        cameraState=State.OVERVIEW;

      while(!Display.isCloseRequested()) {
      if(Display.isVisible()) {
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f );
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer	
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);            
            shader.bind();
            
            camera2.focus=new Vector3f(carrierPos.x,carrierPos.y,10.0f);
            glViewport(0,0,mainWidth,mainHeight);
            drawMainView(shader,carrierPos,currentCamera);
            
            if(moving){
                carrierPos.x+=stepSize;
                camera2.setPosition(carrierPos);
                System.out.println(stepSize);
            
                if(carrierPos.x<2.6f){
                    System.out.println("taken");
                    takePicture();
                }else{moving=false;}
            }
      }
      
      Display.update();
      Display.sync(60);
    }
      
      System.out.println("destroy");
      Display.destroy();
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
        
        glReadBuffer(GL_FRONT);
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer );
       
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