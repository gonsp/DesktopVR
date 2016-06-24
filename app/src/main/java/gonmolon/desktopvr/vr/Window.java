package gonmolon.desktopvr.vr;

import android.util.Log;

import gonmolon.desktopvr.R;

public class Window extends Element {

    private static final String SHADER_NAME = "Window";
    private String name;

    public Window(VRView vrView, String name, float width, float height, float x, float y, float z) {
        super(vrView, SHADER_NAME, R.raw.light_vertex, R.raw.model_fragment, COORDS, NORMALS, COLORS);
        this.name = name;

        scale(width, height);
        move(x, y, z);
    }

    @Override
    public void onClick() {
        Log.d(SHADER_NAME, "TOCADO");
    }

    @Override
    public void onStartLooking() {
        Log.d(SHADER_NAME, "HELLOUUUU");
    }

    @Override
    public void onStopLooking() {

    }

    @Override
    public void onLongLooking() {

    }

    public static final float[] COORDS = new float[] {
            -1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f
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
