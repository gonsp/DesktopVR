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
    public void onStartLooking() {
        if(listener != null) {
            listener.onStartLooking();
        }
    }

    @Override
    public void onStopLooking() {

    }

    @Override
    public void onLongLooking() {
        if(listener != null) {
            listener.onLongLooking();
        }
    }

    @Override
    public void onClick() {
        if(listener != null) {
            listener.onClick();
        }
    }
}
