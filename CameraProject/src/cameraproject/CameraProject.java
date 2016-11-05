/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraproject;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import utils.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

/**
 * This is the main class of the project.
 * It sets up the GUI, the openGL and the initial values for spacecraft and camera. It also contains the main rendering
 * loop and a method for saving the resulting image.
 * 
 * @author jiri
 *
 */
public class CameraProject {
    /**
    * Height of the openGL window.
    */
    public int wHeight;
    /**
     * Width of the openGL window.
     */
    public int wWidth;
    /**
     * The space carrier object.
     * Contains info about the position, size and camera angle.
     */
    public Carrier carrier;
    /**
     * The surface object.
     * Contains info about the size and of the surface plane and its texture.
     */
    public Land land;
    /**
     * The target object.
     * Symbolises the target area, where the carrier should go.
     */
    public Target target;
    
    /**
     * Counts the number of images taken during flight.
     * The spacecraft takes specified number of images, equally spaced.
     */
    public int imgCounter;
    /**
     * True if the carrier is moving.
     */
    public boolean moving;
    /**
     * True if a new texture has been loaded.
     */
    public boolean textureChanged;
    /**
     * Size of the flight step, distance between two images.
     * Set by user from the GUI.
     */
    public float stepSize;
    /**
    * Interface between the input TextFields and application logic.
    */
    public InputData inputData;
    /**
    * Start moving the spacecraft.
    */
    public boolean doMove;
    /**
     * One flight step.
     */
    public Vector3f step;
    /**
     * Width of the created display.
     */
    public final int DISPLAY_WIDTH=1500;
    /**
     * Height of the created display.
     */
    public final int DISPLAY_HEIGHT=800;        
    /**
    *  File extension for the screenshots.
    */
    private String fileExt;
    
    /**
     * Initializes the application and enters the render loop.
     */
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

    /**
     * Initializes the GUI, openGL canvas and sets up openGL.
     */
    public void init() {
        inputData=new InputData();
        fileExt="jpg";
        
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

        //Create the Display
        try {
            Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
            Display.setFullscreen(false);
            Display.setTitle("Camera project");
            Display.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(CameraProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        wWidth = 1500;
        wHeight = 800;
        
        //Set up the openGL
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
    
    /**
     * Defines the GUI.
     * @param sidePanel Target for the GUI components.
     */
    public void setUpGUI(JPanel sidePanel){
        TextFieldListener listener = new TextFieldListener();
        
        //Construct buttons
        JButton startButton = new JButton("START");
        JButton buttonLoad = new JButton("Load Texture");
        
        //Construct panels
        JPanel initPosPanel = new JPanel();
        JPanel endPosPanel = new JPanel();
        JPanel stepSizePanel = new JPanel();
        JPanel formatTypePanel = new JPanel();
        JPanel cameraControlPanel = new JPanel();
        
        //Set Layouts as BoxLayouts
        initPosPanel.setLayout(new BoxLayout(initPosPanel, BoxLayout.LINE_AXIS));
        endPosPanel.setLayout(new BoxLayout(endPosPanel, BoxLayout.LINE_AXIS));
        stepSizePanel.setLayout(new BoxLayout(stepSizePanel, BoxLayout.LINE_AXIS));
        cameraControlPanel.setLayout(new BoxLayout(cameraControlPanel, BoxLayout.LINE_AXIS));
        formatTypePanel.setLayout(new BoxLayout(formatTypePanel, BoxLayout.LINE_AXIS));
        
        //Construct labels
        JLabel labelInitPos=new JLabel("Initial position");
        JLabel labelEndPos=new JLabel("End position   ");
        JLabel labelStepSize=new JLabel("Number of Images");
        JLabel labelFormatType=new JLabel("Output format");
        JLabel cameraLabel=new JLabel("Camera angles");
        JLabel cameraLabelAlpha = new JLabel("alpha");
        JLabel cameraLabelOmega = new JLabel("omega");
        JLabel cameraLabelFov = new JLabel("field of view");
        
        String [] data= new String[]{"jpg","png"};
        
        JList list = new JList(data); //data has type Object[]
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(2);
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
              fileExt=data[list.getSelectedIndex()];
            }
        });
        JScrollPane listScroller = new JScrollPane(list);
        

        //Construct text fields and add documentListeners with mappings
        JTextField textFieldInitPosx = new JTextField("0",5);
        textFieldInitPosx.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldInitPosx.getDocument(), inputData.initPosX);
        JTextField textFieldInitPosy = new JTextField("0",5);        
        textFieldInitPosy.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldInitPosy.getDocument(), inputData.initPosY);
        JTextField textFieldInitPosz = new JTextField("9",5);
        textFieldInitPosz.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldInitPosz.getDocument(), inputData.initPosZ);

        JTextField textFieldEndPosx = new JTextField("0",5);
        textFieldEndPosx.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldEndPosx.getDocument(), inputData.endPosX);
        JTextField textFieldEndPosy = new JTextField("0",5);
        textFieldEndPosy.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldEndPosy.getDocument(), inputData.endPosY);
        JTextField textFieldEndPosz = new JTextField("9",5);
        textFieldEndPosz.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldEndPosz.getDocument(), inputData.endPosZ);
        
        JTextField textFieldStepSize = new JTextField(10);
        textFieldStepSize.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldStepSize.getDocument(), inputData.noImages);
        
        JTextField textFieldAlpha = new JTextField("0",3);
        textFieldAlpha.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldAlpha.getDocument(), inputData.alpha);
        JTextField textFieldOmega = new JTextField("0",3);
        textFieldOmega.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldOmega.getDocument(), inputData.omega);
        JTextField textFieldFov = new JTextField("0",3);
        textFieldFov.getDocument().addDocumentListener(listener);
        listener.addMapping(textFieldFov.getDocument(), inputData.fov);
        
        //Fill the stepSizePanel
        stepSizePanel.add(labelStepSize);
        stepSizePanel.add(Box.createHorizontalStrut(10));
        stepSizePanel.add(textFieldStepSize);
        stepSizePanel.setMaximumSize(stepSizePanel.getPreferredSize());
        
        //Fill the initPosPanel
        initPosPanel.add(labelInitPos);
        initPosPanel.add(Box.createHorizontalStrut(10));
        initPosPanel.add(textFieldInitPosx);
        initPosPanel.add(Box.createHorizontalStrut(10));
        initPosPanel.add(textFieldInitPosy);
        initPosPanel.add(Box.createHorizontalStrut(10));
        initPosPanel.add(textFieldInitPosz);
        initPosPanel.setMaximumSize(initPosPanel.getPreferredSize());
        
        //Fill the endPosPanel
        endPosPanel.add(labelEndPos);
        endPosPanel.add(Box.createHorizontalStrut(10));
        endPosPanel.add(textFieldEndPosx);
        endPosPanel.add(Box.createHorizontalStrut(10));
        endPosPanel.add(textFieldEndPosy);
        endPosPanel.add(Box.createHorizontalStrut(10));
        endPosPanel.add(textFieldEndPosz);
        endPosPanel.setMaximumSize(endPosPanel.getPreferredSize());
        
        cameraLabel.setMaximumSize(cameraLabel.getPreferredSize());
        
        //Fill the cameraControlPanel
        cameraControlPanel.add(cameraLabelAlpha);
        cameraControlPanel.add(textFieldAlpha);
        cameraControlPanel.add(cameraLabelOmega);
        cameraControlPanel.add(textFieldOmega);
        cameraControlPanel.add(cameraLabelFov);
        cameraControlPanel.add(textFieldFov);
        cameraControlPanel.setMaximumSize(cameraControlPanel.getPreferredSize());
        
        formatTypePanel.add(labelFormatType);
        formatTypePanel.add(Box.createHorizontalStrut(10));
        formatTypePanel.add(listScroller);
        formatTypePanel.setMaximumSize(formatTypePanel.getPreferredSize());
        
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
        sidePanel.setSize(300,DISPLAY_HEIGHT);

        //Fill the sidePanel
        sidePanel.add(Box.createVerticalStrut(50));
        sidePanel.add(stepSizePanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(formatTypePanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(initPosPanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(endPosPanel);
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(cameraLabel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(cameraControlPanel);
        sidePanel.add(startButton);
        sidePanel.add(buttonLoad);
       
        //Needed for show open file dialogue
        JFileChooser fc = new JFileChooser();

        //Listener for the start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                step=new Vector3f();
                step.x=(inputData.endPosX.getValue()-inputData.initPosX.getValue())/inputData.noImages.getValue();
                step.y=(inputData.endPosY.getValue()-inputData.initPosY.getValue())/inputData.noImages.getValue();
                step.z=(inputData.endPosZ.getValue()-inputData.initPosZ.getValue())/inputData.noImages.getValue();
                
                doMove=true;
            }
        });
        
        //Listener for the load button
        buttonLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int returnVal = fc.showOpenDialog(sidePanel);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        System.out.println(file.getName());
                        land.setTextureName(file.getAbsolutePath());
                        textureChanged=true;
                }
        }    });
    }
   
    /**
     * The main render loop.
     * Periodically renders the scene on the canvas, using the openGL models of carrier, target and land.
     */
    public void loop() {
        //Create new scene objects
        carrier = new Carrier(-0.5f,0.5f,9f);
        land=new Land(wWidth, wHeight, "img/tahiti.jpg");
        target=new Target();

        //Create new shader and cameras
        Shader shader = new Shader();
        Camera camera = new Camera(640,480,new Vector3f(0,0,8f),new Vector3f(0f,0,10f),35f,0,0);
        Camera camera2 = new Camera(1024,768,new Vector3f(0,0,9f),new Vector3f(1f,0f,10f),35f,0,0);
        Camera currentCamera = camera;
        
        int mainWidth=wWidth-300;
        int mainHeight=wHeight;
        boolean renderTargetCarrier=true;
        
        moving=false;
        stepSize=0.5f;
        
        Vector3f carrierPos=new Vector3f(0,0,9f);
        Vector3f targetPos=new Vector3f(1f,0f,9f);

        boolean takePic=false;
        
      while(!Display.isCloseRequested()) {
        if(Display.isVisible()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer	
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);            
            shader.bind();
            
            if(textureChanged){
                land.loadTexture();
                textureChanged=false;
            }
            
            if(doMove){
                moving=true;
                doMove=false;
                
                camera2.alpha=inputData.alpha.getValue();
                camera2.omega=inputData.omega.getValue();
                camera2.fov=inputData.fov.getValue();
            }
            
            if(moving){
                currentCamera=camera2;
                carrierPos.add(step);
                camera2.setPosition(carrierPos);
                renderTargetCarrier=false;
            }else{
                currentCamera=camera;
                renderTargetCarrier=true;
            }
            
            targetPos.x=inputData.endPosX.getValue();
            targetPos.y=inputData.endPosY.getValue();
            targetPos.z=inputData.endPosZ.getValue();
            
            camera2.focus=new Vector3f(carrierPos.x,carrierPos.y,10.0f);
            glViewport(0,0,mainWidth,mainHeight);
            drawMainView(shader,carrierPos,currentCamera,targetPos,renderTargetCarrier);
            
            glUseProgram(0);
                        
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
                carrierPos.z=inputData.initPosZ.getValue();
            }
      }
      takePic=moving;
    }
      
      System.out.println("destroy");
      Display.destroy();
    }
    
    /**
     * Draws the Canvas scene.
     * Sets up and renders land, carrier and target.
     * @param shader Used shader.
     * @param carrierPos Position of the carrier.
     * @param camera Camera capturing the scene.
     * @param targetPos Position of the target.
     */
    public void drawMainView(Shader shader, Vector3f carrierPos, Camera camera, Vector3f targetPos, boolean renderTargetCarrier){
        Model model;
        
        shader.setUniform("projection", camera.getProjection()); 
        shader.setUniform("sampler", 0);
        
        model=drawLand();            
        model.render();
        
        if(renderTargetCarrier){
            model=drawCarrier(carrierPos.x,carrierPos.y,carrierPos.z);
            model.render();
            model=drawTarget(targetPos.x,targetPos.y,targetPos.z);
            model.render();
        }
    }
    
    /**
     * Sets a new openGL model to the carrier.
     * @param posX New X coordinate of the carrier
     * @param posY New Y coordinate of the carrier
     * @return Model of the carrier
     */
    public Model drawCarrier(float posX, float posY, float posZ) {
        carrier.setModel(posX,posY,posZ);
        return carrier.model;
    }

    /**
     * Sets a new openGL model to the Land.
     * @return Model of the Land
     */
    public Model drawLand() {
        land.setModel();
        return land.model;       
    }
    
    /**
     * Draw the target.
     * @param posX New X position of the target.
     * @param posY New Y position of the target.
     * @return New model of the target.
     */
    public Model drawTarget(float posX, float posY, float posZ){
        target.setModel(posX,posY,posZ);
        return target.model;
    }
    
    /**
     * Take a snapshot of the scene, excluding the GUI in the right side panel.
     * The snapshot is taken from the glbuffer and then stored in the ByteBuffer.
     */
    public void takePicture(){
        int width=wWidth-300;
        int height=wHeight;

        glReadBuffer(GL_FRONT);
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer );
       
        saveImage(width, height, buffer, bpp);
    }
    
    /**
     * Save the image in the buffer into file.
     * @param width Width of the image.
     * @param height Height of the image.
     * @param buffer The image buffer.
     * @param bpp Color and alpha.
     */
    public void saveImage(int width, int height, ByteBuffer buffer,int bpp){      
    String name="img/screenshots/screenshot" + imgCounter + "." + fileExt; 
    imgCounter++;
    File file = new File(name);
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
            ImageIO.write(image, fileExt, file);
        } catch (Exception ex) {
            System.out.println("exc");
            Logger.getLogger(CameraProject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Main method, calling the run of the application.
     * @param args In the future can contain config file and so on.
     */
    public static void main(String[] args) {
        new CameraProject().run();
    }
}