package tobiasschuerg.sphere;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.LayerComparator;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.event.ConsumingMouseListener;

import tobiasschuerg.lemur.piemenu.example.CameraMovementFunctions;
import tobiasschuerg.lemur.piemenu.example.CameraMovementState;
import tobiasschuerg.lemur.piemenu.example.CameraToggleState;

/**
 * test
 *
 * @author Tobias Schürg
 */
public class SphereUIExample extends SimpleApplication {

    public static void main(String[] args) {
        SphereUIExample app = new SphereUIExample();
        app.start();
    }

    public SphereUIExample() {
        super(new StatsAppState(), new CameraMovementState(), new CameraToggleState());
    }

    @Override
    public void simpleInitApp() {

        GuiGlobals.initialize(this);
        CameraMovementFunctions.initializeDefaultMappings(GuiGlobals.getInstance().getInputMapper());

        createFloor();
        addLights();

        SphereAS sphereAppState = new SphereAS() {
            @Override
            public void onCursorHoldOnSphere(Vector3f point) {
                Spatial teapot = getAssetManager().loadModel("Models/Teapot/Teapot.obj");
                teapot.setLocalTranslation(point);
                teapot.setLocalScale(0.5f);
                getRootNode().attachChild(teapot);
            }
        };
        getStateManager().attach(sphereAppState);

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

        Checkbox cbSphereAttached = panel.addChild(new Checkbox("Attach Sphere"));
        cbSphereAttached.setChecked(false);
        cbSphereAttached.addMouseListener(new ConsumingMouseListener() {
            @Override
            public void mouseButtonEvent(MouseButtonEvent event, Spatial target, Spatial capture) {
                System.out.println("hdsgjk");
            }
        });
        // TODO: remove : temp
        sphereAppState.setCheckbox(cbSphereAttached);
    }

    @Override
    public void simpleUpdate(float tpf) {
        // empty
    }

    @Override
    public void simpleRender(RenderManager rm) {
        // empty
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
