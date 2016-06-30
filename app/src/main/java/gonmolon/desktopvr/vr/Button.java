package gonmolon.desktopvr.vr;

import org.rajawali3d.math.vector.Vector3;

public class Button extends Element implements VRListener {

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
        setPosition(actPos.x, actPos.y, actPos.z + 0.1);
        if(listener != null) {
            listener.onStartLooking();
        }
    }

    @Override
    public void onStopLooking() {
        Vector3 actPos = getPosition();
        setPosition(actPos.x, actPos.y, actPos.z - 0.1);
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
