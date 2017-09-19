/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.AnalogListener;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.Vector;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.system.AppSettings;
/**
 * @version 1.0.0
 * @author Zehua Wang
 * @Usage: To change the view of the camera hold left mouse and turn
 */
public class DragonTest extends SimpleApplication {
    public static void main(String[] args){
    DragonTest app = new DragonTest();
    app.start();
    }
    //set the variable we need to use
    protected Spatial grog;
    private ChaseCamera chaseCam;
    Boolean isRunning = true;   
    @Override
    public void simpleInitApp() {
    //disable the default First Person Camera    
    flyCam.setEnabled(false);
    flyCam.setMoveSpeed(30);
    //import a material 
    Material mat_default = new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
    //import the grog model and set the scale and location
    grog = assetManager.loadModel("Models/grog5k/grog5k.j3o");
    rootNode.attachChild(grog);
    grog.setMaterial(mat_default);
    grog.setLocalTranslation(0f,8.9f,0f);
    grog.scale(0.25f,0.25f,0.25f);
    //create a new chase camera to the grog and bundle it to grog
    chaseCam = new ChaseCamera(cam, grog, inputManager);
    chaseCam.setSmoothMotion(true);
    //add the HighField Scene map
    Spatial Scene = assetManager.loadModel("Scenes/HeightMap.j3o");
    rootNode.attachChild(Scene);
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(-0.0f,-0.0f,-0.0f));
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);
    //load my custom keybiding
    initKeys();
    //add the triceratops on the corner and set the material
    Spatial trice_1 = assetManager.loadModel("Models/triceratops/triceratops.j3o");
    Spatial trice_2 = assetManager.loadModel("Models/triceratops/triceratops.j3o");
    Spatial trice_3 = assetManager.loadModel("Models/triceratops/triceratops.j3o");
    Spatial trice_4 = assetManager.loadModel("Models/triceratops/triceratops.j3o");
    trice_1.setMaterial(mat_default);
    trice_2.setMaterial(mat_default);
    trice_3.setMaterial(mat_default);
    trice_4.setMaterial(mat_default);
    //put the triceratops at the corner of the wall
    trice_1.setLocalScale(0.25f,0.25f,0.25f);
    trice_1.move(60.0f,14.5f,40.0f);
    trice_2.setLocalScale(0.25f,0.25f,0.25f);
    trice_2.move(65.f,14.5f,-50.0f);
    trice_3.setLocalScale(0.25f,0.25f,0.25f);
    trice_3.move(-30.0f,14.5f,40.0f);
    trice_4.setLocalScale(0.25f,0.25f,0.25f);
    trice_4.move(-30.0f,14.5f,-60.0f);
    rootNode.attachChild(trice_1);
    rootNode.attachChild(trice_2);
    rootNode.attachChild(trice_3);
    rootNode.attachChild(trice_4);
    //display a line of text with the deafult font
    guiNode.detachAllChildren();
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText helloText = new BitmapText(guiFont, false);
    helloText.setSize(guiFont.getCharSet().getRenderedSize());
    helloText.setText("To hold and change the camera view hold the left mouse");
    helloText.setLocalTranslation(180, helloText.getLineHeight(), 0);
    guiNode.attachChild(helloText);
    }
    //Define the key mapping for the grog
    private void initKeys(){
        inputManager.addMapping("Front", new KeyTrigger(KeyInput.KEY_W));//The grog go forward
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S)); //The grog go back ward
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A)); //The grog go left
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));//The grog go right
        inputManager.addListener(analogListener,"Front","Back","Left","Right");   
    }
    private AnalogListener analogListener = new AnalogListener() {
    public void onAnalog(String name, float value, float tpf) {
      if (isRunning) {
        if (name.equals("Right")) {
        grog.move(5*tpf,0,0);  
        }
        if (name.equals("Left")) {
        grog.move(-5 * tpf, 0, 0);
        }
        if (name.equals("Front")) {
        grog.move(0, 0, -5 * tpf);
        }
        if (name.equals("Back")) {
        grog.move(0, 0, 5 * tpf);
        }
      } 
    }
  };
}
    

