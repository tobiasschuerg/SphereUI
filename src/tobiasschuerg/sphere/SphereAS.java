/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tobiasschuerg.sphere;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.event.CursorEventControl;

/**
 *
 * @author Tobias
 */
abstract public class SphereAS extends AbstractAppState {

    public static final String SPHERE_SWITCH = "ACTION.SWITCH";
    public static final String SPHERE_GROW = "ACTION.GROW";
    public static final String SPHERE_SHRINK = "ACTION.SHRINK";
    private Geometry sphere;
    private SimpleApplication app;
    private boolean isSphereShowing = true;
    private Checkbox cbSphereAttached;
    private ActionListener sphereListener = new ActionListener() {
        public void onAction(String name, boolean pressed, float tpf) {

            if (name.equalsIgnoreCase(SPHERE_SWITCH) && pressed) {
                if (isSphereShowing) {
                    sphere.setCullHint(Spatial.CullHint.Always);
                    isSphereShowing = false;
                } else {
                    sphere.setCullHint(Spatial.CullHint.Inherit);
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
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;

        createSphere();

        app.getInputManager().addMapping(SPHERE_SWITCH, new KeyTrigger(KeyInput.KEY_X));
        app.getInputManager().addMapping(SPHERE_GROW, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        app.getInputManager().addMapping(SPHERE_SHRINK, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        app.getInputManager().addListener(sphereListener, SPHERE_SWITCH);
        app.getInputManager().addListener(sphereListener, SPHERE_GROW);
        app.getInputManager().addListener(sphereListener, SPHERE_SHRINK);

        CursorEventControl.addListenersToSpatial(sphere, new SphereListener() {
            @Override
            public void onCursorHold(Vector3f point) {
                onCursorHoldOnSphere(point);
            }
        });

    }

    abstract public void onCursorHoldOnSphere(Vector3f point);

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (sphere != null && cbSphereAttached.isChecked()) {
            sphere.setLocalTranslation(app.getCamera().getLocation());
            sphere.setLocalRotation(app.getCamera().getRotation());
        }
    }

    private void createSphere() {
        sphere = new Geometry("Sphere", new Sphere(32, 32, 3f));

        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(1f, 0.5f, 0f, 0.4f));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setWireframe(true);
        sphere.setMaterial(mat);

        sphere.rotate(0f, 1f, 0f);
        sphere.setLocalTranslation(app.getCamera().getLocation());
        app.getRootNode().attachChild(sphere);
    }

    void setCheckbox(Checkbox checkBox) {
        this.cbSphereAttached = checkBox;
    }
}
