package com.entitysystem;


import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;




public class EntityTest extends SimpleApplication {

    public static void main(String[] args) {
        EntityTest app = new EntityTest();
        AppSettings aps = new AppSettings(true);
        aps.setVSync(false);
        app.setSettings(aps);
        app.start();
    }

              
    private Node camTrackHelper;
    private EntityManager entityManager = new EntityManager();
    private EntitySpatialsSystem spatials = new EntitySpatialsSystem();
    
    @Override
    public void simpleInitApp() {
        
        setLight();

    // Entity stressTest
    for (int i=0; i<500 ; i++) {
    
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geo = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
//        mat.getAdditionalRenderState().setWireframe(true);
        geo.setMaterial(mat);
        Node selectedSp = new Node();
        selectedSp.attachChild(geo);        
        rootNode.attachChild(selectedSp); 

        // setup Entity
        long ent = entityManager.createEntity();                
        
        ComponentsControl components = entityManager.addComponentControl(ent);
        
        EntityNameComponent name = new EntityNameComponent("ent" + i);
        components.setComponent(name);
        
        // Check for different transform of entity
        Transform tr = new Transform();
        Vector3f loc = new Vector3f((float) Math.random() * 1.0f,(float) Math.random() * 1.0f,(float)Math.random() * 1.0f);
        tr.setTranslation(loc);
//        selectedSp.setLocalTransform(tr);
        
        TransformComponent transform = new TransformComponent(tr);
        components.setComponent(transform);

        // we will get 100 updated entities! Their transform will be updated every frame.
        if (i < 100) components.setToUpdate(false);
        
        EntitySpatialsControl spatialControl = spatials.addSpatialControl(selectedSp, ent, entityManager.getComponentControl(ent));
        spatialControl.setType(EntitySpatialsControl.SpatialType.Node);
        spatialControl.recurseNode();
        
        System.out.println(selectedSp.getUserData("EntityID"));
    }

        // check for hashCode
        System.out.println(entityManager.getComponentControl(1).toString());
        System.out.println(entityManager.getComponentControl(2).toString());
        System.out.println(entityManager.getComponentControl(100).toString());
    
    }

    
 
    
    private void setLight() {
        
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1,1,1,1));
        rootNode.addLight(dl);        
        
        flyCam.setMoveSpeed(30);
        viewPort.setBackgroundColor(ColorRGBA.Gray);   
    }
    


@Override
public void simpleUpdate(float tpf){

}
       
   }
 
    
 
      

