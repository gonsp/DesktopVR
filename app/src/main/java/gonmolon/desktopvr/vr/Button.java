package gonmolon.desktopvr.vr;

import org.rajawali3d.math.vector.Vector3;

public class Button extends Element implements VRListener {

    private float focusZoom = 0.1f;
    private VRListener listener;

    public Button(float width, float height) {
        super(width, height);
    }

    public Button(Layout parent, float width, float height) {
        super(parent, width, height);
    }

    public void setVRListener(VRListener listener) {
        this.listener = listener;
        setClickable(true);
    }

    public void setFocusZoom(float focusZoom) {
        this.focusZoom = focusZoom;
    }

    @Override
    public void onClick() {
        if(listener != null) {
            listener.onClick();
        }
    }

    @Override
    public boolean onLooking(double x, double y) {
        if(listener != null) {
            return listener.onLooking(x, y);
        }
        return false;
    }

    @Override
    public void onStartLooking() {
        Vector3 actPos = getPosition();
        setPosition(actPos.x, actPos.y, actPos.z + focusZoom);
        if(listener != null) {
            listener.onStartLooking();
        }
    }

    @Override
    public void onStopLooking() {
        Vector3 actPos = getPosition();
        setPosition(actPos.x, actPos.y, actPos.z - focusZoom);
        if(listener != null) {
            listener.onStopLooking();
        }
    }

    @Override
    public void onLongLooking() {
        if(listener != null) {
            listener.onLongLooking();
        }
    }
}
