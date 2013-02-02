/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.simpleEditor;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;

/**
 *
 * @author mifth
 */
public class EditorBaseParts {
    
    private Application app;
    private AssetManager assetManager;
    private Node rootNode;
    private Node guiNode;
    private Node camTrackHelper;
    private Camera cam;
    private ViewPort viewPort;
    private FlyByCamera flyCam;
    
public EditorBaseParts(Application app) {
    
        this.app = app;
        cam = app.getCamera();
        viewPort = app.getViewPort();
        assetManager = app.getAssetManager();
        rootNode = (Node) app.getViewPort().getScenes().get(0);
        guiNode = (Node) app.getGuiViewPort().getScenes().get(0);
        flyCam = app.getStateManager().getState(FlyCamAppState.class).getCamera();
        flyCam.setEnabled(false);
        
        createSimpleGui();
        setLight();
        setCamTracker();
        EditorCameraSets camSettings = new EditorCameraSets(cam, camTrackHelper, app.getInputManager());
        createGrid();

        EditorGui gui = new EditorGui();
        app.getStateManager().attach(gui); 
        
        
        // Selected Spatial for a while
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geo = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        geo.setMaterial(mat);
        Node selectedSp = new Node();
        selectedSp.attachChild(geo);        
        rootNode.attachChild(selectedSp);  
        
        selectedSp.addControl(new EditorTool(app, selectedSp));
//        EditorTool edt = new EditorTool(this, selectedSp);    
}
    
    private void setCamTracker() {
        
        
        camTrackHelper  = new Node("camTrackHelper");
        
        // Red line for X axis
        final Line xAxis = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0.05f, 0f, 0f));
        xAxis.setLineWidth(2f);
        Geometry gxAxis = new Geometry("XAxis", xAxis);
        gxAxis.setModelBound(new BoundingBox());
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", new ColorRGBA(1.0f, 0.0f, 0.0f, 0.1f));
        mat1.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        gxAxis.setQueueBucket(RenderQueue.Bucket.Transparent);
        gxAxis.setShadowMode(RenderQueue.ShadowMode.Off);
        gxAxis.setMaterial(mat1);
//        gxAxis.setCullHint(CullHint.Never);
        
        camTrackHelper.attachChild(gxAxis);

        
        // Blue line for Y axis
        final Line yAxis = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0.05f, 0f));
        yAxis.setLineWidth(2f);
        Geometry gyAxis = new Geometry("ZAxis", yAxis);
        gyAxis.setModelBound(new BoundingBox());
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", new ColorRGBA(0.0f, 0.0f, 1.0f, 0.1f));
        mat2.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        gyAxis.setQueueBucket(RenderQueue.Bucket.Transparent);        
        gyAxis.setShadowMode(RenderQueue.ShadowMode.Off);
        gyAxis.setMaterial(mat2);
//        gzAxis.setCullHint(CullHint.Never);
        camTrackHelper.attachChild(gyAxis);               
        
        
        // Blue line for Z axis
        final Line zAxis = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 0.05f));
        zAxis.setLineWidth(2f);
        Geometry gzAxis = new Geometry("ZAxis", zAxis);
        gzAxis.setModelBound(new BoundingBox());
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat3.setColor("Color", new ColorRGBA(0.0f, 1.0f, 0.0f, 0.1f));
        mat3.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        gxAxis.setQueueBucket(RenderQueue.Bucket.Transparent);        
        gzAxis.setShadowMode(RenderQueue.ShadowMode.Off);
        gzAxis.setMaterial(mat3);
//        gzAxis.setCullHint(CullHint.Never);
        camTrackHelper.attachChild(gzAxis);       
        
        CameraNode camnode = new CameraNode("this", cam);
//        camnode.attachChild(camTrackHelper);
        rootNode.attachChild(camnode);
        rootNode.attachChild(camTrackHelper);

    }
    
    private void setLight() {
        
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1,1,1,1));
        rootNode.addLight(dl);        

        viewPort.setBackgroundColor(ColorRGBA.Gray);   
    }
    

    
    private void createSimpleGui() {

        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize());
        ch.setText("W,A,S,D,Q,Z, MiddleMouseButton, RightMouseButton, Scroll"); // crosshairs
        ch.setColor(new ColorRGBA(1f,0.8f,0.1f,0.3f));
        ch.setLocalTranslation(viewPort.getCamera().getWidth()*0.1f,viewPort.getCamera().getHeight()*0.1f,0);
        guiNode.attachChild(ch);         

    }
    
    private void createGrid(){
        Node gridNode = new Node("gridNode");
        
        //Create a grid plane
        Geometry g = new Geometry("GRID", new Grid(101, 101, 1f) );
        Material floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floor_mat.getAdditionalRenderState().setWireframe(true);
        floor_mat.setColor("Color", new ColorRGBA(0.3f, 0.3f, 0.3f, 0.1f));
        floor_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        g.setShadowMode(RenderQueue.ShadowMode.Off);
        g.setQueueBucket(RenderQueue.Bucket.Transparent);
        g.setMaterial(floor_mat);
        g.center().move(new Vector3f(0f,0f,0f));
        gridNode.attachChild(g);

        // Red line for X axis
        final Line xAxis = new Line(new Vector3f(-50f, 0f, 0f), new Vector3f(50f, 0f, 0f));
        xAxis.setLineWidth(2f);
        Geometry gxAxis = new Geometry("XAxis", xAxis);
        gxAxis.setModelBound(new BoundingBox());
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(1.0f, 0.2f, 0.2f, 0.2f));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        gxAxis.setQueueBucket(RenderQueue.Bucket.Transparent);
        gxAxis.setShadowMode(RenderQueue.ShadowMode.Off);
        gxAxis.setMaterial(mat);
        gxAxis.setCullHint(Spatial.CullHint.Never);
        
        gridNode.attachChild(gxAxis);

        // Blue line for Z axis
        final Line zAxis = new Line(new Vector3f(0f, 0f, -50f), new Vector3f(0f, 0f, 50f));
        zAxis.setLineWidth(2f);
        Geometry gzAxis = new Geometry("ZAxis", zAxis);
        gzAxis.setModelBound(new BoundingBox());
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(0.2f, 1.0f, 0.2f, 0.2f));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        gxAxis.setQueueBucket(RenderQueue.Bucket.Transparent);        
        gzAxis.setShadowMode(RenderQueue.ShadowMode.Off);
        gzAxis.setMaterial(mat);
        gzAxis.setCullHint(Spatial.CullHint.Never);
        gridNode.attachChild(gzAxis);

        rootNode.attachChild(gridNode);
        
    }    
    
}