/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static org.lwjgl.opengl.GL11.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
    private Target target;
    
    private int imgCounter;
    
    private State cameraState;
    private boolean moving;
    private float stepSize;
    private InputData inputData;
    private boolean doMove;
    
    private Vector3f step;
    
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
        inputData=new InputData();
        
        JFrame frame = new JFrame();        
        
        JPanel sidePanel = new JPanel();
        setUpGUI(sidePanel);
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
    
    private void setUpGUI(JPanel sidePanel){
        TextFieldListener listener = new TextFieldListener();
        
        JButton button = new JButton("START");
        JButton buttonLoad = new JButton("Load Texture");
        
        JPanel initPosPanel = new JPanel();
        JPanel endPosPanel = new JPanel();
        JPanel stepSizePanel = new JPanel();
        
        JPanel cameraControlPanel = new JPanel();
        
        initPosPanel.setLayout(new BoxLayout(initPosPanel, BoxLayout.LINE_AXIS));
        endPosPanel.setLayout(new BoxLayout(endPosPanel, BoxLayout.LINE_AXIS));
        stepSizePanel.setLayout(new BoxLayout(stepSizePanel, BoxLayout.LINE_AXIS));
        cameraControlPanel.setLayout(new BoxLayout(cameraControlPanel, BoxLayout.LINE_AXIS));
        
        JLabel labelInitPos=new JLabel("Initial position");
        JLabel labelEndPos=new JLabel("End position");
        JLabel labelStepSize=new JLabel("Number of Images");
        JLabel cameraLabel=new JLabel("Camera angles");
        JLabel cameraLabelAlpha = new JLabel("alpha");
        JLabel cameraLabelOmega = new JLabel("omega");
        
        JTextField textFieldInitPosx = new JTextField(5);
        textFieldInitPosx.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldInitPosx.getDocument(), inputData.initPosX);
        JTextField textFieldInitPosy = new JTextField(5);        
        textFieldInitPosy.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldInitPosy.getDocument(), inputData.initPosY);
        JTextField textFieldEndPosx = new JTextField(5);
        textFieldEndPosx.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldEndPosx.getDocument(), inputData.endPosX);
        JTextField textFieldEndPosy = new JTextField(5);
        textFieldEndPosy.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldEndPosy.getDocument(), inputData.endPosY);
        JTextField textFieldStepSize = new JTextField(10);
        textFieldStepSize.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldStepSize.getDocument(), inputData.noImages);
        
        JTextField textFieldAlpha = new JTextField(3);
        textFieldAlpha.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldAlpha.getDocument(), inputData.alpha);
        JTextField textFieldOmega = new JTextField(3);
        textFieldOmega.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldOmega.getDocument(), inputData.omega);
        
        stepSizePanel.add(labelStepSize);
        stepSizePanel.add(Box.createHorizontalStrut(10));
        stepSizePanel.add(textFieldStepSize);
        stepSizePanel.setMaximumSize(stepSizePanel.getPreferredSize());
        
        initPosPanel.add(labelInitPos);
        initPosPanel.add(Box.createHorizontalStrut(10));
        initPosPanel.add(textFieldInitPosx);
        initPosPanel.add(Box.createHorizontalStrut(10));
        initPosPanel.add(textFieldInitPosy);
        initPosPanel.setMaximumSize(initPosPanel.getPreferredSize());
        
        endPosPanel.add(labelEndPos);
        endPosPanel.add(Box.createHorizontalStrut(10));
        endPosPanel.add(textFieldEndPosx);
        endPosPanel.add(Box.createHorizontalStrut(10));
        endPosPanel.add(textFieldEndPosy);
        endPosPanel.setMaximumSize(endPosPanel.getPreferredSize());
        
        cameraLabel.setMaximumSize(cameraLabel.getPreferredSize());
        
        cameraControlPanel.add(cameraLabelAlpha);
        cameraControlPanel.add(textFieldAlpha);
        cameraControlPanel.add(cameraLabelOmega);
        cameraControlPanel.add(textFieldOmega);
        cameraControlPanel.setMaximumSize(cameraControlPanel.getPreferredSize());
        
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
        
        sidePanel.setSize(300,DISPLAY_HEIGHT);

        sidePanel.add(Box.createVerticalStrut(50));
        sidePanel.add(stepSizePanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(initPosPanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(endPosPanel);
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(cameraLabel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(cameraControlPanel);
        
        sidePanel.add(button);
        sidePanel.add(buttonLoad);
       
        JFileChooser fc = new JFileChooser();

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                step=new Vector3f();
                step.x=(inputData.endPosX.getValue()-inputData.initPosX.getValue())/inputData.noImages.getValue();
                step.y=(inputData.endPosY.getValue()-inputData.initPosY.getValue())/inputData.noImages.getValue();

                System.out.println(step);
                doMove=true;
            }
            
            private int convertInput(String text) {
                int ret=0;
                
                try{
                    ret=Integer.parseInt(text);
                }catch(Exception ex){
                }
                return ret;
            }
        });
        
        buttonLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int returnVal = fc.showOpenDialog(sidePanel);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        System.out.println(file.getName());
                        land.setTextureName(file.getAbsolutePath());
                }
        }    });
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
        carrier = new CarrierS(-0.5f,0.5f,9f);
        land=new Land(wWidth, wHeight, "img/tahiti.jpg");
        target=new Target();
        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 900, 0, 600, 100, -100);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity(); //Reset the camera

        Shader shader = new Shader();
        Camera camera = new Camera(640,480,new Vector3f(0,0,8f),new Vector3f(0f,0,10f),35f,0,0);
        Camera camera2 = new Camera(1024,768,new Vector3f(0,0,9f),new Vector3f(1f,0f,10f),35f,0,0);
        Camera currentCamera = camera;
        
        int mainWidth=wWidth-300;
        int mainHeight=wHeight;
        
        moving=false;
        stepSize=0.5f;
        
        Vector3f carrierPos=new Vector3f(0,0,9f);
        Vector3f targetPos=new Vector3f(1f,0f,9f);
        
        cameraState=State.OVERVIEW;
        boolean takePic=false;
        
      while(!Display.isCloseRequested()) {
      if(Display.isVisible()) {
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f );
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer	
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);            
            shader.bind();
            
            if(doMove){
                moving=true;
                doMove=false;
                
                camera2.alpha=inputData.alpha.getValue();
                camera2.omega=inputData.omega.getValue();
            }
            
            if(moving){
                currentCamera=camera2;
                carrierPos.add(step);
                camera2.setPosition(carrierPos);
            }else{
                currentCamera=camera;
            }
            
            targetPos.x=inputData.endPosX.getValue();
            targetPos.y=inputData.endPosY.getValue();
            
            camera2.focus=new Vector3f(carrierPos.x,carrierPos.y,10.0f);
            glViewport(0,0,mainWidth,mainHeight);
            drawMainView(shader,carrierPos,currentCamera,targetPos);
            
            Display.update();
            Display.sync(60);
            
            if(takePic){
                if(imgCounter<inputData.noImages.getValue()){
                    System.out.println("taken");
                    System.out.println(carrierPos);
                    takePicture();
                }else{
                    moving=false;
                    imgCounter=0;
                }
            }else{
                carrierPos.x=inputData.initPosX.getValue();
                carrierPos.y=inputData.initPosY.getValue();
            }
      }
      
      takePic=moving;

    }
      
      System.out.println("destroy");
      Display.destroy();
    }
    
    public void drawMainView(Shader shader, Vector3f carrierPos, Camera camera, Vector3f targetPos){
        Model model;
        
        shader.setUniform("projection", camera.getProjection()); 
        shader.setUniform("sampler", 0);
        
        model=drawLand();            
        model.render();
        model=drawCarrier(carrierPos.x,carrierPos.y);
        model.render();
        model=drawTarget(targetPos.x,targetPos.y);
        model.render();
    }
    
    private Model drawTarget(float posX, float posY){
        target.setModel(posX,posY);
        return target.model;
    }
    
    public void takePicture(){
        int width=wWidth-300;
        int height=wHeight;
        
        String name;
        
        glReadBuffer(GL_FRONT);
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer );
       
        name="img/screenshots/screenshot" + imgCounter + ".jpg"; 
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