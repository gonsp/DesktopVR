package gonmolon.desktopvr;

public class Button extends Model {

    private VRListener listener;

    public Button() {

    }

    public void setVRListener(VRListener listener) {
        this.listener = listener;
    }

    @Override
    public void onLooking() {
        if(listener != null) {
            listener.onLooking(); //Param relative position
        }
    }

    @Override
    public void onLongLooking() {
        if(listener != null) {
            listener.onLongLooking(); //Param relative position
        }
    }

    @Override
    public void onClick() {
        if(listener != null) {
            listener.onClick(); //Param relative position
        }
    }
}
