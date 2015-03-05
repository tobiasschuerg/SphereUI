/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tobiasschuerg.sphere;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.event.CursorMotionEvent;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.event.DefaultCursorListener;

/**
 *
 * @author Tobias
 */
public class SphereListener extends DefaultCursorListener {

    private Vector3f oldPoint;
    private long lastmove = 0;

    @Override
    public void cursorMoved(CursorMotionEvent event, Spatial target, Spatial capture) {
        Vector3f point = event.getCollision().getContactPoint();

        if (oldPoint != null && oldPoint.distance(point) < 0.1f) {
            //count += System.currentTimeMillis();

            if (System.currentTimeMillis() - lastmove > 1000) {
                System.out.println("BÄMM");

            }


        } else {
            lastmove = System.currentTimeMillis();
            System.out.println("mouse moved " + lastmove);
        }



        oldPoint = point;
    }
}
