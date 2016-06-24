package gonmolon.desktopvr.vr;

import gonmolon.desktopvr.R;

public class Button extends Element {

    private static final String SHADER_NAME = "Button";
    private VRListener listener;

    public Button(VRView vrView) {
        super(vrView, SHADER_NAME, R.raw.light_vertex, R.raw.model_fragment, COORDS, NORMALS, COLORS);
    }

    public void setVRListener(VRListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStartLooking() {
        if(listener != null) {
            listener.onStartLooking(); //Param relative position
        }
    }

    @Override
    public void onStopLooking() {

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

    public static final float[] COORDS = new float[]{
            -1, 1, 0,
            -1, -1, 0,
            1, 1, 0,
            -1, -1, 0,
            1, -1, 0,
            1, 1, 0
    };

    public static final float[] NORMALS = new float[] {
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
    };

    public static final float[] COLORS = new float[] {
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
    };
}
