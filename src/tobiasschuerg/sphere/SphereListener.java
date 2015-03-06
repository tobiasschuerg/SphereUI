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
abstract public class SphereListener extends DefaultCursorListener {

    private Vector3f oldPoint;
    private long lastmove = 0;
    private boolean isPositionTriggered = true;
    private long millisTillTrigger = 2000;
    private float maxMoveDelta = 0.05f;

    @Override
    public void cursorMoved(CursorMotionEvent event, Spatial target, Spatial capture) {
        Vector3f point = event.getCollision().getContactPoint();

        // was cursor noticeablely moved
        if (oldPoint == null || oldPoint.distance(point) > maxMoveDelta) {
            lastmove = System.currentTimeMillis();
            
            if (isPositionTriggered) {
                isPositionTriggered = false;
            } else {
                System.out.println("Trigger reenabled: " + lastmove);
            } 
        } else {
            // check if position should be triggered
            if (System.currentTimeMillis() - lastmove > millisTillTrigger && !isPositionTriggered) {
                System.out.println("Trigger");
                isPositionTriggered = true;
                onCursorHold(point);
            }
        }
        oldPoint = point;
    }

    abstract public void onCursorHold(Vector3f point);
}
