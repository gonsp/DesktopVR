package gonmolon.desktopvr;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.google.vr.sdk.audio.GvrAudioEngine;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public class VRView extends GvrView implements GvrView.StereoRenderer {

    private static final String TAG = "GvrView";
    private static final float CAMERA_Z = 0.01f;
    private static final float[] LIGHT_POS_IN_WORLD_SPACE = new float[] {0.0f, 2.0f, 0.0f, 1.0f};
    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 100.0f;

    private GvrAudioEngine vrAudio;
    protected ShaderContainer shaderContainer;

    protected float[] camera;
    private float[] headView;
    protected float[] view;
    protected float[] lightPosInEyeSpace;

    private Floor floor;
    private Window window;

    public VRView(final Context context) {
        super(context);
        setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        setRenderer(this);
        setTransitionViewEnabled(true);
        setOnCardboardBackButtonListener(
                new Runnable() {
                    @Override
                    public void run() {
                        ((Activity)context).onBackPressed();
                    }
                }
        );
        camera = new float[16];
        headView = new float[16];
        view = new float[16];
        lightPosInEyeSpace = new float[4];
        shaderContainer = new ShaderContainer(context);
        vrAudio = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
    }

    @Override
    public void onPause() {
        vrAudio.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        vrAudio.resume();
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f);

        floor = new Floor(this);
        window = new Window(this, "test", 100, 50, 0, 10, 20);
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        headTransform.getHeadView(headView, 0);
        checkGLError("onReadyToDraw");
    }

    @Override
    public void onDrawEye(Eye eye) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        checkGLError("colorParam");

        Matrix.multiplyMM(view, 0, eye.getEyeView(), 0, camera, 0);
        Matrix.multiplyMV(lightPosInEyeSpace, 0, view, 0, LIGHT_POS_IN_WORLD_SPACE, 0);

        float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);
        floor.draw(perspective);
        window.draw(perspective);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {}

    @Override
    public void onRendererShutdown() {}

    public static void checkGLError(String label) {
        int error;
        while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }

    public void addWindow() {

    }

    public void onCardboardTrigger() {

    }
}
