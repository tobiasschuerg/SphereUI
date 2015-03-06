package tobiasschuerg.sphere;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.LayerComparator;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.event.ConsumingMouseListener;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.style.ElementId;

import tobiasschuerg.lemur.piemenu.example.CameraMovementFunctions;
import tobiasschuerg.lemur.piemenu.example.CameraMovementState;
import tobiasschuerg.lemur.piemenu.example.CameraToggleState;

/**
 * test
 *
 * @author Tobias Schürg
 */
public class SphereUIExample extends SimpleApplication {
    public static final String SPHERE_SWITCH = "ACTION.SWITCH";
    public static final String SPHERE_GROW = "ACTION.GROW";
    public static final String SPHERE_SHRINK = "ACTION.SHRINK";
    private boolean isSphereShowing = true;

    public static void main(String[] args) {
        SphereUIExample app = new SphereUIExample();
        app.start();
    }
    private Geometry sphere;
    private Checkbox cbSphereAttached;

    public SphereUIExample() {
        super(new StatsAppState(), new CameraMovementState(), new CameraToggleState());
    }
    
    private ActionListener sphereListener = new ActionListener(){
        public void onAction(String name, boolean pressed, float tpf){
            
            if (name.equalsIgnoreCase(SPHERE_SWITCH) && pressed) {
                if (isSphereShowing) {
                    sphere.setCullHint(CullHint.Always);
                    isSphereShowing = false;
                } else {
                    sphere.setCullHint(CullHint.Inherit);
                    isSphereShowing = true;
                }
            } else if (name.equalsIgnoreCase(SPHERE_GROW)) {
                sphere.scale(1.05f);
                System.out.println("x: " + name + " = " + pressed);
            } else if (name.equalsIgnoreCase(SPHERE_SHRINK)) {
                sphere.scale(0.95f);
                System.out.println("x: " + name + " = " + pressed);
            }
        }
    };

    @Override
    public void simpleInitApp() {

        GuiGlobals.initialize(this);
        CameraMovementFunctions.initializeDefaultMappings(GuiGlobals.getInstance().getInputMapper());
        
        inputManager.addMapping(SPHERE_SWITCH, new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping(SPHERE_GROW, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(SPHERE_SHRINK, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addListener(sphereListener, SPHERE_SWITCH);
        inputManager.addListener(sphereListener, SPHERE_GROW);
        inputManager.addListener(sphereListener, SPHERE_SHRINK);

        createFloor();
        createSphere();

        CursorEventControl.addListenersToSpatial(sphere, new SphereListener() {
            @Override
            public void onCursorHold(Vector3f point) {
                Spatial teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
                teapot.setLocalTranslation(point);
                teapot.setLocalScale(0.5f);
                rootNode.attachChild(teapot);
                }
        });
        
        addLights();

        
        
        /*
         * Menu
         */
        Container hudPanel = new Container("hud");
        hudPanel.setLocalTranslation(5, cam.getHeight() - 20, 0);
        guiNode.attachChild(hudPanel);
        // Create a top panel for some stats toggles.
        Container panel = new Container("panel");
        hudPanel.addChild(panel);

        panel.setBackground(new QuadBackgroundComponent(new ColorRGBA(0, 0.5f, 0.5f, 0.5f), 5, 5, 0.02f, false));
        panel.addChild(new Label("Settings"));
        panel.addChild(new Panel(2, 2, ColorRGBA.Cyan)).setUserData(LayerComparator.LAYER, 2);

        cbSphereAttached = panel.addChild(new Checkbox("Attach Sphere"));
        cbSphereAttached.setChecked(false);
        cbSphereAttached.addMouseListener(new ConsumingMouseListener(){

            @Override
            public void mouseButtonEvent(MouseButtonEvent event, Spatial target, Spatial capture) {
                 System.out.println("hdsgjk");
            }
        
        });


    }

    @Override
    public void simpleUpdate(float tpf) {
        if (sphere != null && cbSphereAttached.isChecked()) {
            sphere.setLocalTranslation(getCamera().getLocation());
            sphere.setLocalRotation(getCamera().getRotation());
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    public static final Quaternion PITCH270 = new Quaternion().fromAngleAxis(FastMath.PI * 3 / 2, new Vector3f(1, 0, 0));

    private void createFloor() {
        Quad q = new Quad(20f, 20f);
        Geometry floor = new Geometry("floor", q);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        Texture cube1Tex = assetManager.loadTexture(
                "Textures/Terrain/BrickWall/BrickWall.jpg");
        mat.setTexture("ColorMap", cube1Tex);

        mat.setColor("Color", ColorRGBA.Brown);
        floor.setMaterial(mat);
        floor.rotate(PITCH270);
        floor.move(getCamera().getLocation().add(new Vector3f(-3f, -1f, 3f)));
        rootNode.attachChild(floor);
    }

    private void createSphere() {
        sphere = new Geometry("Sphere", new Sphere(32, 32, 3f));

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(1f, 0.5f, 0f, 0.4f));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        mat.getAdditionalRenderState().setWireframe(true);
        sphere.setMaterial(mat);

        sphere.rotate(0f, 1f, 0f);
        sphere.setLocalTranslation(getCamera().getLocation());
        rootNode.attachChild(sphere);
    }

    private void addLights() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1f, 0f, 1f));
        rootNode.addLight(sun);
        
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection(new Vector3f(-1f, -1f, 0f));
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2);
    }
}
