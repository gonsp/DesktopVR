package gonmolon.desktopvr;

public class Button extends Model {

    private static final String SHADER_NAME = "Button";
    private VRListener listener;

    public Button(VRView vrView) {
        super(vrView, SHADER_NAME, R.raw.light_vertex, R.raw.passthrough_fragment);
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

    @Override
    protected void draw(float[] modelView, float[] modelViewProjection) {

    }
}
