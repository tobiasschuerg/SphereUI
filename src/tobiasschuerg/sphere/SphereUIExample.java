package tobiasschuerg.sphere;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.light.AmbientLight;
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
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.MouseEventControl;

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

        //WireBox b = new WireBox(1, 1, 1);
        //b.setLineWidth(0.1f);
        // Box b = new Box(1f, 1f, 1f);
        Sphere b = new Sphere(32, 32, 5f);

        Geometry geom = new Geometry("Box", b);
        // b.setMesh(Mesh.Mode.Lines);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(1f, 0.5f, 0f, 0.8f));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        mat.getAdditionalRenderState().setWireframe(true);
        geom.setMaterial(mat);

        geom.rotate(0f, 1f, 0f);
        geom.setLocalTranslation(getCamera().getLocation());

        CursorEventControl.addListenersToSpatial(geom, new SphereListener());

        // add light
        PointLight lamp_light = new PointLight();

        lamp_light.setColor(ColorRGBA.Yellow);

        lamp_light.setRadius(
                4f);
        lamp_light.setPosition(
                new Vector3f());
        rootNode.addLight(lamp_light);
        AmbientLight al = new AmbientLight();

        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
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
}
