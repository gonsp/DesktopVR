package gonmolon.desktopvr.vr;

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
    }

    @Override
    public void onClick(double x, double y) {
        if(listener != null) {
            listener.onClick(x, y);
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
        if(listener != null) {
            listener.onStartLooking();
        }
    }

    @Override
    public void onStopLooking() {
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
